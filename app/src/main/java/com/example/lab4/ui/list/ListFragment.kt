package com.example.lab4.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lab4.R
import com.example.lab4.data.model.user.UserModel
import com.example.lab4.databinding.FragmentListBinding
import com.example.lab4.ui.base.BaseFragment
import com.example.lab4.ui.dialogfragment.ConfirmDeleteDialogFragment
import com.example.lab4.ui.extensions.toastLong
import com.example.lab4.ui.list.adapter.ListNamesAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ListFragment : BaseFragment<FragmentListBinding>(),
    ListNamesAdapter.ListNamesAdapterListener, ConfirmDeleteDialogFragment.ConfirmDeleteListener {

    private val listViewModel: ListViewModel by viewModels()
    private val listNamesAdapter = ListNamesAdapter(this)
    private var deletePosition: Int? = null

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

        val swipeHandler = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                deletePosition = position
                showConfirmDeleteDialog(position)
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(binding?.rvList)
    }

    private fun setUpListeners() {
        binding?.ibAdd?.setOnClickListener {
            findNavController().navigate(ListFragmentDirections.actionListFragmentToFormFragment(-1L))
        }
    }

    override fun configureToolbarAndConfigScreenSections() {
        fragmentLayoutWithToolbar()
        showToolbar(title = getString(R.string.list_fragment_title), showBack = false)
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

        viewLifecycleOwner.lifecycleScope.launch {
            listViewModel.successFlow.collect {
                requireContext().toastLong(getString(R.string.user_deleted))
            }
        }
    }

    private fun updateUsers(userList: List<UserModel>) {
        listNamesAdapter.submitList(userList) {
            binding?.rvList?.scrollToPosition(0)
        }
    }

    override fun viewCreatedAfterSetupObserverViewModel(view: View, savedInstanceState: Bundle?) {
        listViewModel.getUsers()
    }

    override fun onItemClick(id: Int) {
        val action = ListFragmentDirections.actionListFragmentToFormFragment(id.toLong())
        findNavController().navigate(action)
    }

    override fun onConfirmDelete(position: Int) {
        deletePosition?.let {
            val user = listNamesAdapter.currentList[it]
            listViewModel.deleteUser(user)
        }
    }

    override fun onCancelDelete(position: Int) {
        listNamesAdapter.notifyItemChanged(position)
        deletePosition = null
    }
}