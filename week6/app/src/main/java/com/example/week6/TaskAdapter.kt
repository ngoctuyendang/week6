package com.example.week6

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.week6.ROOM.Task
import kotlinx.android.synthetic.main.activity_add_task.view.*
import kotlinx.android.synthetic.main.item_task.view.*
import java.util.*

class TaskAdapter(var items: ArrayList<Task>, val context: Context) : RecyclerView.Adapter<TaskViewHolder>() {

    lateinit var mListener: TaskItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): TaskViewHolder {
        return TaskViewHolder(LayoutInflater.from(context).inflate(R.layout.item_task, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(taskViewHolder: TaskViewHolder, position: Int) {

        taskViewHolder.tvName.text = "#$position ${items[position].task} "


        taskViewHolder.itemView.setOnClickListener {
            mListener.onItemCLicked(position)
        }

        taskViewHolder.itemView.setOnLongClickListener {
            mListener.onItemLongCLicked(position)
            true
        }
    }

    fun setListener(listener: TaskItemClickListener) {
        this.mListener = listener
    }

    fun setData(items: ArrayList<Task>) {
        this.items = items
    }

    fun appendData(newTaskAdded: Task) {
        this.items.add(newTaskAdded)
        notifyItemInserted(items.size - 1)
    }

}

class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var tvName = view.tvTask

}


