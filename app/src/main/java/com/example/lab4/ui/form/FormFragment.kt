package com.example.lab4.ui.form

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.lab4.R
import com.example.lab4.databinding.FragmentFormBinding
import com.example.lab4.ui.base.BaseFragment
import com.example.lab4.ui.extensions.toastLong
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FormFragment : BaseFragment<FragmentFormBinding>(), View.OnClickListener {

    private val formViewModel: FormViewModel by viewModels()

    override fun inflateBinding() {
        binding = FragmentFormBinding.inflate(layoutInflater)
    }

    override fun createViewAfterInflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) {
        setUpListeners()
    }

    private fun setUpListeners() {
        binding?.btLocation?.setOnClickListener(this)
        binding?.btSave?.setOnClickListener(this)
    }

    override fun configureToolbarAndConfigScreenSections() {
        fragmentLayoutWithToolbar()
        showToolbar(title = getString(R.string.register), showBack = true)
    }

    override fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            formViewModel.loadingFlow.collect {
                showLoading(it)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            formViewModel.errorFlow.collect { error ->
                requireContext().toastLong(error.message)
            }
        }
    }

    override fun viewCreatedAfterSetupObserverViewModel(view: View, savedInstanceState: Bundle?) =
        Unit

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btLocation -> {
                //TODO
            }

            R.id.btSave -> {
                //TODO
            }
        }
    }


}