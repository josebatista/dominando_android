package dominando.android.hotel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.nhaarman.mockitokotlin2.*
import dominando.android.hotel.form.HotelFormViewModel
import dominando.android.hotel.model.Hotel
import dominando.android.hotel.repository.HotelRepository
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*

class HotelFormViewModelTests {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()
    private lateinit var viewModel: HotelFormViewModel
    private val mockedRepo = mock<HotelRepository>()
    private val anHotelID = Random().nextLong()
    private val anHotel = Hotel(
        id = anHotelID,
        name = "Ritz Recife",
        address = "Praia da Boa Viagem, SN",
        rating = 5.0f
    )

    @Before
    fun before_each_test() {
        viewModel = HotelFormViewModel(mockedRepo)
    }

    @Test
    fun give_an_existing_ID_should_load_the_hotel() {
        //GIVEN
        val liveData = MutableLiveData<Hotel>().apply {
            value = anHotel
        }

        //WHEN
        whenever(mockedRepo.hotelById(any())).thenReturn(liveData)

        //THEN
        viewModel.loadHotel(anHotelID).observeForever {
            assertThat(it).isEqualTo(anHotel)
        }
    }

    @Test
    fun given_valid_hotel_info_should_save_a_hotel() {
        //GIVEN
        val info = Hotel(
            name = "Ritz Recife",
            address = "Praia da Boa Viagem, Recife/PE",
            rating = 4.8f
        )

        //WHEN
        val saved = viewModel.saveHotel(info)
        whenever(mockedRepo.save(any())).thenAnswer { Unit }

        //THEN
        assertThat(saved).isTrue()
        verify(mockedRepo, times(1)).save(any())
        verifyNoMoreInteractions(mockedRepo)
    }

    @Test
    fun given_invalid_hotel_info_should_fail_at_save_the_hotel() {
        //GIVEN
        val invalidInfo = Hotel(
            name = "Y",
            address = "ZZZ",
            rating = 4.8f
        )

        //WHEN
        val saved = viewModel.saveHotel(invalidInfo)

        //THEN
        assertThat(saved).isFalse()
        verifyZeroInteractions(mockedRepo)
    }

}