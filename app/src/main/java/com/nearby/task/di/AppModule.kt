package com.nearby.task.di
import android.app.Application
import android.content.Context
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.nearby.task.database.AppDatabase
import com.nearby.task.database.wishlist.WishlistMastersDAO
import com.nearby.task.extentions.HttpErrorInterceptor
import com.nearby.task.network.ApiService
import com.nearby.task.utils.Constant
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AppModule {


    private val NETWORK_TIMEOUT = 10L

    @Provides
    fun providesBaseUrl(): String = Constant.BaseURL

    @Provides
    @Singleton
    fun getAppDB(context: Application): AppDatabase {
        return AppDatabase.getAppDB(context)
    }

    @Provides
    @Singleton
    fun getWishlistMastersDAO(appDb: AppDatabase): WishlistMastersDAO {
        return appDb.wishlistMastersDAO()
    }

    @Provides
    @Singleton
    fun providesRetrofitBuilder(baseUrl: String, @ApplicationContext context: Context): Retrofit =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(getClient(context).build())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    fun providesApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

    val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private fun getClient(context: Context): OkHttpClient.Builder {
        return OkHttpClient.Builder().apply {
            addInterceptor(loggingInterceptor)
            addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Version", "1.0")
                chain.proceed(request.build())
            }
            connectTimeout(NETWORK_TIMEOUT, TimeUnit.MINUTES)
            readTimeout(NETWORK_TIMEOUT, TimeUnit.MINUTES)
            writeTimeout(NETWORK_TIMEOUT, TimeUnit.MINUTES)
            addInterceptor(HttpErrorInterceptor(context))
        }
    }
}

