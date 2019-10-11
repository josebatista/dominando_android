package dominando.android.hotel

import androidx.room.Room
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import dominando.android.hotel.common.HotelActivity
import dominando.android.hotel.repository.HotelRepository
import dominando.android.hotel.repository.room.HotelDatabase
import dominando.android.hotel.repository.room.RoomRepository
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

@RunWith(AndroidJUnit4::class)
class HotelCurdTest {

    @get:Rule
    val activityRule = ActivityTestRule(HotelActivity::class.java)

    init {
        loadKoinModules(module {
            single(override = true) {
                val db = Room.inMemoryDatabaseBuilder(
                    InstrumentationRegistry.getInstrumentation().context,
                    HotelDatabase::class.java
                )
                    .allowMainThreadQueries()
                    .build()
                RoomRepository(db) as HotelRepository
            }
        })
    }

    @Test
    fun crudTest() {
        add()
    }

    private fun add() {
        onView(withId(R.id.fabAdd)).perform(click())
        onView(withId(R.id.edtName)).perform(typeText(NEW_HOTEL_NAME))
        onView(withId(R.id.edtName)).perform(pressImeActionButton())
        onView(withId(R.id.edtAddress)).perform(typeText(NEW_HOTEL_ADDRESS))
        onView(withId(R.id.edtAddress)).perform(pressImeActionButton())
        closeSoftKeyboard()
    }

    companion object {
        private const val NEW_HOTEL_NAME = "Hotel de Teste"
        private const val NEW_HOTEL_ADDRESS = "Rua tal"
    }

}