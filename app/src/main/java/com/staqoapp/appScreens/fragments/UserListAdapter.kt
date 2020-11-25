package com.staqoapp.appScreens.fragments

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.staqoapp.R
import com.staqoapp.databinding.RecyclerItemHorizontalUserBinding
import com.staqoapp.databinding.RecyclerItemVerticalUserBinding
import com.staqoapp.model.User
import com.staqoapp.utils.Constants.OtherConstants.HORIZONTAL
import com.staqoapp.utils.Constants.OtherConstants.VERTICAL

class UserListAdapter(private val type: Int, private val context: Context, private val userList: ArrayList<User>):
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(type) {
            VERTICAL -> {
                val binding = RecyclerItemVerticalUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                VerticalViewHolder(binding)
            }
            HORIZONTAL -> {
                val binding = RecyclerItemHorizontalUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                HorizontalViewHolder(binding)
            }
            else -> {
                val binding = RecyclerItemVerticalUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                VerticalViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is VerticalViewHolder -> {
                holder.bind(userList[holder.adapterPosition])
            }
            is HorizontalViewHolder -> {
                holder.bind(userList[holder.adapterPosition])
            }
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    inner class VerticalViewHolder(private val binding: RecyclerItemVerticalUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            binding.user = user
            setImage(user.avatar)
        }

        private fun setImage(url: String) {
            Glide.with(context).load(url).placeholder(getDrawable(context, R.drawable.user_placeholder)).into(binding.imageViewUser)
        }
    }

    inner class HorizontalViewHolder(private val binding: RecyclerItemHorizontalUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            binding.user = user
            setImage(user.avatar)
        }

        private fun setImage(url: String) {
            Glide.with(context)
                .load(url)
                .placeholder(getDrawable(context, R.drawable.user_placeholder)
            ).into(binding.imageViewUser)
        }
    }
}