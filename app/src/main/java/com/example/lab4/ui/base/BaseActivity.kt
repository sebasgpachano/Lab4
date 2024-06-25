package com.example.lab4.ui.base

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.NavController
import androidx.viewbinding.ViewBinding
import com.example.lab4.R
import com.example.lab4.ui.dialogfragment.LoadingDialogFragment
import com.example.lab4.ui.dialogfragment.LoadingDialogFragment.Companion.LOADING_DIALOG_FRAGMENT_TAG
import com.example.lab4.ui.extensions.gone
import com.example.lab4.ui.extensions.visible
import javax.inject.Inject

abstract class BaseActivity<B : ViewBinding> : AppCompatActivity(), View.OnClickListener {

    lateinit var binding: B
    lateinit var navController: NavController

    private var isKeyboardVisible = false
    private var clToolbar: ConstraintLayout? = null
    private var tbToolbar: Toolbar? = null
    private var ibToolbarBack: ImageButton? = null
    private var tvToolbarTitle: TextView? = null

    private var loadingDialogFragment: LoadingDialogFragment = LoadingDialogFragment()

    @Inject
    lateinit var baseActivityControlShowLoading: BaseActivityControlShowLoading

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inflateBinding()
        setContentView(binding.root)
        findViewByIdToolbar()
        observeViewModel()
        createAfterInflateBindingSetupObserverViewModel(savedInstanceState)
        setListenersClickToolbarButtons()
    }

    override fun onResume() {
        super.onResume()
        configureToolbarAndConfigScreenSections()
    }

    private fun findViewByIdToolbar() {
        clToolbar = findViewById(R.id.clToolbar)
        tbToolbar = findViewById(R.id.tbToolbar)
        ibToolbarBack = findViewById(R.id.ibToolbarBack)
        tvToolbarTitle = findViewById(R.id.tvToolbarTitle)
    }

    fun showToolbar(showBack: Boolean = false, title: String = "") {
        hideAllElementToolbar()
        clToolbar?.visible()
        if (showBack) {
            ibToolbarBack?.visible()
        }
        if (title.isNotBlank()) {
            tvToolbarTitle?.visible()
            tvToolbarTitle?.text = title
        }
    }

    private fun setListenersClickToolbarButtons() {
        ibToolbarBack?.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.ibToolbarBack -> clickToolbarBack()
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 1) {
            finish()
        } else {
            super.onBackPressed()
        }
    }

    protected open fun clickToolbarBack() {
        onBackPressed()
    }

    private fun hideAllElementToolbar() {
        ibToolbarBack?.gone()
        tvToolbarTitle?.gone()
    }

    private fun showToolbarLayout() {
        clToolbar?.visible()
    }

    private fun hideToolbarLayout() {
        clToolbar?.gone()
    }

    fun hideToolbar() {
        clToolbar?.gone()
    }

    fun fragmentFullScreenLayoutWithoutToolbar() {
        hideToolbarLayout()
    }

    fun fragmentLayoutWithToolbar() {
        showToolbarLayout()
    }

    fun updateShowToolbarBack(showBack: Boolean) {
        if (showBack) {
            ibToolbarBack?.visible()
        } else {
            ibToolbarBack?.gone()
        }
    }

    fun updateShowToolbarTitle(title: String) {
        if (title.isNotBlank()) {
            tvToolbarTitle?.visible()
            tvToolbarTitle?.text = title
        } else {
            tvToolbarTitle?.gone()
        }
    }

    fun showLoading(show: Boolean) {
        if (show) {
            if (baseActivityControlShowLoading.canShowLoading(
                    supportFragmentManager,
                    LOADING_DIALOG_FRAGMENT_TAG
                )
            ) {
                loadingDialogFragment.show(supportFragmentManager, LOADING_DIALOG_FRAGMENT_TAG)
            }
        } else {
            if (baseActivityControlShowLoading.canHideLoading(
                    supportFragmentManager,
                    LOADING_DIALOG_FRAGMENT_TAG
                )
            ) {
                loadingDialogFragment.dismiss()
            }
        }
    }

    abstract fun inflateBinding()
    abstract fun observeViewModel()
    abstract fun createAfterInflateBindingSetupObserverViewModel(savedInstanceState: Bundle?)
    abstract fun configureToolbarAndConfigScreenSections()
}