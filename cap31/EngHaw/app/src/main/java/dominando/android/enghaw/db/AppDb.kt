package dominando.android.enghaw.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dominando.android.enghaw.model.Album

@Database(entities = [Album::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDb : RoomDatabase() {

    abstract fun albumDao(): AlbumDao

    companion object {
        private val DB_NAME = "engHwDb"
        private var INSTANCE: AppDb? = null

        fun getInstance(context: Context): AppDb? {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    AppDb::class.java,
                    DB_NAME
                )
                    .build()
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }

}