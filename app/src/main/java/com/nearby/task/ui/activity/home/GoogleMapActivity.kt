package com.nearby.task.ui.activity.home

import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolylineOptions
import com.nearby.task.R
import com.nearby.task.database.wishlist.NearByMaster
import com.nearby.task.ui.base.BaseActivity
import com.nearby.task.utils.DrawMarker
import com.nearby.task.utils.LocationManager
import com.nearby.task.utils.MyInfoWindowAdapter
import kotlinx.android.synthetic.main.custom_toolbar.*

class GoogleMapActivity : BaseActivity() , OnMapReadyCallback {
    lateinit var nearByMaster:NearByMaster
    var googleMap: GoogleMap? = null
    var latLng = LatLng(0.0, 0.0)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun findFragmentPlaceHolder() = 0
    override fun findContentView() = com.nearby.task.R.layout.activity_map
    override fun observeViewModel() {
        val extras = intent.extras
        if (extras != null) {
            nearByMaster = extras.getParcelable("NearByData")!!
            textViewTitle.text=nearByMaster.name
        }
        setMap()

        locationManager.triggerLocation(object : LocationManager.LocationListener {
            override fun onLocationAvailable(location: LatLng) {
                locationManager.stop()
                latLng = location
                googleMap?.clear()
                addMarker()
            }

            override fun onFail(status: LocationManager.LocationListener.Status) {
                Log.e("", "onFail: $status")
            }
        })
    }


    override fun onMapReady(googleMap: GoogleMap?) {
        this.googleMap = googleMap

    }
    private fun setMap()
    {
        val mapFragment = supportFragmentManager.findFragmentById(com.nearby.task.R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
    }

    companion object {
        fun start(context: FragmentActivity,bundle: Bundle) {
            val intent = Intent(context, GoogleMapActivity::class.java)
            intent.putExtras(bundle)
            context.startActivity(intent)
        }
    }

    fun addMarker()
    {
        googleMap?.mapType = GoogleMap.MAP_TYPE_NORMAL
        googleMap?.uiSettings?.isZoomControlsEnabled = true
        googleMap?.uiSettings?.isZoomGesturesEnabled = true
        googleMap?.uiSettings?.isCompassEnabled = true
        val lineOptions = PolylineOptions()
        val points = ArrayList<LatLng?>()
        val destination = LatLng(nearByMaster.lat?.toDouble()!!, nearByMaster.lng?.toDouble()!!)
        points.add(latLng)
        points.add(destination)
        lineOptions.addAll(points)
        lineOptions.width(12f)
        lineOptions.color(ContextCompat.getColor(this, R.color.colorPrimary))
        googleMap?.addPolyline(lineOptions)
        DrawMarker.getInstance(this)
            ?.draw(googleMap!!, latLng, R.drawable.ic_my_loc, "You")
        DrawMarker.getInstance(this)
            ?.draw(googleMap!!, destination, R.drawable.ic_rest_loc, "Destination")
        val bounds = LatLngBounds.Builder()
            .include(latLng)
            .include(destination).build()
        val displaySize = Point()
        windowManager?.defaultDisplay?.getSize(displaySize)
        googleMap?.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, displaySize.x, 250, 30))
        googleMap?.setInfoWindowAdapter(MyInfoWindowAdapter(this))

    }
}