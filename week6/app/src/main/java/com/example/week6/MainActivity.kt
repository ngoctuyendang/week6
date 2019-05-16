package com.example.week6

import android.annotation.SuppressLint
import android.app.Activity
import android.arch.persistence.room.Room
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.example.week6.ROOM.*
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.concurrent.schedule


@SuppressLint("SetTextI18n")
class MainActivity : AppCompatActivity() {

    var tasks: ArrayList<Task> = ArrayList()
    lateinit var taskAdapter: TaskAdapter
    lateinit var dao: TaskDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initRoomDatabase()

        setupRecyclerView()

        getTask()


        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, AddTaskActivity::class.java)
            startActivityForResult(intent, CODE_ADD_NEW_USER)
        }

        fab.setOnLongClickListener {
            dao.deleteAllTask()
            val size = tasks.size
            if (size > 0) {
                for (i in 0 until size) {
                    tasks.removeAt(0)
                }

                taskAdapter.notifyItemRangeRemoved(0, size)
            }
            return@setOnLongClickListener false
        }

    }

    private fun initRoomDatabase() {
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, DATABASE_NAME
        ).allowMainThreadQueries()
            .build()
        dao = db.taskDAO()
    }

    /**
     * setup layout manager and recyclerview
     */
    private fun setupRecyclerView() {
        rvStudents.layoutManager = LinearLayoutManager(this) as RecyclerView.LayoutManager?

        taskAdapter = TaskAdapter(tasks, this)

        rvStudents.adapter = taskAdapter

        taskAdapter.setListener(taskItemCLickListener)
    }

    private val taskItemCLickListener = object : TaskItemClickListener {
        override fun onItemCLicked(position: Int) {

            val intent = Intent(this@MainActivity, DetailActivity::class.java)
            intent.putExtra(USER_NAME_KEY, tasks[position].task)


            startActivity(intent)

        }

        override fun onItemLongCLicked(position: Int) {

            val builder = AlertDialog.Builder(this@MainActivity)
            builder.setTitle("Confirmation")
                .setMessage("Remove ${tasks[position].task} ?")
                .setPositiveButton("OK") { _, _ ->
                    removeItem(position)
                }
                .setNegativeButton(
                    "Cancel"
                ) { dialog, _ -> dialog?.dismiss() }

            val myDialog = builder.create();
            myDialog.show()
        }

        override fun onEditIconClicked(position: Int) {

        }
    }

    private fun removeItem(position: Int) {
        dao.delete(tasks[position]) // remove from Room database  //

        tasks.removeAt(position) // remove student list on RAM

        taskAdapter.notifyItemRemoved(position) // notify data change
        Timer(false).schedule(500) {
            runOnUiThread {
                taskAdapter.setData(tasks)
                taskAdapter.notifyDataSetChanged()
            }
        }
    }


    private fun getTask() {
        val task = dao.getAll() // get Students from ROOM database

        this.tasks.addAll(task) // add to student list

        taskAdapter.notifyDataSetChanged() // notify data changed
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CODE_ADD_NEW_USER && resultCode == Activity.RESULT_OK) {
            val newTaskAdded = data?.extras?.getParcelable(USER_OBJECT_KEY) as Task
            handleOnNewTaskAdded(newTaskAdded)
        }
        if (requestCode == CODE_ADD_UPDATE_USER && resultCode == Activity.RESULT_OK) {
            val newTaskAdded = data?.extras?.getParcelable(USER_OBJECT_KEY) as Task
            handleOnTaskUpdated(newTaskAdded)
        }
    }

    /**
     * Update the item
     */
    private fun handleOnTaskUpdated(newTaskAdded: Task) {
        // TODO : Update the updated item infomation

    }

    /**
     * append new data to task list and notify data change
     */
    private fun handleOnNewTaskAdded(task: Task) {
        taskAdapter.appendData(task)
    }
}
