package com.ferhatozcelik.mapview.example

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ferhatozcelik.mapview.example.databinding.ActivityMapsBinding
import com.ferhatozcelik.mapview.MapsView
import com.ferhatozcelik.mapview.interfaces.OnReceiveMapCallback
import com.ferhatozcelik.mapview.model.Place
import com.ferhatozcelik.mapview.platform.GooglePlatform
import com.ferhatozcelik.mapview.platform.HuaweiPlatform
import com.ferhatozcelik.mapview.util.PlatformType
import com.ferhatozcelik.mapview.util.getPlatformType
import com.ferhatozcelik.mapview.util.isHmsAvailable

private const val MAPVIEW_BUNDLE_KEY = "MapViewBundleKey"

class MapsActivity : AppCompatActivity(), OnReceiveMapCallback<Any> {

    private lateinit var binding: ActivityMapsBinding
    private lateinit var platformType:PlatformType
    private lateinit var map: Any
    private var mapsView: MapsView? = null
    private var coordinates = mutableListOf<Place>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var mapViewBundle: Bundle? = null
        if (savedInstanceState != null) {
            mapViewBundle =
                savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY)
        }

        val sharedPref = getPreferences(Context.MODE_PRIVATE)

        val initPlatform = sharedPref.getString("init_platform", null);

        if (initPlatform == null){
            platformType = getPlatformType(application)
        }else{
            when(initPlatform){
                "HMS" -> {
                    platformType = PlatformType.HMS
                }
                "GMS" -> {
                    platformType = PlatformType.GMS
                }
            }
        }

        initMapsView(mapViewBundle)

        binding.hmsChangeButton.setOnClickListener {
            if(isHmsAvailable(this)){
                sharedPref.edit().putString("init_platform", "HMS").apply()
                val i = Intent(this, MapsActivity::class.java)
                i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(i)
            }else{
                Toast.makeText(applicationContext, "HMS Core Service Not Found", Toast.LENGTH_SHORT).show()
            }
        }
        binding.gmsChangeButton.setOnClickListener {
            sharedPref.edit().putString("init_platform", "GMS").apply()
            val i = Intent(this, MapsActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(i)
        }

    }

    private fun initMapsView(mapViewBundle: Bundle?) {
        mapsView = when (platformType) {
            PlatformType.GMS -> {
                MapsView(GooglePlatform(
                    binding.customMaps
                ), mapViewBundle, this, this)
            }
            PlatformType.HMS -> {
                MapsView(
                    HuaweiPlatform(
                        binding.customMaps
                    ), mapViewBundle, this, this
                )
            }
            else -> null
        }

        setCoordinates()
    }

    private fun setCoordinates() {
        coordinates.add(
            Place(
                41.028985,
                29.117591,
                "Huawei R&D"
            )
        )
    }

    override fun onMap(map: Any) {
        this@MapsActivity.map = map
        mapsView?.addMarkers(coordinates)
    }

}