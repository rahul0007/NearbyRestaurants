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
class NearByViewModel @Inject constructor(
    private val repository: Repository,
    private val roomRepository: RoomRepository,
    @ApplicationContext val context: Context,
) : ViewModel() {
    var categoryItemsDelegates = SingleLiveData<List<NearByMaster>>()
    var delegatesMessage = SingleLiveData<String>()
    var delegateRestaurant = SingleLiveData<ArrayList<NearByMaster>?>()

    val errorLiveData: SingleLiveData<ErrorModelView> = SingleLiveData()
    var exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        errorLiveData.postValue(
            ErrorModelView(
                messageTitle = throwable.message,
                tagException = throwable.printStackTrace().toString()
            )
        )
    }

    init {
        getNearBy()
    }

    fun doPlaces(latLong: String) {
        viewModelScope.manageNetwork(context, exceptionHandler)
            ?.launch(exceptionHandler) {
                withContext(Dispatchers.IO) {
                    repository.doPlaces(
                        "restaurants",
                        latLong,
                        "restaurants",
                        "distance",
                        "AIzaSyB1tU_7k6M6_ppWcg8A3Jj4Cp9w2xqSHoE"
                    ).apply {

                        if (this.awaitResponse().body()?.status.equals("REQUEST_DENIED")) {
                            errorLiveData.postValue(ErrorModelView("REQUEST_DENIED", "", ""))
                        }
                    }

                }
            }
    }

    fun addToWishlist(nearByMaster: NearByMaster) {
        if (roomRepository.getWishlist().contains(nearByMaster)) {
            roomRepository.removeWishlist(nearByMaster)
        } else {
            roomRepository.insertWishlist(nearByMaster)
        }
    }


    private fun getNearBy() {
        val list = ArrayList<NearByMaster>()
        list.add(
            NearByMaster(
                1,
                "હોટલ જાહલ",
                "21.1541",
                "70.0770",
                "3.1 miles",
                2f,
                "Keas 69 Str. 15234, Chalandri Athens keshod"
            )
        )
        list.add(
            NearByMaster(
                2,
                "Jay Vadvada hotel",
                "21.0640",
                "70.2090",
                "1 miles",
                2f,
                "Keas 69 Str. 15234, Chalandri Athens keshod"
            )
        )
        list.add(
            NearByMaster(
                3,
                "માલધારી પાન લોજ",
                "21.1756",
                "70.0672",
                "4.2 miles",
                2f,
                "Keas 69 Str. 15234, Chalandri Athens keshod"
            )
        )
        list.add(
            NearByMaster(
                4,
                "Hotel shakti",
                "21.19931",
                "70.05111",
                "1 miles",
                2f,
                "Keas 69 Str. 15234, Chalandri Athens keshod"
            )
        )
        list.add(
            NearByMaster(
                5,
                "PURUSHARTH HOTEL",
                "21.18971",
                "70.05351",
                "1 miles",
                2f,
                "Keas 69 Str. 15234, Chalandri Athens keshod"
            )
        )
        list.add(
            NearByMaster(
                6,
                "SHIV SHAKTI FARSHAN",
                "21.1961",
                "70.04973",
                "1 miles",
                2f,
                "Keas 69 Str. 15234, Chalandri Athens keshod"
            )
        )
        list.add(
            NearByMaster(
                7,
                "MAHADEV PUNJABI & CHINESE",
                "21.1938",
                "70.0493",
                "1 miles",
                2f,
                "Keas 69 Str. 15234, Chalandri Athens keshod"
            )
        )
        delegateRestaurant.postValue(list)
    }
}