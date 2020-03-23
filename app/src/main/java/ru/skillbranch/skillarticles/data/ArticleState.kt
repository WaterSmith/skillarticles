package ru.skillbranch.skillarticles.data

data class ArticleState(
    val isAuth:Boolean = false, //User is autorised
    val isLoadingContent: Boolean = true, //Loading content in progress
    val isLoadingReviews: Boolean = true, //Review loading in progress
    val isLike: Boolean = false, //It is liked
    val isBookmark: Boolean = false, //It is in bookmark
    val isShowMenu: Boolean = false, //Menu is visible
    val isBigText:Boolean = false, //Used big font
    val isDarkMode:Boolean = false, //Night mode is on
    val isSearch: Boolean = false, //Search mode is on
    val searchQuery: String? = null, //Search query
    val searchResults: List<Pair<Int,Int>> = emptyList(), //Search result (start and end position)
    val searchPosition: Int = 0, //Current position in searchResults
    val shareLink: String? = null, //Publication link for sharing
    val title: String? = null, //Publication title
    val category: String? = null, //Publication category
    val categoryIcon: Any? = null, //Publication category icon
    val date: String? = null, //Date of publication
    val author: Any? = null, //Author of publication
    val poster: String? = null, //Cover of peblication
    val content: List<Any> = emptyList(),
    val reviews: List<Any> = emptyList()
    )