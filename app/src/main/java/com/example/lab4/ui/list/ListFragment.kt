package com.example.lab4.ui.list

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
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
        setUpSearchBar()
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

    private fun setUpSearchBar() {
        binding?.etSearch?.apply {
            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) =
                    Unit

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    listNamesAdapter.filter.filter(s)
                }

                override fun afterTextChanged(s: Editable?) = Unit
            })

            setOnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    actionId == EditorInfo.IME_ACTION_DONE ||
                    event?.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER
                ) {
                    v.clearFocus()
                    val imm =
                        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken, 0)
                    true
                } else {
                    false
                }
            }
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
                listNamesAdapter.sumbitList(users)
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