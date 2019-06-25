package dominando.android.mapas

import android.annotation.SuppressLint
import android.app.Application
import android.os.Bundle
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class MapViewModel(app: Application) : AndroidViewModel(app), CoroutineScope {

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private var googleApiClient: GoogleApiClient? = null
    private val locationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(getContext())
    }

    private val connectionStatus = MutableLiveData<GoogleApiConnectionStatus>()
    private val currentLocationError = MutableLiveData<LocationError>()
    private val mapState = MutableLiveData<MapState>().apply {
        value = MapState()
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    fun getConnectionStatus(): LiveData<GoogleApiConnectionStatus> {
        return connectionStatus
    }

    fun getCurrentLocationError(): LiveData<LocationError> {
        return currentLocationError
    }

    fun getMapState(): LiveData<MapState> {
        return mapState
    }

    fun connectGoogleApiClient() {
        if (googleApiClient == null) {
            googleApiClient = GoogleApiClient.Builder(getContext())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(object : GoogleApiClient.ConnectionCallbacks {
                    override fun onConnected(p0: Bundle?) {
                        connectionStatus.value = GoogleApiConnectionStatus(true)
                    }

                    override fun onConnectionSuspended(p0: Int) {
                        connectionStatus.value = GoogleApiConnectionStatus(false)
                        googleApiClient?.connect()
                    }
                })
                .addOnConnectionFailedListener { connectionResult ->
                    connectionStatus.value = GoogleApiConnectionStatus(false, connectionResult)
                }
                .build()
        }
        googleApiClient?.connect()
    }

    fun disconnectGoogleApiClient() {
        connectionStatus.value = GoogleApiConnectionStatus(false)
        if (googleApiClient != null && googleApiClient?.isConnected == true) {
            googleApiClient?.disconnect()
        }
    }

    @SuppressLint("MissingPermission")
    private suspend fun loadLastLocation(): Boolean = suspendCoroutine { continuation ->
        locationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    val latLng = LatLng(location.latitude, location.longitude)
                    mapState.value = mapState.value?.copy(origin = latLng)
                    continuation.resume(true)
                } else {
                    continuation.resume(false)
                }
            }
            .addOnFailureListener {
                continuation.resume(false)
            }
            .addOnCanceledListener {
                continuation.resume(false)
            }
    }

    fun requestLocation() {
        launch {
            currentLocationError.value = try {
                val success = withContext(Dispatchers.Default) { loadLastLocation() }
                if (success) {
                    null
                } else {
                    LocationError.ErrorLocationUnavailable
                }
            } catch (e: Exception) {
                LocationError.ErrorLocationUnavailable
            }
        }
    }

    private fun getContext() = getApplication<Application>()

    // Data classes...
    data class MapState(
        val origin: LatLng? = null
    )

    data class GoogleApiConnectionStatus(
        val success: Boolean,
        val connectionResult: ConnectionResult? = null
    )

    sealed class LocationError {
        object ErrorLocationUnavailable : LocationError()
    }
}