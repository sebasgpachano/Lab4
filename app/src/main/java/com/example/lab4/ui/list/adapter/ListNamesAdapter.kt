package com.example.lab4.ui.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.lab4.data.model.user.UserModel
import com.example.lab4.databinding.ItemNamesBinding
import com.example.lab4.ui.list.adapter.viewholder.UserViewHolder

class ListNamesAdapter(private val listNamesAdapterListener: ListNamesAdapterListener) :
    ListAdapter<UserModel, UserViewHolder>(UserDiffCallback()) {

    interface ListNamesAdapterListener {
        fun onItemClick(id: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemNamesBinding.inflate(layoutInflater, parent, false)
        return UserViewHolder(binding, listNamesAdapterListener)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.onBind(getItem(position), position)
    }
}