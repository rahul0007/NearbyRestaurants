package com.nearby.task.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.google.android.gms.maps.model.LatLng
import com.nearby.task.R
import com.nearby.task.database.wishlist.NearByMaster
import com.nearby.task.ui.activity.home.GoogleMapActivity
import com.nearby.task.viewModel.NearByViewModel
import com.nearby.task.ui.adapter.NearByAdapter
import com.nearby.task.ui.base.BaseActivity
import com.nearby.task.ui.base.BaseFragment
import com.nearby.task.utils.LocationManager
import com.nearby.task.utils.showToast
import kotlinx.android.synthetic.main.fragment_near_by.*

class NearByFragment : BaseFragment() {
    lateinit var homeViewModel: NearByViewModel
    var nearByAdapter: NearByAdapter? = null
    override fun getLayoutId(): Int = R.layout.fragment_near_by
    override fun onBinding() {
        homeViewModel = ViewModelProvider(this)[NearByViewModel::class.java]

        locationManager.triggerLocation(object : LocationManager.LocationListener {
            override fun onLocationAvailable(location: LatLng) {
                locationManager.stop()
                // homeViewModel.doPlaces(location.toString())
            }

            override fun onFail(status: LocationManager.LocationListener.Status) {
                Log.e("", "onFail: $status")
            }

        })

        setObserver()
        setUpUI()
    }


    private fun setObserver() {
        homeViewModel.errorLiveData.observe(viewLifecycleOwner) {
            it.messageTitle?.let { it1 -> requireContext().showToast(it1) }
        }

        homeViewModel.delegateRestaurant.observe(viewLifecycleOwner) {
            it?.let { it1 -> nearByAdapter?.update(it1) }
        }
    }

    private fun setUpUI() {
        nearByAdapter = NearByAdapter(requireContext(), object :
            NearByAdapter.OnItemClickStatusListener {
            override fun itemsClicked(nearByMaster: NearByMaster) {
//                NavHostFragment.findNavController(requireParentFragment())
//                    .navigate(R.id.action_navigation_near_by_to_fragment_direction_map)
                val bundle= Bundle()
                bundle.putParcelable("NearByData",nearByMaster)
                GoogleMapActivity.start(activity!!,bundle)

            }

            override fun addORRemoveWishList(nearBy: NearByMaster) {
                homeViewModel.addToWishlist(nearBy)
            }
        })
        recyclerViewPlace.adapter = nearByAdapter

    }

    override fun showErrorMessage(error: String?, view: View?) {
    }

    override fun showSuccessMessage(message: String?, view: View?) {
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }
}