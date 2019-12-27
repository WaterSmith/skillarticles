package ru.skillbranch.skillarticles.viewmodels

import androidx.lifecycle.LiveData
import kotlinx.android.synthetic.main.layout_submenu.*
import ru.skillbranch.skillarticles.data.ArticleData
import ru.skillbranch.skillarticles.data.ArticlePersonalInfo
import ru.skillbranch.skillarticles.data.repositories.ArticleRepository
import ru.skillbranch.skillarticles.extensions.data.toAppSettings
import ru.skillbranch.skillarticles.extensions.format

class ArticleViewModel (private val articleId:String) : BaseViewModel<ArticleState>(ArticleState()) {
    private val repository = ArticleRepository

    init {
        subscribeOnDataSource(getArticleDala()){article, state ->
            article ?: return@subscribeOnDataSource null
            state.copy(
                shareLink = article.shareLink,
                title = article.title,
                category = article.category,
                categoryIcon = article.categoryIcon,
                date = article.date.format()
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

    private fun getArticleContent():LiveData<List<Any>?> = repository.loadArticleContent(articleId)

    private fun getArticleDala():LiveData<ArticleData?> = repository.getArticle(articleId)

    private fun getArticlePersonalInfo():LiveData<ArticlePersonalInfo?> = repository.loadArticlePersonalInfo(articleId)

    fun handleLike() {

    }

    fun handleBookmark(){

    }

    fun handleShare(){

    }

    fun handleToggleMenu() = updateState {it.copy(isShowMenu = !it.isShowMenu)}

    fun handleUpText(){
        val setting = currentState.toAppSettings()
        repository.updateSettings(
            setting.copy(
                isBigText = true
            )
        )
    }

    fun handleDownText(){
        val setting = currentState.toAppSettings()
        repository.updateSettings(
            setting.copy(
                isBigText = false
            )
        )
    }

    fun handleNightMode(){
        val setting = currentState.toAppSettings()
        repository.updateSettings(setting.copy(
            isDarkMode = !setting.isDarkMode
        ))
    }
}