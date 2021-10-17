package com.example.top_10_app
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {
    var questions = mutableListOf<Question>()
    private lateinit var recyclerView: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.rv)
        FetchTopSongs().execute()

    }


    private inner class FetchTopSongs : AsyncTask<Void, Void, MutableList<Question>>() {
        val parser = XMLParser()
        override fun doInBackground(vararg params: Void?): MutableList<Question> {

            val url = URL("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=10/xml")

            val urlConnection = url.openConnection() as HttpURLConnection

            questions = urlConnection.inputStream?.let {

                parser.parse(it)
            }
                    as MutableList<Question>
            return questions
        }

        override fun onPostExecute(result: MutableList<Question>?) {
            recyclerView.isVisible=false
            super.onPostExecute(result)
            val adapter =
                ArrayAdapter(this@MainActivity, android.R.layout.simple_list_item_1, questions)
            recyclerView.adapter = RVAdapter(questions)
            recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)

        }

    }

    fun Getfeed(view: View) {
        recyclerView.isVisible=true
    }

}


