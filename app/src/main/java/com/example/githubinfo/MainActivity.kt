package com.example.githubinfo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.activityUiThread
import org.jetbrains.anko.doAsync
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener {
            // async code starts here, it'll run in the background
            doAsync {
                // passing the return value to variable
                val githubInfo = fetchGitHubInfo(txtSearch.text.toString())
                // using the variable as json and parsing it
                val jsonReader = JSONObject(githubInfo)
                /* returning to the UI thread and setting the text attribute with the field "name" from the json */
                activityUiThread { txtUsername.text = jsonReader.getString("name") }
            }
        }
    }

    //    always make data fetching private
    private fun fetchGitHubInfo(login_id: String): String {
        val url = "https://api.github.com/users/$login_id"
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()
        val response = client.newCall(request).execute()
        // getting the JSON from the request
        return response.body().string()
    }

    override fun onResume() {
        super.onResume()
        txtSearch.setText("")
        // this dynamically set the hint if text field is empty, also overrides the hint set in the xml file
        txtSearch.hint = "Enter GitHub username"
    }
}
