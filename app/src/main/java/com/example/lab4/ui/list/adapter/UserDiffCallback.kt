package com.example.lab4.ui.list.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.lab4.data.model.user.UserModel
import com.example.lab4.data.repository.bbdd.user.UserBD

class UserDiffCallback : DiffUtil.ItemCallback<UserModel>() {
    override fun areItemsTheSame(oldItem: UserModel, newItem: UserModel): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: UserModel, newItem: UserModel): Boolean {
        return oldItem == newItem
    }
}