package com.soflex.sisep.osmdroidtest

import android.Manifest
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.core.app.ActivityCompat
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.MapTileProviderBasic
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.tileprovider.tilesource.XYTileSource
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.CopyrightOverlay
import org.osmdroid.views.overlay.TilesOverlay
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay



class MainActivity : AppCompatActivity() {
    private lateinit var mLocationOverlay: MyLocationNewOverlay
    private var mMapView: MapView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), 1)

        //OSM requiere un agent
        Configuration.getInstance().setUserAgentValue("SISEP")

        val mapContainer = findViewById<LinearLayout>(R.id.map_container)

        mMapView = MapView(this)
        mMapView!!.isTilesScaledToDpi = true

        mapContainer.addView(
            mMapView, RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
            )
        )
        mMapView!!.zoomController.setVisibility(
            CustomZoomButtonsController.Visibility.SHOW_AND_FADEOUT
        )

        // zoom to the netherlands
        mMapView!!.controller.setZoom(17.0)
        mMapView!!.controller.setCenter(GeoPoint(-34.5961, -58.3830))

        val tileProvider = MapTileProviderBasic(applicationContext)
        val tileSource = XYTileSource(
            "Mapa", 3, 18, 256, ".png",
            arrayOf("http://a.tile.stamen.com/toner/")
        )
        tileProvider.tileSource = tileSource
        tileProvider.tileRequestCompleteHandlers.add(mMapView!!.tileRequestCompleteHandler)
        val tilesOverlay = TilesOverlay(tileProvider, this.baseContext)
        tilesOverlay.loadingBackgroundColor = Color.TRANSPARENT
        mMapView!!.overlays.add(tilesOverlay)

        this.mLocationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(this), mMapView)
        this.mLocationOverlay.enableMyLocation()
        mMapView!!.overlays.add(this.mLocationOverlay)

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    public override fun onPause() {
        super.onPause()
        mMapView!!.onPause()
    }

    public override fun onResume() {
        super.onResume()
        mMapView!!.onResume()
    }

}
