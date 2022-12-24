package com.ferhatozcelik.mapview

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.widget.LinearLayout
import com.ferhatozcelik.mapview.R
import com.ferhatozcelik.mapview.interfaces.IPlatform
import com.ferhatozcelik.mapview.interfaces.OnReceiveMapCallback
import com.ferhatozcelik.mapview.model.Place

class MapsView : LinearLayout {

    private lateinit var iMapsView: IPlatform

    fun addMarkers(coordinates: MutableList<Place>) = iMapsView.addMarkers(coordinates)

    constructor(
        iMapsView: IPlatform,
        mapViewBundle: Bundle?,
        activity: Activity,
        callback: OnReceiveMapCallback<Any>
    ) : super(
        activity.applicationContext
    ) {
        this.iMapsView = iMapsView
        iMapsView.setMap(mapViewBundle, activity, callback)
    }

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init(attrs, defStyle)
    }

    @SuppressLint("CustomViewStyleable")
    private fun init(attrs: AttributeSet?, defStyle: Int) {
        val a = context.obtainStyledAttributes(
            attrs, R.styleable.MapsView, defStyle, 0
        )
        a.recycle()
    }
}