package ru.skillbranch.skillarticles.viewmodels

import androidx.lifecycle.LiveData
import ru.skillbranch.skillarticles.data.ArticleData
import ru.skillbranch.skillarticles.data.ArticlePersonalInfo
import ru.skillbranch.skillarticles.data.repositories.ArticleRepository
import ru.skillbranch.skillarticles.extensions.data.toAppSettings
import ru.skillbranch.skillarticles.extensions.data.toArticlePersonalInfo
import ru.skillbranch.skillarticles.extensions.format

class ArticleViewModel (private val articleId:String) : BaseViewModel<ArticleState>(
    ArticleState()
), IArticleViewModel {
    private val repository = ArticleRepository

    init {
        subscribeOnDataSource(getArticleData()){article, state ->
            article ?: return@subscribeOnDataSource null
            state.copy(
                shareLink = article.shareLink,
                title = article.title,
                category = article.category,
                categoryIcon = article.categoryIcon,
                date = article.date.format(),
                author = article.author
            )
        }

        subscribeOnDataSource(getArticleContent()){content, state ->
            content ?: return@subscribeOnDataSource null
            state.copy(
                isLoadingContent = false,
                content = content
            )
        }

        subscribeOnDataSource(getArticlePersonalInfo()){info, state ->
            info ?: return@subscribeOnDataSource null
            state.copy(
                isBookmark = info.isBookmark,
                isLike = info.isLike)
        }

        subscribeOnDataSource(repository.getAppSettings()){settings, state ->
            //settings ?: return@subscribeOnDataSource null
            state.copy(
                isBigText = settings.isBigText,
                isDarkMode = settings.isDarkMode
            )

        }
    }

    override fun getArticleContent():LiveData<List<Any>?> = repository.loadArticleContent(articleId)

    override fun getArticleData(): LiveData<ArticleData?> = repository.getArticle(articleId)

    override fun getArticlePersonalInfo():LiveData<ArticlePersonalInfo?> = repository.loadArticlePersonalInfo(articleId)

    override fun handleLike(){
        val toggleLike = {
            val info = currentState.toArticlePersonalInfo()
            repository.updateArticlePersonalInfo(info.copy(isLike = !info.isLike))
        }

        toggleLike()

        val msg = if (currentState.isLike) Notify.TextMessage("Mark is liked")
        else {
            Notify.ActionMessage(
                "Don`t like it anymore",
                "No, still like it",
                toggleLike
            )
        }

        notify(msg)
    }

    override fun handleBookmark(){
        val textMessage = if (currentState.isBookmark) "Remove from bookmarks" else "Add to bookmarks"
        val info = currentState.toArticlePersonalInfo()
        repository.updateArticlePersonalInfo(info.copy(isBookmark = !info.isBookmark))
        notify(Notify.TextMessage(textMessage))
    }

    override fun handleShare(){
        val msg = "Share is not implemented"
        notify(Notify.ErrorMessage(msg, "OK", null))
    }

    override fun handleToggleMenu() = updateState {it.copy(isShowMenu = !it.isShowMenu)}

    override fun handleSearchMode(isSearch: Boolean) {
        updateState {
            it.copy(isSearch = isSearch)
        }
    }

    override fun handleSearch(query: String?) {
        updateState {
            it.copy(searchQuery = query)
        }
    }

    override fun handleUpText(){
        val setting = currentState.toAppSettings()
        repository.updateSettings(
            setting.copy(
                isBigText = true
            )
        )
    }

    override fun handleDownText(){
        val setting = currentState.toAppSettings()
        repository.updateSettings(
            setting.copy(
                isBigText = false
            )
        )
    }

    override fun handleNightMode(){
        val setting = currentState.toAppSettings()
        repository.updateSettings(setting.copy(
            isDarkMode = !setting.isDarkMode
        ))
    }
}

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