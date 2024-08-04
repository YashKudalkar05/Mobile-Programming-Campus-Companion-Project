package com.example.project

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.project.R
import androidx.appcompat.widget.Toolbar
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class NavigationActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    private val locationPermissionRequest: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val granted = permissions.entries.all { it.value }
            if (granted) {
                enableMyLocation()
            } else {
                // Handle the case where permissions are not granted
                // Show a message to the user or disable location-based features
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)

        // Setup Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)  // Added Toolbar setup
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in your campus and move the camera
        val campusLocation = LatLng(48.421246054756686, -89.26067794496926)
        mMap.addMarker(MarkerOptions().position(campusLocation).title("Campus"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(campusLocation, 15f))

        val RyanBuilding = LatLng(48.42124419010666, -89.25966103909835)
        mMap.addMarker(MarkerOptions().position(RyanBuilding).title("Ryan Building"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(RyanBuilding, 15f))

        val NursingBuilding = LatLng(48.422251685604195, -89.26054616806964)
        mMap.addMarker(MarkerOptions().position(NursingBuilding).title("Nursing Building "))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(NursingBuilding, 15f))

        val ChancellorPatersonLibrary = LatLng(48.42066033197862, -89.26021357415442)
        mMap.addMarker(MarkerOptions().position(ChancellorPatersonLibrary).title("Chancellor Paterson Library"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ChancellorPatersonLibrary, 15f))

        val LakeheadUniversityResidence = LatLng(48.41828922942559, -89.26393111584912)
        mMap.addMarker(MarkerOptions().position(LakeheadUniversityResidence).title("Lakehead University Residence"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LakeheadUniversityResidence, 15f))

        val LakeheadUniversityCentennialBuilding = LatLng(48.42206131225477, -89.26305246646828)
        mMap.addMarker(MarkerOptions().position(LakeheadUniversityCentennialBuilding).title("Lakehead University Centennial Building"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LakeheadUniversityCentennialBuilding, 15f))


        // Check for location permissions
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            enableMyLocation()
        } else {
            locationPermissionRequest.launch(arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ))
        }
    }

    private fun enableMyLocation() {
        if (::mMap.isInitialized) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mMap.isMyLocationEnabled = true
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Ensure that location updates are enabled when the activity is resumed
        if (::mMap.isInitialized) {
            enableMyLocation()
        }
    }

    override fun onPause() {
        super.onPause()
        // Disable location updates to save resources when the activity is paused
        if (::mMap.isInitialized) {
            // You should only disable location updates if they were previously enabled
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mMap.isMyLocationEnabled = false
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Perform any necessary cleanup when the activity is destroyed
    }

    override fun onLowMemory() {
        super.onLowMemory()
        // Release resources or data that can be recreated later if needed
    }
}
