package com.example.ecosortapp

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class Maps : AppCompatActivity() {

    private lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_maps)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.map)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync { googleMap ->
            map = googleMap
            setUpMap()
        }
    }

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION_PERMISSION)
            return
        }

        map.isMyLocationEnabled = true

        // Here you can add markers for garbage collection centers
        val garbageCollectionCenters = listOf(
            LatLng(37.7749, -122.4194), // Sample coordinates for a garbage collection center
            LatLng(40.7128, -74.0060)   // Sample coordinates for another garbage collection center
        )

        // Add markers for garbage collection centers
        garbageCollectionCenters.forEach {
            map.addMarker(
                MarkerOptions()
                    .position(it)
                    .title("Garbage Collection Center")
            )
        }

        // Example: Move camera to a specific location
        val truckLocation = LatLng(37.7749, -122.4194) // Sample truck location
        map.moveCamera(CameraUpdateFactory.newLatLng(truckLocation))
        map.animateCamera(CameraUpdateFactory.zoomTo(10f))
    }

    companion object {
        private const val REQUEST_LOCATION_PERMISSION = 1
    }
}