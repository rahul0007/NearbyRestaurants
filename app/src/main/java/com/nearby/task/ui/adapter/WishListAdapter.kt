package com.nearby.task.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.nearby.task.R
import com.nearby.task.database.AppDatabase
import com.nearby.task.database.wishlist.NearByMaster
import kotlinx.android.synthetic.main.row_wishlist.view.*

class WishListAdapter(
    var context: Context,
    var listner: OnItemClickStatusListener
) : RecyclerView.Adapter<WishListAdapter.ViewHolder>() {
    var wishList = ArrayList<NearByMaster>()

    lateinit var view: View
    var appDatabase: AppDatabase? = null

    init {
        appDatabase = AppDatabase.getAppDB(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WishListAdapter.ViewHolder {
        view = LayoutInflater.from(context).inflate(R.layout.row_wishlist, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return wishList.size
    }

    override fun onBindViewHolder(holder: WishListAdapter.ViewHolder, position: Int) {
        holder.bind(wishList[position])
    }

    fun update(it: ArrayList<NearByMaster>) {
        wishList.clear()
        wishList.addAll(it)
        notifyDataSetChanged()
    }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        fun bind(items: NearByMaster) {
            appDatabase?.wishlistMastersDAO()?.readWishlist()
            itemView.textViewCategoryName.text = items.name
            itemView.rtbProductRating.rating = items.rating!!
            itemView.textviewKM.text = items.distance!!
            itemView.textViewAddress.text = items.address!!
            Glide.with(context)
                .load(R.drawable.ic_default_images)
                .apply(RequestOptions().transforms(CenterCrop(), RoundedCorners(100)))
                .into(itemView.imageViewCategory)
            itemView.imageViewAddToWishlist.setOnClickListener {
                listner.addORRemoveWishList(items)
                wishList.remove(items)
                notifyDataSetChanged()
            }
        }
    }

    interface OnItemClickStatusListener {
        fun itemsClicked(categoryMasters: NearByMaster)
        fun addORRemoveWishList(nearBy: NearByMaster)
    }
}
