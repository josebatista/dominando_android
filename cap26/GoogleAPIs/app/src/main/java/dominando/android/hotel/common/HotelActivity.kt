package dominando.android.hotel.common

import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import dominando.android.hotel.R
import dominando.android.hotel.auth.UserProfileFragment
import dominando.android.hotel.details.HotelDetailsActivity
import dominando.android.hotel.details.HotelDetailsFragment
import dominando.android.hotel.form.HotelFormFragment
import dominando.android.hotel.list.HotelListFragment
import dominando.android.hotel.list.HotelListViewModel
import dominando.android.hotel.model.Hotel
import kotlinx.android.synthetic.main.activity_hotel.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class HotelActivity : BaseActivity(),
    HotelListFragment.OnHotelClickListener,
    SearchView.OnQueryTextListener,
    MenuItem.OnActionExpandListener {

    private val viewModel: HotelListViewModel by viewModel()

    private var searchView: SearchView? = null

    private val listFragment: HotelListFragment by lazy {
        supportFragmentManager.findFragmentById(R.id.fragmentList) as HotelListFragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hotel)

        fabAdd.setOnClickListener {
            listFragment.hideDeleteMode()
            HotelFormFragment.newInstance().open(supportFragmentManager)
        }

        initAdMob()
    }

    override fun onResume() {
        super.onResume()
        adView.resume()
    }

    override fun onPause() {
        super.onPause()
        adView.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        adView.destroy()
    }

    private fun initAdMob() {
        MobileAds.initialize(this, getString(R.string.admob_adunit_id))
        val adRequest = AdRequest.Builder()
            .addTestDevice(AdRequest.DEVICE_ID_EMULATOR) //permite testar no emulador
            //.addTestDevice("TEST_DEVICE_ID") //use o adb devices para obter o id do aparelho de teste
            .build()
        adView.loadAd(adRequest)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.hotel, menu)
        val searchItem = menu?.findItem(R.id.action_search)
        searchItem?.setOnActionExpandListener(this)
        searchView = searchItem?.actionView as SearchView
        searchView?.queryHint = getString(R.string.hint_search)
        searchView?.setOnQueryTextListener(this)

        if (viewModel.getSearchTerm()?.value?.isNotEmpty() == true) {
            Handler().post {
                val query = viewModel.getSearchTerm()?.value
                searchItem.expandActionView()
                searchView?.setQuery(query, true)
                searchView?.clearFocus()
            }
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_info -> {
                AboutDialogFragment().show(supportFragmentManager, "sobre")
            }
            R.id.action_user_profile -> {
                UserProfileFragment().open(supportFragmentManager)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onQueryTextSubmit(query: String?): Boolean = true

    override fun onQueryTextChange(newText: String?): Boolean {
        listFragment.search(newText ?: "")
        return true
    }

    override fun onMenuItemActionExpand(item: MenuItem?): Boolean = true //para expandir a view

    override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
        listFragment.search()
        return true
    }

    override fun onHotelClick(hotel: Hotel) {
        if (isTablet()) {
            viewModel.hotelIdSelected = hotel.id
            showDetailsFragment(hotel.id)
        } else if (isSmartphone()) {
            showDetailsActivity(hotel.id)
        }
    }

    private fun showDetailsActivity(hotelId: Long) {
        HotelDetailsActivity.open(this, hotelId)
    }

    private fun showDetailsFragment(hotelId: Long) {
        /*o menu será recriado quando o fragment de detalhe for exibido,
        * então o listenet deve ser removido para não ser notificado com o texto vazio*/

        searchView?.setOnQueryTextListener(null)

        val fragment = HotelDetailsFragment.newInstance(hotelId)

        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(R.anim.enter_hotel_details, R.anim.exit_hotel_details)
            .replace(
                R.id.details, fragment,
                HotelDetailsFragment.TAG_DETAILS
            )
            .commit()
    }

//    private fun isTablet() = findViewById<View>(R.id.details) != null

    //forma mais elegante de verificar se é um tablet
    private fun isTablet() = resources.getBoolean(R.bool.tablet)

    private fun isSmartphone() = resources.getBoolean(R.bool.smartphone)

}
