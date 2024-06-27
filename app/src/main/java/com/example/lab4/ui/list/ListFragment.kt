package com.example.lab4.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.lab4.R
import com.example.lab4.databinding.FragmentListBinding
import com.example.lab4.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListFragment : BaseFragment<FragmentListBinding>() {

    private val listViewModel: ListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun inflateBinding() {
        binding = FragmentListBinding.inflate(layoutInflater)
    }

    override fun createViewAfterInflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) {
        setUpListeners()
    }

    private fun setUpListeners() {
        binding?.ibAdd?.setOnClickListener {
            findNavController().navigate(ListFragmentDirections.actionListFragmentToFormFragment())
        }
    }

    override fun configureToolbarAndConfigScreenSections() {
        fragmentLayoutWithToolbar()
        showToolbar(title = "Lista", showBack = true)
    }

    override fun observeViewModel() {
        //TODO("Not yet implemented")
    }

    override fun viewCreatedAfterSetupObserverViewModel(view: View, savedInstanceState: Bundle?) {
        //TODO("Not yet implemented")
    }

}