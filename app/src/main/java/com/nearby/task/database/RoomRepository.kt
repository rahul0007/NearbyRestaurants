package com.nearby.task.database

import com.nearby.task.database.wishlist.WishlistMastersDAO
import com.nearby.task.database.wishlist.NearByMaster
import javax.inject.Inject

class RoomRepository @Inject constructor(
    private val wishlistMastersDAO: WishlistMastersDAO,
    private val appDatabase: AppDatabase

) {
    fun getWishlist(): List<NearByMaster> {
        return wishlistMastersDAO.readWishlist()
    }
    fun clearAllTables() {
        appDatabase.clearAllTables()
    }
    fun insertWishlist(nearByMaster: NearByMaster) {
        wishlistMastersDAO.saveWishlist(nearByMaster)
    }
    fun removeWishlist(nearByMaster: NearByMaster) {
        wishlistMastersDAO.deleteWishlist(nearByMaster)
    }
}