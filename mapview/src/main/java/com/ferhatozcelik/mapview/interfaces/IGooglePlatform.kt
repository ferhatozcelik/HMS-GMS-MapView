package com.ferhatozcelik.mapview.interfaces

import android.app.Activity
import android.os.Bundle
import com.ferhatozcelik.mapview.model.Place

interface IGooglePlatform : IPlatform {
    override fun setMap(
        mapViewBundle: Bundle?,
        activity: Activity,
        callback: OnReceiveMapCallback<Any>
    )

    override fun addMarkers(coordinates: MutableList<Place>)

}