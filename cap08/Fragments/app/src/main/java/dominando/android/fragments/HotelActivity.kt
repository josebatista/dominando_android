package dominando.android.fragments

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class HotelActivity : AppCompatActivity(), HotelListFragment.OnHotelClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hotel)
    }

    override fun onHotelClick(hotel: Hotel) {
        if (isTablet()) {
            showDetailsFragment(hotel.id)
        } else if (isSmartphne()) {
            showDetailsActivity(hotel.id)
        }
    }

    private fun showDetailsActivity(hotelId: Long) {
        HotelDetailsActivity.open(this, hotelId)
    }

    private fun showDetailsFragment(hotelId: Long) {
        val fragment = HotelDetailsFragment.newInstance(hotelId)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.details, fragment, HotelDetailsFragment.TAG_DETAILS)
            .commit()
    }

//    private fun isTablet() = findViewById<View>(R.id.details) != null

    //forma mais elegante de verificar se é um tablet
    private fun isTablet() = resources.getBoolean(R.bool.tablet)

    private fun isSmartphne() = resources.getBoolean(R.bool.smartphone)
}
