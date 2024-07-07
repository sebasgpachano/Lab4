package com.example.lab4.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lab4.R
import com.example.lab4.data.repository.bbdd.user.UserBD
import com.example.lab4.databinding.FragmentListBinding
import com.example.lab4.ui.base.BaseFragment
import com.example.lab4.ui.extensions.toastLong
import com.example.lab4.ui.list.adapter.ListNamesAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ListFragment : BaseFragment<FragmentListBinding>(),
    ListNamesAdapter.ListNamesAdapterListener {

    private val listViewModel: ListViewModel by viewModels()
    private val listNamesAdapter = ListNamesAdapter(this)

    override fun inflateBinding() {
        binding = FragmentListBinding.inflate(layoutInflater)
    }

    override fun createViewAfterInflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) {
        setUpListeners()
        configRecyclerView()
    }

    private fun configRecyclerView() {
        binding?.rvList?.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = listNamesAdapter
        }
    }

    private fun setUpListeners() {
        binding?.ibAdd?.setOnClickListener {
            findNavController().navigate(ListFragmentDirections.actionListFragmentToFormFragment())
        }
    }

    override fun configureToolbarAndConfigScreenSections() {
        fragmentLayoutWithToolbar()
        showToolbar(title = getString(R.string.list_fragment_title), showBack = true)
    }

    override fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            listViewModel.loadingFlow.collect {
                showLoading(it)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            listViewModel.listUsersStateFlow.collect { users ->
                updateUsers(users)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            listViewModel.errorFlow.collect { error ->
                requireContext().toastLong(error.message)
            }
        }
    }

    private fun updateUsers(userList: List<UserBD>) {
        listNamesAdapter.submitList(userList) {
            binding?.rvList?.scrollToPosition(0)
        }
    }

    override fun viewCreatedAfterSetupObserverViewModel(view: View, savedInstanceState: Bundle?) {
        listViewModel.getUsers()
    }

    override fun onItemClick(id: Int) {
        val action = ListFragmentDirections.actionListFragmentToDetailFragment(id)
        findNavController().navigate(action)
    }

}