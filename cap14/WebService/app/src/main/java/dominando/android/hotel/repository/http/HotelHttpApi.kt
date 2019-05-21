package dominando.android.hotel.repository.http

import dominando.android.hotel.model.Hotel
import retrofit2.Call
import retrofit2.http.*

interface HotelHttpApi {

    @GET("$WEB_SERVICE/{user}")
    fun listHotels(@Path("user") user: String): Call<List<Hotel>>

    @GET("$WEB_SERVICE/{user}/{hotelId}")
    fun hotelById(
        @Path("user") user: String,
        @Path("hotelId") id: Long
    ): Call<Hotel>

    @POST("$WEB_SERVICE/{user}")
    fun insert(
        @Path("user") user: String,
        @Body hotel: Hotel
    ): Call<IdResult>

    @PUT("$WEB_SERVICE/{user}/{hotelId}")
    fun update(
        @Path("user") user: String,
        @Path("hotelId") id: Long,
        @Body hotel: Hotel
    ): Call<IdResult>

    @DELETE("$WEB_SERVICE/{user}/{hotelId}")
    fun delete(
        @Path("user") user: String,
        @Path("hotelId") id: Long
    ): Call<IdResult>

    companion object {
        const val WEB_SERVICE = "webservice.php"
    }

}