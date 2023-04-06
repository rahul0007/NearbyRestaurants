package com.nearby.task.database.wishlist

import androidx.room.*

@Dao
interface WishlistMastersDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveWishlist(nearByMaster: NearByMaster)

    @Query("select * from NearByMaster")
    fun readWishlist(): List<NearByMaster>

    @Delete
    fun deleteWishlist(nearByMaster: NearByMaster)
}