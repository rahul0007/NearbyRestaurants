package com.nearby.task.database
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.nearby.task.database.wishlist.WishlistMastersDAO
import com.nearby.task.database.wishlist.NearByMaster


@Database(entities = arrayOf((NearByMaster::class)), version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun wishlistMastersDAO(): WishlistMastersDAO

    companion object {
        private var dbInstance: AppDatabase? = null

        fun getAppDB(context: Context): AppDatabase {
            if (dbInstance == null) {
                dbInstance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "NearByDB"
                )
                    .allowMainThreadQueries()
                    .build()
            }
            return dbInstance!!
        }
    }
}