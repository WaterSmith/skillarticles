package ru.skillbranch.skillarticles.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_root.*
import kotlinx.android.synthetic.main.layout_bottombar.*
import kotlinx.android.synthetic.main.layout_submenu.*
import ru.skillbranch.skillarticles.R
import ru.skillbranch.skillarticles.extensions.dpToIntPx
import ru.skillbranch.skillarticles.data.ArticleState
import ru.skillbranch.skillarticles.viewmodels.ArticleViewModel
import ru.skillbranch.skillarticles.viewmodels.Notify
import ru.skillbranch.skillarticles.viewmodels.ViewModelFactory

class RootActivity : AppCompatActivity() {

    private lateinit var viewModel: ArticleViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_root)
        setupToolbar()
        setupBottombar()
        setupSubmenu()
        val vmFactory = ViewModelFactory("0")
        viewModel = ViewModelProviders.of(this, vmFactory).get(ArticleViewModel::class.java)
        viewModel.observeState(this){
            renderUI(it)
        }

        viewModel.observeNotifications(this){
            renderNotification(it)
        }
    }

    private fun renderNotification(notify: Notify) {
        with(Snackbar.make(coordinator_container, notify.message, Snackbar.LENGTH_LONG)
            .setAnchorView(bottombar)
        ){
            when(notify){
                is Notify.TextMessage -> {}
                is Notify.ActionMessage -> {
                        setActionTextColor(getColor(R.color.color_accent_dark))
                        setAction(notify.actionLabel){
                        notify.actionHandler?.invoke()
                    }
                }
                is Notify.ErrorMessage -> {
                        setBackgroundTint(getColor(R.color.design_default_color_error))
                        setTextColor(getColor(android.R.color.white))
                        setActionTextColor(getColor(android.R.color.white))
                        setAction(notify.errLabel){
                        notify.errHandler?.invoke()
                    }
                }
            }
            show()
        }
    }

    private fun renderUI(state: ArticleState) {
        btn_settings.isChecked = state.isShowMenu
        if (state.isShowMenu) submenu.open() else submenu.close()

        btn_like.isChecked = state.isLike
        btn_bookmark.isChecked = state.isBookmark
        switch_mode.isChecked = state.isDarkMode
        delegate.localNightMode = if (state.isDarkMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO

        tv_text_content.textSize = if (state.isBigText) 18f else 14f
        btn_text_up.isChecked = state.isBigText
        btn_text_down.isChecked = !state.isBigText

        tv_text_content.text = if (state.isLoadingContent) "Loading" else state.content.first() as String

        toolbar.title = state.title ?: "loading"
        toolbar.subtitle = state.category ?: "loading"

        if (state.categoryIcon!=null) toolbar.logo = getDrawable(state.categoryIcon as Int)
    }

    private fun setupBottombar() {
        btn_like.setOnClickListener {
            viewModel.handleLike()
        }
        btn_bookmark.setOnClickListener {
            viewModel.handleBookmark()
        }
        btn_share.setOnClickListener {
            viewModel.handleShare()
        }
        btn_settings.setOnClickListener {
            viewModel.handleToggleMenu()
        }
    }

    private fun setupSubmenu(){
        btn_text_up.setOnClickListener {
            viewModel.handleUpText()
        }

        btn_text_down.setOnClickListener {
            viewModel.handleDownText()
        }

        switch_mode.setOnClickListener {
            viewModel.handleNightMode()
        }

    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val logo = if (toolbar.childCount>2) toolbar.getChildAt(2) as ImageView else null
        logo?.scaleType = ImageView.ScaleType.CENTER_CROP
        val lp = logo?.layoutParams as? Toolbar.LayoutParams

        lp?.let{
            it.width = this.dpToIntPx(40)
            it.height = this.dpToIntPx(40)
            it.marginEnd = this.dpToIntPx(16)

            logo.layoutParams = it
        }
    }


}
