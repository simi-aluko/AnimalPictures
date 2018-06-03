package com.example.simileoluwaaluko.animalpictures

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.KeyEvent
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import android.widget.Toast
import com.example.simileoluwaaluko.animalpictures.Models.AnimalResultModel
import com.example.simileoluwaaluko.animalpictures.Models.Hit
import com.example.simileoluwaaluko.animalpictures.R.id.animal_picture_recycler
import com.example.simileoluwaaluko.animalpictures.R.id.progress_bar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.okhttp.*
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        animal_name.setOnEditorActionListener { v, actionId, event ->
            if(event!=null && (event.keyCode == KeyEvent.KEYCODE_ENTER)){
                //hide soft input keyboard
                val view = this@MainActivity.currentFocus
                if(view != null){
                    val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
                }

                //show progress bar
                progress_bar.visibility = ProgressBar.VISIBLE

                // validate user input and handle fetching of input result
                val animalName = animal_name.text.toString()
                if(validateAnimalNameInput(animalName)){
                    handleNetworkRequest(animalName)
                }else{
                    showToastMessage("You did not enter a valid animal name \uD83D\uDE22")
                    progress_bar.visibility = ProgressBar.GONE
                }
            }
            false
        }
    }

    private fun handleNetworkRequest(animalName : String){
        val baseUrl = getString(R.string.baseUrl)
        val apiKey = getString(R.string.apiKey)
        val client = OkHttpClient()

        val urlBuilder = HttpUrl.parse(baseUrl).newBuilder()
        urlBuilder.addQueryParameter("key",apiKey)
        urlBuilder.addQueryParameter("q",animalName)
        urlBuilder.addQueryParameter("image_type", "photo")
        urlBuilder.addQueryParameter("category", "animals")
        urlBuilder.addQueryParameter("safesearch", "true")


        val url = urlBuilder.build().toString()
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback{

            override fun onResponse(response: Response?) {

                val responseClone = response?.body()?.string()

                val gson = Gson()
                val resources = gson.fromJson(responseClone,AnimalResultModel::class.java)

                var finalResource = arrayListOf<Hit>()
                var index = 0

                for(resource in resources.hits){
                    index++

                    if(resources.hits.isEmpty()) break
                    if(index > 5 && resources.hits.size > 5) break
                    finalResource.add(resource)

                }

                val myAdapter = AnimalPictureAdapter(this@MainActivity, finalResource)
                val layoutManager = LinearLayoutManager(this@MainActivity,
                                    LinearLayoutManager.VERTICAL,false)
                runOnUiThread {
                    progress_bar.visibility = ProgressBar.GONE
                    animal_picture_recycler.layoutManager = layoutManager
                    animal_picture_recycler.adapter = myAdapter

                    if(resources.totalHits == 0) showToastMessage("We couldn't find what you searched for \ud83d\ude22")
                    if(resources.totalHits > 0)showToastMessage("we found $animalName pictures \ud83d\ude0d")

                }
            }

            override fun onFailure(request: Request?, e: IOException?) {
                e?.printStackTrace()
                runOnUiThread {
                    showToastMessage("Sorry, something went wrong! \uD83D\uDE22")
                    progress_bar.visibility = ProgressBar.GONE
                }
            }
        })
    }

    private fun validateAnimalNameInput(animalName : String) : Boolean{
        if(!animalName.isEmpty()){
            return true
        }
        return false
    }

    fun showToastMessage(message : String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
