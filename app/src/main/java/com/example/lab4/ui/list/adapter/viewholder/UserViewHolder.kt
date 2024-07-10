package com.example.lab4.ui.list.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.example.lab4.data.model.user.UserModel
import com.example.lab4.data.repository.bbdd.user.UserBD
import com.example.lab4.databinding.ItemNamesBinding
import com.example.lab4.ui.list.adapter.ListNamesAdapter

class UserViewHolder(
    val binding: ItemNamesBinding,
    private val listNamesAdapterListener: ListNamesAdapter.ListNamesAdapterListener
) : RecyclerView.ViewHolder(binding.root) {
    fun onBind(user: UserModel, position: Int) {
        binding.tvUsername.text = user.name
        binding.root.setOnClickListener {
            listNamesAdapterListener.onItemClick(user.id)
        }
    }
}