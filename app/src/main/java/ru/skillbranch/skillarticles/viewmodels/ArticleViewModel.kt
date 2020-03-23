package ru.skillbranch.skillarticles.viewmodels

import androidx.lifecycle.LiveData
import ru.skillbranch.skillarticles.data.ArticleData
import ru.skillbranch.skillarticles.data.ArticlePersonalInfo
import ru.skillbranch.skillarticles.data.ArticleState
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
            settings ?: return@subscribeOnDataSource null
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
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun handleSearch(query: String?) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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