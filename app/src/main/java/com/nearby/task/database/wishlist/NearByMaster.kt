package com.nearby.task.database.wishlist
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
@Entity(tableName = "NearByMaster")
@Parcelize
data class NearByMaster(
    @PrimaryKey(autoGenerate = true)
    var id: Int?=0,
    @ColumnInfo(name = "Name")
    var name: String? = null,
    @ColumnInfo(name = "Lat")
    var lat: String? = null,
    @ColumnInfo(name = "Lng")
    var lng: String? = null,
    @ColumnInfo(name = "Distance")
    var distance: String? = null,
    @ColumnInfo(name = "Rating")
    var rating: Float? = null,
    @ColumnInfo(name = "Address")
    var address: String? = null,

): Parcelable

