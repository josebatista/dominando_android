package dominando.android.mapas

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MainActivity : AppCompatActivity() {

    private var googleMap: GoogleMap? = null
    private var origin = LatLng(-23.561706, -46.655981)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragment = supportFragmentManager.findFragmentById(R.id.fragmentMap) as SupportMapFragment
        fragment.getMapAsync { map -> initMap(map) }
    }

    private fun initMap(map: GoogleMap?) {
        googleMap = map?.apply {
            mapType = GoogleMap.MAP_TYPE_SATELLITE
        }
        updateMap()
    }

    private fun updateMap() {
        googleMap?.run {
            //            animateCamera(CameraUpdateFactory.newLatLngZoom(origin, 17.0f))
//            addMarker(
//                MarkerOptions()
//                    .position(origin)
//                    .title("Av. Paulista")
//                    .snippet("São Paulo")
//            )

            mapType = GoogleMap.MAP_TYPE_NORMAL
            uiSettings?.isZoomControlsEnabled = true
            uiSettings?.isMapToolbarEnabled = false

            val icon = BitmapDescriptorFactory.fromResource(R.drawable.blue_marker)
            addMarker(
                MarkerOptions()
                    .position(origin)
                    .icon(icon)
                    .title("Av. Paulista")
                    .snippet("São Paulo")
            )
            val cameraPosition = CameraPosition.builder()
                .target(origin)
                .zoom(17.0f)
                .bearing(90f)
                .tilt(45f)
                .build()

            animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

        }
    }
}
