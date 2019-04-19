package fr.efficom.formation.ppe1719

import android.Manifest
import android.app.DownloadManager
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_photos.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PhotosActivity : AppCompatActivity() {

val bornesService: BornesService

    init {
    bornesService = createBornesService()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photos)
         getPhotoAsList()


    }

    fun isStoragePermissionGranted(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) === PackageManager.PERMISSION_GRANTED) {
                Log.v("PERM", "Permission is granted")
                return true
            } else {

                Log.v("PERM", "Permission is revoked")
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    1
                )
                return false
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("PERM", "Permission is granted")
            return true
        }
    }
    fun downloadPhoto(photo: Photo) {
        if (isStoragePermissionGranted()) {
            val uri = Uri.parse(photo.url)
            val r = DownloadManager.Request(uri)
            r.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, uri.lastPathSegment)
            r.allowScanningByMediaScanner()
            r.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            val dm = this.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager?
            if (dm != null) {
                dm.enqueue(r)
            }
        }
    }
    fun getPhotoAsList() {
        val code = intent.getStringExtra("codeEvent")
        val request = bornesService.getPhotoAsList(code)
        request.enqueue(object : Callback<List<Photo>> {
            override fun onFailure(call: Call<List<Photo>>, t: Throwable) {

            }
            override fun onResponse(call: Call<List<Photo>>, response: Response<List<Photo>>) {
                val photos = response.body()
                if (photos !=null){
                    Log.d("PhotosActivity", "Photos: $photos")
                    //création de l'adapter et on le renseigne dans le recycler view
                    photosRecyclerView.adapter = PhotosAdapter(photos, ::downloadPhoto)
                    photosRecyclerView.layoutManager = LinearLayoutManager(this@PhotosActivity)
                    /*
                    Glide.with(imageViewPhoto.context)
                        .load(photos[0].url)
                        .into(imageViewPhoto)
                    */
                }
            }



        })
    }
}
