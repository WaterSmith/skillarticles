package ru.skillbranch.skillarticles.extensions

fun String.indexesOf(substr:String, ignoreCase:Boolean = true) : List<Int> {
    var startIndex = this.indexOf(substr,0, ignoreCase)
    val result = mutableListOf<Int>()
    while (startIndex >= 0) {
        result.add(startIndex)
        startIndex = this.indexOf(substr,++startIndex, ignoreCase)
    }
    return result
}