package dominando.android.http

import android.content.Context
import android.net.ConnectivityManager
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.Charset

object BookHttp {

    private const val BOOK_JSON_URL =
        "https://raw.githubusercontent.com/nglauber/dominando_android3/master/livros_novatec.json"

    @Throws(IOException::class)
    private fun connect(urlAddress: String): HttpURLConnection {
        val second = 1000
        val url = URL(urlAddress)
        val connetion = (url.openConnection() as HttpURLConnection).apply {
            readTimeout = 10 * second
            connectTimeout = 15 * second
            requestMethod = "GET"
            doInput = true
            doOutput = false
        }
        connetion.connect()
        return connetion
    }

    fun hasConnection(ctx: Context): Boolean {
        val cm = ctx.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val info = cm.activeNetworkInfo
        return info != null && info.isConnected
    }

    fun loadBooks(): List<Book>? {
        try {
            val connection = connect(BOOK_JSON_URL)
            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val inputStream = connection.inputStream
                val json = JSONObject(streamToString(inputStream))
                return readBooksFromJson(json)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    @Throws(JSONException::class)
    fun readBooksFromJson(json: JSONObject): List<Book> {
        val bookList = mutableListOf<Book>()
        var currentCategory: String
        val jsonNovatec = json.getJSONArray("novatec")
        for (i in 0 until jsonNovatec.length()) {
            val jsonCategory = jsonNovatec.getJSONObject(i)
            currentCategory = jsonCategory.getString("categoria")
            val jsonBooks = jsonCategory.getJSONArray("livros")
            for (j in 0 until jsonBooks.length()) {
                val jsonBook = jsonBooks.getJSONObject(j)
                val book = Book(
                    jsonBook.getString("titulo"),
                    currentCategory,
                    jsonBook.getString("autor"),
                    jsonBook.getInt("ano"),
                    jsonBook.getInt("paginas"),
                    jsonBook.getString("capa")
                )
                bookList.add(book)
            }
        }
        return bookList
    }

    @Throws(IOException::class)
    private fun streamToString(inputStream: InputStream): String {
        val buffer = ByteArray(1024)

        //O bigBuffer vai armazenar todos os bytes lidos
        val bigBuffer = ByteArrayOutputStream()

        //Precisamos saber quantos bytes foram lidos
        var bytesRead: Int

        //Vamos ler 1KB por vez...
        while (true) {
            bytesRead = inputStream.read(buffer)
            if (bytesRead == -1) {
                break
            }

            //Copiando a quantidade de bytes lidos do buffer para o bigBuffer
            bigBuffer.write(buffer, 0, bytesRead)
        }

        return String(bigBuffer.toByteArray(), Charset.forName("UTF-8"))
    }

}