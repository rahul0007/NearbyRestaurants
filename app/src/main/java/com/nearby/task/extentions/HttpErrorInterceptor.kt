package com.nearby.task.extentions

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import okhttp3.Interceptor
import okhttp3.Response

/**
Created by Janos
 **/
class HttpErrorInterceptor(val context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        if (response.code != 200 && response.code != 401) {
            handleForbiddenResponse(response.code)
        }
        return response;
    }

    private fun handleForbiddenResponse(responseCode: Int) {
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(context, "$responseCode", Toast.LENGTH_SHORT)
                .show()
        }
    }
}