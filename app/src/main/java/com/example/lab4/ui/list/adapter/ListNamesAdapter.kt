package com.example.lab4.ui.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.lab4.data.repository.bbdd.user.UserBD
import com.example.lab4.databinding.ItemNamesBinding
import com.example.lab4.ui.list.adapter.viewholder.UserViewHolder

class ListNamesAdapter(private val userList: List<UserBD>) :
    ListAdapter<UserBD, UserViewHolder>(UserDiffCallback()) {

    interface ListNamesAdapterListener {
        fun onItemClick(id: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemNamesBinding.inflate(layoutInflater, parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.onBind(getItem(position), position)
    }

    override fun getItemCount() = userList.size


}