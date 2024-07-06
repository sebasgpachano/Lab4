package com.example.lab4.ui.list.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.lab4.data.repository.bbdd.user.UserBD

class UserDiffCallback : DiffUtil.ItemCallback<UserBD>() {
    override fun areItemsTheSame(oldItem: UserBD, newItem: UserBD): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: UserBD, newItem: UserBD): Boolean {
        return oldItem == newItem
    }
}