package com.example.lab4.ui.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.ListAdapter
import com.example.lab4.data.model.user.UserModel
import com.example.lab4.databinding.ItemNamesBinding
import com.example.lab4.ui.list.adapter.viewholder.UserViewHolder

class ListNamesAdapter(private val listNamesAdapterListener: ListNamesAdapterListener) :
    ListAdapter<UserModel, UserViewHolder>(UserDiffCallback()), Filterable {

    private var userListFull: List<UserModel> = listOf()

    interface ListNamesAdapterListener {
        fun onItemClick(id: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemNamesBinding.inflate(layoutInflater, parent, false)
        return UserViewHolder(binding, listNamesAdapterListener)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    fun sumbitList(list: List<UserModel>?) {
        userListFull = list ?: listOf()
        super.submitList(list)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString()?.trim() ?: ""

                val filteredList = if (charString.isEmpty()) {
                    userListFull
                } else {
                    userListFull.filter {
                        it.name.contains(charString, ignoreCase = true)
                    }
                }

                val filterResults = FilterResults()
                filterResults.values = filteredList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                val filteredList = results?.values as List<UserModel>? ?: listOf()
                submitFilteredList(filteredList)
            }
        }
    }

    private fun submitFilteredList(list: List<UserModel>) {
        super.submitList(list)
    }
}