package dominando.android.fragments

interface HotelListView {
    fun showHotels(hotels: List<Hotel>)
    fun showHotelDetail(hotel: Hotel)
}