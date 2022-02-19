package com.mahdi.d.o.taha.aym.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mahdi.d.o.taha.aym.R
import com.mahdi.d.o.taha.aym.models.User_model
import kotlinx.android.synthetic.main.user_item.view.*

class UserRecyclerAdapter(
    val context: Context,
    val list: ArrayList<User_model>,
    var click: onClick
) :
    RecyclerView.Adapter<UserRecyclerAdapter.ViewHolder>() {

    class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val username = item.tv_username
        val isActive = item.tv_isActive
        val joni = item.join
        val user_id = item.user_id
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context).inflate(R.layout.user_item, parent, false)
        return ViewHolder(inflater)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.username.text = list[position].username.substring(0, 6)
        holder.isActive.text = list[position].isActive
        holder.user_id.text = list[position].id
        holder.joni.setOnClickListener {
            click.onItemClick(holder.adapterPosition)
        }
    }

    interface onClick {
        fun onItemClick(position: Int)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}
