package com.mahdi.d.o.taha.aym.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mahdi.d.o.taha.aym.R
import com.mahdi.d.o.taha.aym.models.Group_model
import kotlinx.android.synthetic.main.group_item.view.*

class GroupRecyclerAdapter(
    val context: Context,
    val list: ArrayList<Group_model>,
    var click: onClick
) :
    RecyclerView.Adapter<GroupRecyclerAdapter.ViewHolder>() {

    class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val name = item.group_username
        val join = item.join
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context).inflate(R.layout.group_item, parent, false)
        return ViewHolder(inflater)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = list[position].name
        holder.join.setOnClickListener {
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
