package com.example.lab4.ui.list.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.example.lab4.data.repository.bbdd.user.UserBD
import com.example.lab4.databinding.ItemNamesBinding

class UserViewHolder(val binding: ItemNamesBinding) : RecyclerView.ViewHolder(binding.root) {
    fun onBind(userBD: UserBD, position: Int) {
        binding.tvUsername.text = userBD.name
    }
}