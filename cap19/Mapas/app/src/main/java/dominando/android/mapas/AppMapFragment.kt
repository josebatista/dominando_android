package dominando.android.mapas

import android.graphics.Color
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions

class AppMapFragment : SupportMapFragment() {

    private val viewModel: MapViewModel by lazy {
        ViewModelProviders.of(requireActivity()).get(MapViewModel::class.java)
    }

    private var googleMap: GoogleMap? = null

    override fun getMapAsync(callback: OnMapReadyCallback?) {
        super.getMapAsync {
            googleMap = it
            setupMap()
            callback?.onMapReady(googleMap)
        }
    }

    private fun setupMap() {
        googleMap?.run {
            mapType = GoogleMap.MAP_TYPE_NORMAL
            uiSettings.isMapToolbarEnabled = false
            uiSettings.isZoomControlsEnabled = true
        }
        viewModel.getMapState().observe(this, Observer { mapState ->
            if (mapState != null) {
                updateMap(mapState)
            }
        })
    }

    private fun updateMap(mapState: MapViewModel.MapState) {
        googleMap?.run {
            clear()
            val area = LatLngBounds.Builder()
            val origin = mapState.origin
            if (origin != null) {
                addMarker(
                    MarkerOptions()
                        .position(origin)
                        .title(getString(R.string.map_marker_origin))
                )
                area.include(origin)
            }

            val destination = mapState.destination
            if (destination != null) {
                addMarker(
                    MarkerOptions()
                        .position(destination)
                        .title(getString(R.string.map_marker_destination))
                )
                area.include(destination)
            }

            val route = mapState.route
            if (route != null && route.isNotEmpty()) {
                val polyLineOptions = PolylineOptions()
                    .addAll(route)
                    .width(5f)
                    .color(Color.RED)
                    .visible(true)
                addPolyline(polyLineOptions)
                route.forEach { area.include(it) }
            }

            if (origin != null) {
                if (destination != null) {
                    animateCamera(CameraUpdateFactory.newLatLngBounds(area.build(), 50))
                } else {
                    animateCamera(CameraUpdateFactory.newLatLngZoom(origin, 17f))
                }
            }

        }
    }

}