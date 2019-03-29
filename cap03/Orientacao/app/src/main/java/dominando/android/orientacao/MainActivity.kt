package dominando.android.orientacao

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        private val BUNDLE_KEY = "names_list"
    }

    var names = arrayListOf<String>()
    var adapter: ArrayAdapter<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState != null) {
            names = savedInstanceState.getStringArrayList(BUNDLE_KEY)
        }


        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, names)
        lstNames.adapter = adapter

        //exemplos de lista imutaveis (não podem ser alteradas), mutaveis (podem ser modificadas)
//        val imutableList = listOf("José", "Pereira", "Isaac", "Natali")
//        val secondName = imutableList[1] //Pereira
//        val mutableList = mutableListOf("Cicrano")
//        mutableList.add("José")
//        mutableList.add("Pereira")
//        mutableList.removeAt(0) // Cicrano removido

    }

    fun btnAddClick(view: View) {
        names.add(edtName.text.toString())
        edtName.text.clear()
        adapter?.notifyDataSetChanged()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putStringArrayList(BUNDLE_KEY, names)
    }
}
