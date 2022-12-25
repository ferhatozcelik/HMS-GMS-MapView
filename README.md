# MapView

[![](https://jitpack.io/v/ferhatozcelik/MapView.svg)](https://jitpack.io/#ferhatozcelik/MapView)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)


MapsKit is a all-in-one library that supports both HMS and GMS maps.

## Installation

Add this repository to your project-level build.gradle file.

```groovy
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

Add this dependency to your app-level build.gradle file.

```
dependencies {
            implementation 'com.github.ferhatozcelik:MapView:1.0.0'
}
```

## Usage

You'll still have to go through these steps at [HMS MapKit Codelab](https://developer.huawei.com/consumer/en/codelab/HMSMapKit/index.html#0) and [Google Add a map to your Android app](https://developers.google.com/codelabs/maps-platform/maps-platform-101-android#0)

Place the view in XML file.

```XML
<com.ferhatozcelik.mapview.MapsView
    android:id="@+id/custom_maps"
    android:layout_width="match_parent"
    android:layout_height="match_parent" />
```

Then get your Fragment/Activity ready for map initialization.

```kotlin

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
```

License
--------


    Copyright 2021 ferhatozcelik.com

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
