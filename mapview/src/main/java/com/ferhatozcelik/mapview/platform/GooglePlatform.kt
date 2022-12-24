package com.ferhatozcelik.mapview.platform

import android.app.Activity
import android.os.Bundle
import android.view.View
import com.ferhatozcelik.mapview.interfaces.IGooglePlatform
import com.ferhatozcelik.mapview.interfaces.OnReceiveMapCallback
import com.ferhatozcelik.mapview.model.Place
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class GooglePlatform(private val mapsView: com.ferhatozcelik.mapview.MapsView) : IGooglePlatform,
    OnMapReadyCallback {

    private lateinit var onReceiveMapCallback: OnReceiveMapCallback<Any>
    private lateinit var googleMap: GoogleMap

    override fun setMap(
        mapViewBundle: Bundle?,
        activity: Activity,
        callback: OnReceiveMapCallback<Any>
    ) {
        onReceiveMapCallback = callback
        val mapView = MapView(activity.baseContext)
        mapView.apply {
            onCreate(mapViewBundle)
            getMapAsync(this@GooglePlatform)
        }
        mapsView.addView(mapView as View?)
        mapView.onResume()
    }

    override fun addMarkers(coordinates: MutableList<Place>) {
        for (place in coordinates) {
            val coordinate = LatLng(place.latitude, place.longitude)
            googleMap.addMarker(MarkerOptions().position(coordinate).title(place.title))
        }
        if (coordinates.isNotEmpty()) {
            val firstCoordinate = LatLng(coordinates[0].latitude, coordinates[0].longitude)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstCoordinate, 13f))
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        onReceiveMapCallback.onMap(googleMap)
    }

}