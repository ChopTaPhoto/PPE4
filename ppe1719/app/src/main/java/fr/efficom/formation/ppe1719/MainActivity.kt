package fr.efficom.formation.ppe1719

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    val bornesService: BornesService

    init {
        val urlApi = "http://coralielecocq.fr/"
        val retrofit = Retrofit.Builder().baseUrl(urlApi)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        bornesService = retrofit.create(BornesService::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val logoUrl = "https://image.noelshack.com/fichiers/2019/14/5/1554449094-logo.png"
        Glide.with(logoImageView.context)
            .load(logoUrl)
            .into(logoImageView)


        login_button.setOnClickListener {
            val intent = Intent(this@MainActivity, PhotosActivity::class.java)
            Log.d("MainActivity","code event: ${code_event.text.toString()}")
            intent.putExtra("codeEvent",code_event.text.toString())
            startActivity(intent)
        }

    }

    fun login() {
        val tokenRequest = bornesService.loginUser("toto")
        tokenRequest.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.w("bornesService", "Echec loginUser")
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val body = response.body()
                if (body != null) {
                 //   token_text_view.text = body.string()
                }
            }
        })
    }

    fun code() {
        val tokenRequest = bornesService.CodePhoto(code_event.text.toString())
        tokenRequest.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.w("bornesService", "Echec photos_event")
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val body = response.body()
                if (body != null) {
                   // CodeView.text = body.string()
                }
            }
        })

    }

    fun getPhotoAsList() {
        val request = bornesService.getPhotoAsList(code_event.text.toString())
        request.enqueue(object : Callback<List<Photo>>{
            override fun onFailure(call: Call<List<Photo>>, t: Throwable) {

            }
            override fun onResponse(call: Call<List<Photo>>, response: Response<List<Photo>>) {
                val photos = response.body()
                if (photos !=null){
             //       CodeView.text = photos.joinToString ( "/" )
                    Glide.with(logoImageView.context)
                        .load(photos[0].url)
                        .into(logoImageView)
                }
            }



        })
    }
}