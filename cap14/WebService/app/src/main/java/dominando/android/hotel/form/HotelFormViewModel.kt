package dominando.android.hotel.form

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dominando.android.hotel.model.Hotel
import dominando.android.hotel.repository.HotelRepository

class HotelFormViewModel(private val repository: HotelRepository): ViewModel() {

    private val validator by lazy { HotelValidator() }

    val photoUrl = MutableLiveData<String>()

    fun loadHotel(id: Long): LiveData<Hotel> = repository.hotelById(id)

    fun saveHotel(hotel: Hotel): Boolean {
        return validator.validate(hotel).also { validated ->
            if (validated) {
                repository.save(hotel)
            }
        }
    }

}