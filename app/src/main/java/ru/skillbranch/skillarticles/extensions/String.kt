package ru.skillbranch.skillarticles.extensions

fun String?.indexesOf(substr:String, ignoreCase:Boolean = true) : List<Int> {
    val result = mutableListOf<Int>()

    if (this.isNullOrEmpty() || substr.isEmpty()) return result

    var startIndex = this.indexOf(substr,0, ignoreCase)
    while (startIndex >= 0) {
        result.add(startIndex)
        startIndex = this.indexOf(substr,++startIndex, ignoreCase)
    }

    return result
}