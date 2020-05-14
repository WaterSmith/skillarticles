package ru.skillbranch.skillarticles.ui

import org.junit.Test

import ru.skillbranch.skillarticles.extensions.indexesOf

class ExampleInstrumentedTest {

    @Test
    fun indexesOfSubstring() {
        println("lorem ipsum sum".indexesOf("sum").toString())

    }
}