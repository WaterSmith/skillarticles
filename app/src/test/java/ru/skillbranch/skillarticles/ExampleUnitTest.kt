package ru.skillbranch.skillarticles

import org.junit.Test
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val numbers = Array(5){
            255+it
        }
        numbers.forEach {
            println(it)
        }

        val today = GregorianCalendar.getInstance()
        println(today[GregorianCalendar.YEAR]-1)
    }
}
