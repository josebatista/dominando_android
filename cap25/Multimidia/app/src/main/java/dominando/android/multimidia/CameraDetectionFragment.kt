package dominando.android.multimidia

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import kotlinx.android.synthetic.main.fragment_camera_detection.*
import java.io.IOException

class CameraDetectionFragment : MultimidiaFragment() {

    private val detector: BarcodeDetector by lazy {
        BarcodeDetector.Builder(activity).setBarcodeFormats(Barcode.ALL_FORMATS).build()
    }

    private val cameraSource: CameraSource by lazy {
        CameraSource.Builder(activity, detector)
            .setFacing(CameraSource.CAMERA_FACING_BACK)
            .setAutoFocusEnabled(true)
            .setRequestedPreviewSize(800, 512)
            .build()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_camera_detection, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initProcessor()
        surfaceView.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceChanged(
                holder: SurfaceHolder?,
                format: Int,
                width: Int,
                heigth: Int
            ) {
            }

            override fun surfaceDestroyed(holder: SurfaceHolder?) {
                cameraSource.stop()
            }

            override fun surfaceCreated(holder: SurfaceHolder?) {
                startDetection()
            }
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.none { it == PackageManager.PERMISSION_DENIED }) {
            startDetection()
        }
    }

    private fun initProcessor() {
        detector.setProcessor(object : Detector.Processor<Barcode> {
            override fun release() {}

            override fun receiveDetections(detections: Detector.Detections<Barcode>) {
                val barCodes = detections.detectedItems
                if (barCodes.size() != 0) {
                    txtBarCode.post {
                        txtBarCode.text = barCodes.valueAt(0).displayValue
                    }
                }
            }
        })
    }

    private fun startDetection() {
        if (hasPermission()) {
            try {
                cameraSource.start(surfaceView.holder)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } else {
            requestPermissions()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraSource.release()
        detector.release()
    }
}