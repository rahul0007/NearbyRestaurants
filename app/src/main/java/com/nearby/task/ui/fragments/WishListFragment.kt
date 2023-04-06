package com.nearby.task.ui.fragments
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.nearby.task.R
import com.nearby.task.database.wishlist.NearByMaster
import com.nearby.task.ui.activity.home.GoogleMapActivity
import com.nearby.task.viewModel.WishListViewModel
import com.nearby.task.ui.adapter.WishListAdapter
import com.nearby.task.ui.base.BaseFragment
import com.nearby.task.utils.hide
import com.nearby.task.utils.show
import kotlinx.android.synthetic.main.fragment_wishlist.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class WishListFragment() : BaseFragment() {
    lateinit var wishListViewModel: WishListViewModel
    private var wishListAdapter: WishListAdapter? = null
    override fun getLayoutId(): Int = R.layout.fragment_wishlist
    override fun onBinding() {
        wishListViewModel = ViewModelProvider(this)[WishListViewModel::class.java]
        setUpUI()
        setObserver()
    }

    override fun showErrorMessage(error: String?, view: View?) {

    }

    override fun showSuccessMessage(message: String?, view: View?) {
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    private fun setUpUI() {
        wishListAdapter = WishListAdapter(requireContext(), object :
            WishListAdapter.OnItemClickStatusListener {
            override fun itemsClicked(nearByMaster: NearByMaster) {
                val bundle= Bundle()
                bundle.putParcelable("NearByData",nearByMaster)
                GoogleMapActivity.start(activity!!,bundle)
            }
            override fun addORRemoveWishList(nearBy: NearByMaster) {
                wishListViewModel.addToWishlist(nearBy)
                setPlaceHolder()
            }
        })
        recyclerViewPlace.adapter = wishListAdapter
    }

    private fun setObserver() {
        wishListViewModel.wishlistDelegates.observe(viewLifecycleOwner) {
            it?.let { it1 -> wishListAdapter?.update(it1 as ArrayList) }
            setPlaceHolder()
        }
    }

    fun setPlaceHolder()
    {
        lifecycleScope.launch {
            delay(100)
            if (wishListAdapter?.itemCount!! > 0) {
                recyclerViewPlace.show()
                textviewPlaceHolder.hide()
            } else {
                recyclerViewPlace.hide()
                textviewPlaceHolder.show()
            }
        }
    }
}

