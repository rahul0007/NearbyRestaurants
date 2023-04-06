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
import kotlinx.android.synthetic.main.row_nearby.view.*

class NearByAdapter(
    var context: Context,
    var listner: OnItemClickStatusListener
) : RecyclerView.Adapter<NearByAdapter.ViewHolder>() {
    var items = ArrayList<NearByMaster>()
    var appDatabase: AppDatabase? = null

    init {
        appDatabase = AppDatabase.getAppDB(context)
    }

    lateinit var view: View
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NearByAdapter.ViewHolder {
        view = LayoutInflater.from(context).inflate(R.layout.row_nearby, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: NearByAdapter.ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun update(it: ArrayList<NearByMaster>) {
        items.clear()
        items.addAll(it)
        notifyDataSetChanged()
    }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        fun bind(items: NearByMaster) {
            itemView.textViewCategoryName.text = items.name
            itemView.rtbProductRating.rating = items.rating!!
            itemView.textViewAddress.text = items.address!!
            itemView.textviewKM.text = items.distance!!
            Glide.with(context)
                .load(R.drawable.ic_default_images)
                .apply(RequestOptions().transforms(CenterCrop(), RoundedCorners(100)))
                .into(itemView.imageViewCategory)

            itemView.imageViewAddToWishlist.setOnClickListener {
                listner.addORRemoveWishList(items)
                notifyDataSetChanged()
            }

            itemView.imageViewDirection.setOnClickListener {
               listner.itemsClicked(items)
            }

            if (appDatabase?.wishlistMastersDAO()?.readWishlist()?.contains(items) == true) {
                itemView.imageViewAddToWishlist.setBackgroundResource(R.drawable.ic_fill_heart)
            } else {
                itemView.imageViewAddToWishlist.setBackgroundResource(R.drawable.ic_heart)
            }
        }
    }
    interface OnItemClickStatusListener {
        fun itemsClicked(categoryMasters: NearByMaster)
        fun addORRemoveWishList(nearBy: NearByMaster)
    }
}
