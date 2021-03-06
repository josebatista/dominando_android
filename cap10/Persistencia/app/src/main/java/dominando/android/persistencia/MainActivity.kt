package dominando.android.persistencia

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import kotlinx.android.synthetic.main.activity_main.*
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.OnPermissionDenied
import permissions.dispatcher.RuntimePermissions
import java.io.*

@RuntimePermissions
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnRead.setOnClickListener {
            btnReadClick()
        }
        btnSave.setOnClickListener {
            btnSaveClick()
        }
        btnOpenPref.setOnClickListener {
            startActivity(Intent(this, ConfigActivity::class.java))
        }
        btnReadPref.setOnClickListener {
            readPrefs()
        }
    }

    private fun btnReadClick() {
        when (rgType.checkedRadioButtonId) {
            R.id.rbInternal -> loadFromInternal()
            R.id.rbExternalPriv -> loadFromExternalWithPermissionCheck(true)
            R.id.rbExternalPublic -> loadFromExternalWithPermissionCheck(false)
        }
    }

    private fun btnSaveClick() {
        when (rgType.checkedRadioButtonId) {
            R.id.rbInternal -> saveToInternal()
            R.id.rbExternalPriv -> saveToExternalWithPermissionCheck(true)
            R.id.rbExternalPublic -> saveToExternalWithPermissionCheck(false)
        }
    }

    private fun readPrefs() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val city = prefs.getString(getString(R.string.pref_city), getString(R.string.pref_city_default))
        val socialNetwork = prefs.getString(
            getString(R.string.pref_social_network),
            getString(R.string.pref_social_network_default)
        )
        val messages = prefs.getBoolean(getString(R.string.pref_messages), false)

        val msg = String.format(
            "%s = %s\n%s = %s\n%s = %s",
            getString(R.string.title_city), city,
            getString(R.string.title_social_network), socialNetwork,
            getString(R.string.title_messages), messages.toString()
        )
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun save(fos: FileOutputStream) {
        val lines = TextUtils.split(edtView.text.toString(), "\n")
        val writer = PrintWriter(fos)
        for (line in lines) {
            writer.println(line)
        }
        writer.flush()
        writer.close()
        fos.close()
    }

    private fun load(fis: FileInputStream) {
        val reader = BufferedReader(InputStreamReader(fis))
        val sb = StringBuilder()
        do {
            val line = reader.readLine() ?: break
            if (sb.isNotEmpty()) {
                sb.append('\n')
            }
            sb.append(line)
        } while (true)
        reader.close()
        fis.close()
        txtView.text = sb.toString()
    }

    private fun saveToInternal() {
        try {
            val fos = openFileOutput("arquivo.txt", Context.MODE_PRIVATE)
            save(fos)
        } catch (e: Exception) {
            Log.e("JBP", "Erro ao salvar o arquivo", e)
        }
    }

    private fun loadFromInternal() {
        try {
            val fis = openFileInput("arquivo.txt")
            load(fis)
        } catch (e: Exception) {
            Log.e("JBP", "erro ao carregar o arquivo", e)
        }
    }

    private fun getExternalDir(privateDir: Boolean) =
        if (privateDir) {
            // SDCard/Android/data/pacote.da.app/files
            getExternalFilesDir(null)
        } else {
            // SDCard/DCIM
            Environment.getExternalStorageDirectory()
        }

    @NeedsPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun saveToExternal(privateDir: Boolean) {
//        val hasPermission = checkStoragePermission(
//            android.Manifest.permission.WRITE_EXTERNAL_STORAGE, RC_STORAGE_PERMISSION
//        )
//        if (!hasPermission) {
//            return
//        }

        val state = Environment.getExternalStorageState()
        if (Environment.MEDIA_MOUNTED == state) {
            try {
                val myDir = getExternalDir(privateDir)
                if (myDir?.exists() == false) {
                    myDir.mkdir()
                }

                val txtFile = File(myDir, "arquivo.txt")
                if (!txtFile.exists()) {
                    txtFile.createNewFile()
                }
                val fos = FileOutputStream(txtFile)
                save(fos)
            } catch (e: IOException) {
                Log.d("JBP", "Erro ao salvar arquivo", e)
            }
        } else {
            Log.e("JBP", "Não é possível escrever no SD Card")
        }
    }

    @NeedsPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
    fun loadFromExternal(privateDir: Boolean) {
//        val hasPermission = checkStoragePermission(
//            android.Manifest.permission.READ_EXTERNAL_STORAGE, RC_STORAGE_PERMISSION
//        )
//        if (!hasPermission) {
//            return
//        }

        val state = Environment.getExternalStorageState()
        if (Environment.MEDIA_MOUNTED == state || Environment.MEDIA_MOUNTED_READ_ONLY == state) {
            val myDir = getExternalDir(privateDir)
            if (myDir?.exists() == true) {
                val txtFile = File(myDir, "arquivo.txt")
                if (txtFile.exists()) {
                    try {
                        txtFile.createNewFile()
                        val fis = FileInputStream(txtFile)
                        load(fis)
                    } catch (e: IOException) {
                        Log.d("JBP", "Erro ao carregar arquivo", e)
                    }
                }
            }
        } else {
            Log.e("JBP", "SD Card indisponível")
        }
    }

//    private fun checkStoragePermission(permisson: String, requestCode: Int): Boolean {
//        if (ActivityCompat.checkSelfPermission(this, permisson) != PackageManager.PERMISSION_GRANTED) {
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permisson)) {
//                Toast.makeText(this, R.string.message_permission_requested, Toast.LENGTH_LONG).show()
//            }
//            ActivityCompat.requestPermissions(this, arrayOf(permisson), requestCode)
//            return false
//        }
//        return true
//    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }

    @OnPermissionDenied(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun showDeniedForExternal() {
        Toast.makeText(this, R.string.message_permission_requested, Toast.LENGTH_SHORT).show()
    }

//    companion object {
//        const val RC_STORAGE_PERMISSION = 0
//    }

}
