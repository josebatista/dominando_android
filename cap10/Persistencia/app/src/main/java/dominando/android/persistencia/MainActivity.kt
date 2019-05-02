package dominando.android.persistencia

import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*

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
    }

    private fun btnReadClick() {
        when (rgType.checkedRadioButtonId) {
            R.id.rbInternal -> loadFromInternal()
            R.id.rbExternalPriv -> loadFromExternal(true)
            R.id.rbExternalPublic -> loadFromExternal(false)
        }
    }

    private fun btnSaveClick() {
        when (rgType.checkedRadioButtonId) {
            R.id.rbInternal -> saveToInternal()
            R.id.rbExternalPriv -> saveToExternal(true)
            R.id.rbExternalPublic -> saveToExternal(false)
        }
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
        textView.text = sb.toString()
    }

    private fun saveToInternal() {}
    private fun loadFromInternal() {}
    private fun saveToExternal(privateDir: Boolean) {}
    private fun loadFromExternal(privateDir: Boolean) {}
}
