package dominando.android.fragments

class HotelListPresenter(
    private val view: HotelListView,
    private val repository: HotelRepository
) {
    fun searchHotels(term: String) {
        repository.search(term) { hotels ->
            view.showHotels(hotels)
        }
    }

    fun showHotelDetails(hotel: Hotel) {
        view.showHotelDetail(hotel)
    }
}