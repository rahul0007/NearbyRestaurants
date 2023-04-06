package com.nearby.task.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nearby.task.database.RoomRepository
import com.nearby.task.extentions.manageNetwork
import com.nearby.task.lifecycle.ErrorModelView
import com.nearby.task.lifecycle.SingleLiveData
import com.nearby.task.database.wishlist.NearByMaster
import com.nearby.task.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse
import javax.inject.Inject

@HiltViewModel
class WishListViewModel @Inject constructor(
    private val repository: Repository,
    private val roomRepository: RoomRepository,
    @ApplicationContext val context: Context,
) : ViewModel() {
    var wishlistDelegates = SingleLiveData<List<NearByMaster>>()
    var delegateRestaurant = SingleLiveData<ArrayList<NearByMaster>?>()
    init {
        getWishList()
    }

    private fun getWishList() {
        wishlistDelegates.postValue(roomRepository.getWishlist())
    }

    fun addToWishlist(nearByMaster: NearByMaster) {
        if (roomRepository.getWishlist().contains(nearByMaster)) {
            roomRepository.removeWishlist(nearByMaster)
        } else {
            roomRepository.insertWishlist(nearByMaster)
        }
    }
}