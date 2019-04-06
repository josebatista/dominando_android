package dominando.android.adapter

import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    //0=VW;1=GM;2=Fiat;3=Ford
    private val vehicles = mutableListOf(
        Vehicle("Onix", 2018, 1, true, true),
        Vehicle("Uno", 2007, 2, true, false),
        Vehicle("Del Rey", 1988, 3, false, true),
        Vehicle("Gol", 2014, 0, true, true)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val listView = ListView(this)
        setContentView(listView)
        val adapter = VehicleAdapter(this, vehicles)
        listView.adapter = adapter
    }
}
