package ru.skillbranch.skillarticles.ui

interface IArticleView {
    fun renderSearchResult(searchResult: List<Pair<Int, Int>>)
    fun renderSearchPosition(searchPosition: Int)
    fun clearSearchResult()
    fun showSearchBar()
    fun hideSearchBar()
}