package com.pashacabu.rentateamtestapp.view.listScreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.pashacabu.rentateamtestapp.R
import com.pashacabu.rentateamtestapp.data.network.DataItem

class UsersAdapter(private val selector: UserOnTouchInterface) :
    androidx.recyclerview.widget.ListAdapter<DataItem, UsersViewHolder>(UsersViewHolder.UsersDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        return UsersViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.user_list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        val user = getItem(position)
        holder.bindUserData(user)
        holder.itemView.setOnClickListener { selector.onSelectUser(getItem(position)) }
    }

}

class UsersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val name: TextView = itemView.findViewById(R.id.name)
    private val lastName: TextView = itemView.findViewById(R.id.lastName)

    fun bindUserData(data: DataItem) {
        name.text = data.firstName
        lastName.text = data.lastName

    }


    class UsersDiffCallback : DiffUtil.ItemCallback<DataItem>() {
        override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
            return oldItem.avatar == newItem.avatar && oldItem.email == newItem.email && oldItem.firstName == newItem.firstName && oldItem.lastName == newItem.lastName
        }

    }


}

class BottomScrolledListener(private val callback: () -> Unit) : RecyclerView.OnScrollListener() {
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if (dy > 0 && !recyclerView.canScrollVertically(1)) {
            callback()
        }
    }

}

interface UserOnTouchInterface {
    fun onSelectUser(user: DataItem)
}