package dominando.android.hotel.repository.http

import dominando.android.hotel.repository.HotelRepository

class HotelHttp(
    private val service: HotelHttpApi,
    private val repository: HotelRepository,
    private val currentUser: String
) {

    fun synchronizeWithServer() {
        if (currentUser.isBlank()) {
            throw SecurityException("Usuário não cadastrado")
        } else {
            sendPendingData()
            updateLocal()
        }
    }

    private fun sendPendingData() {
        val pendingHotels = repository.pending()
        pendingHotels.forEach { hotel ->
            when (hotel.status) {
                Status.INSERT -> {
                    val result = service.insert(currentUser, hotel).execute()
                    if (result.isSuccessful) {
                        hotel.serverId = result.body()?.id ?: 0
                        hotel.status = Status.OK
                        repository.update(hotel)
                    }
                }
                Status.DELETE -> {
                    val serverId = hotel.serverId ?: 0L
                    if (serverId != 0L) {
                        val result = service.delete(currentUser, serverId).execute()
                        if (result.isSuccessful) {
                            repository.remove(hotel)
                        }
                    } else {
                        repository.remove(hotel)
                    }
                }
                Status.UPDATE -> {
                    val result = if (hotel.serverId == 0L) {
                        service.insert(currentUser, hotel).execute()
                    } else {
                        service.update(currentUser, hotel.serverId ?: 0, hotel).execute()
                    }
                    if (result.isSuccessful) {
                        hotel.serverId = result.body()?.id ?: 0
                        hotel.status = Status.OK
                        repository.update(hotel)
                    }
                }
            }
        }
    }

    private fun updateLocal() {
        val response = service.listHotels(currentUser).execute()

        if (response.isSuccessful) {
            val hotelsInServer = response.body()?.map { hotel ->
                hotel.apply {
                    val id = hotel.id
                    hotel.serverId = id
                    hotel.id = 0
                }
            }
            hotelsInServer?.forEach { hotel ->
                hotel.status = Status.OK
                val localHotel = repository.hotelByServerId(hotel.serverId ?: 0)
                if (localHotel == null) {
                    repository.insert(hotel)
                } else {
                    hotel.id = localHotel.id
                    repository.update(hotel)
                }
            }
        }
    }

    companion object {
        const val BASE_URL = "http://192.168.64.2/hotel_service"
    }
}