package com.example.week6

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.app.Activity
import android.content.Intent
import com.example.week6.ROOM.AppDatabase
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.bumptech.glide.Glide
import com.example.week6.ROOM.Task
import kotlinx.android.synthetic.main.activity_add_task.*
import kotlin.collections.ArrayList


class AddTaskActivity : AppCompatActivity() {

    var task1 =Task()

    lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        db = AppDatabase.invoke(this) // get Room database instance

        handleSubmitData()

    }

    private fun handleSubmitData() {
        buttonadd.setOnClickListener {
            // FIXME:  validate empty input value
            task1.task = tvTask.text.toString()


            val taskDAO = db.taskDAO() // get DAO instance
            val id = taskDAO.insert(task1) // insert task object to ROOM database

            task1.id = id.toInt()

            /**
             * send inserted student-object to main screen when insert database successful
             */
            val intent = Intent()
            intent.putExtra(TASK_OBJECT_KEY, task1)
            setResult(Activity.RESULT_OK, intent);
            finish()
        }
    }
}
