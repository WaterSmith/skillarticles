package ru.skillbranch.skillarticles.viewmodels.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.skillbranch.skillarticles.viewmodels.ArticleViewModel

class ViewModelFactory(private val params: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ArticleViewModel::class.java)) {
            return ArticleViewModel(params) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}