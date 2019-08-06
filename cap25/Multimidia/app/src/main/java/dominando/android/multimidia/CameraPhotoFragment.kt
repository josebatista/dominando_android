package dominando.android.multimidia

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.core.content.FileProvider
import kotlinx.android.synthetic.main.fragment_camera_photo.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class CameraPhotoFragment : MultimidiaFragment(), ViewTreeObserver.OnGlobalLayoutListener {

    private var photoFile: File? = null
    private var imageWidth: Int = 0
    private var imageHeight: Int = 0

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        retainInstance = true
        if (photoFile == null) {
            activity?.let {
                val lastPath = MediaUtils.getLastMediaPath(requireContext(), MediaUtils.MediaType.MEDIA_PHOTO)
                if (lastPath != null) {
                    photoFile = File(lastPath)
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layout = inflater.inflate(R.layout.fragment_camera_photo, container, false)
        layout.viewTreeObserver.addOnGlobalLayoutListener(this)
        return layout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnTakePhoto.setOnClickListener {
            openCamera()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == MediaUtils.REQUEST_CODE_PHOTO && resultCode == Activity.RESULT_OK) {
            loadImage()
        }
    }

    override fun onGlobalLayout() {
        view?.viewTreeObserver?.removeOnGlobalLayoutListener(this)
        imageWidth = imgPhoto.width
        imageHeight = imgPhoto.height
        loadImage()
    }

    private fun openCamera() {
        activity?.let {
            if (hasPermission()) {
                try {
                    val newPhotoFile = MediaUtils.newMedia(MediaUtils.MediaType.MEDIA_PHOTO)
                    photoFile = newPhotoFile
                    val photoUri = FileProvider.getUriForFile(it, MediaUtils.PROVIDER_AUTHORITY, newPhotoFile)
                    startActivityForResult(Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
                        putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                    }, MediaUtils.REQUEST_CODE_PHOTO)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                requestPermissions(RC_OPEN_CAMERA)
            }
        }
    }

    private fun loadImage() {
        val file = photoFile
        if (file?.exists() == true) {
            if (hasPermission()) {
                launch {
                    val bitmap = withContext(Dispatchers.IO) {
                        MediaUtils.loadImage(file, imageWidth, imageHeight)
                    }
                    imgPhoto.setImageBitmap(bitmap)
                    activity?.let {
                        MediaUtils.saveLastMediaPath(it, MediaUtils.MediaType.MEDIA_PHOTO, file.absolutePath)
                    }
                }
            } else {
                requestPermissions(RC_LOAD_PHOTO)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.none { it == PackageManager.PERMISSION_DENIED }) {
            when (requestCode) {
                RC_LOAD_PHOTO -> loadImage()
                RC_OPEN_CAMERA -> openCamera()
            }
        }
    }

    companion object {
        private const val RC_LOAD_PHOTO = 1
        private const val RC_OPEN_CAMERA = 2
    }
}