package com.example.lab4.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.lab4.R
import com.example.lab4.databinding.FragmentDetailBinding
import com.example.lab4.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailFragment : BaseFragment<FragmentDetailBinding>() {

    private val detailViewModel: DetailViewModel by viewModels()
    private val args: DetailFragmentArgs by navArgs()

    override fun inflateBinding() {
        binding = FragmentDetailBinding.inflate(layoutInflater)
    }

    override fun createViewAfterInflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) {
        val id = args.id
        detailViewModel.getUserById(id)
    }

    override fun configureToolbarAndConfigScreenSections() {
        fragmentLayoutWithToolbar()
        showToolbar(title = getString(R.string.detail_fragment_title), showBack = true)

    }

    override fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            detailViewModel.userStateFlow.collect { user ->
                user?.let {
                    binding?.apply {
                        tvName.text = user.name
                        tvColor.text = user.favoriteColor
                        tvBirthDate.text = user.birthDate
                        tvCity.text = user.favoriteCity
                        tvNumber.text = user.favoriteNumber.toString()
                    }
                }
            }
        }
    }

    override fun viewCreatedAfterSetupObserverViewModel(view: View, savedInstanceState: Bundle?) =
        Unit

}