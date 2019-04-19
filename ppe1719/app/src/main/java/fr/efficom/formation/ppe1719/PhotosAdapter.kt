package fr.efficom.formation.ppe1719

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.stfalcon.imageviewer.StfalconImageViewer
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback

class PhotosAdapter(val photos: List<Photo>, val onPhotoDownload: (photo:Photo)->Unit) : RecyclerView.Adapter<PhotoViewHolder>() {
    val bornesService = createBornesService()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        //Inflater permet de charger une vue depuis un layout xml
        val inflater = LayoutInflater.from(parent.context)
        //Chargement du layout depuis le xml
        val photoItemView = inflater.inflate(R.layout.photo_item_layout, parent, false)
        //Création du view holder qui va mettre en cache cette vue ainsi que des sous-vues
        val photoViewHolder = PhotoViewHolder(photoItemView)
        return photoViewHolder
    }

    override fun getItemCount(): Int {
    return photos.size
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        //mettre dans une constante la photo courante
        val photo = photos[position]
        //mettre a jour le texte
        holder.likeTextView.text = photo.date_prise
        val imageUrl = if (photo.estAime ==1) R.drawable.ic_heart else R.drawable.ic_grey_heart
       holder.likeImageView.setOnClickListener{
           Log.d("ca passe", "${photo.id} - ${photo.estAime}")
         val request =  bornesService.toggleLike(photo.id)
           request.enqueue(object :Callback<ResponseBody>{
               override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

               }

               override fun onResponse(call: Call<ResponseBody>, response: retrofit2.Response<ResponseBody>) {
                   val body = response.body()
                   if(body !=null){
                       val isLiked = body.string().toInt()
                       photo.estAime = isLiked
                       if(isLiked ==1){
                           holder.likeImageView.setImageResource(R.drawable.ic_heart)
                       } else {
                           holder.likeImageView.setImageResource(R.drawable.ic_grey_heart)
                       }
                   }

                   }


           })


           }
        holder.photoImageView.setOnClickListener {
            StfalconImageViewer.Builder<Photo>(holder.photoImageView.context, listOf(photo)) { view, photo ->
                Glide.with(view.context).load(photo.url).into(view)
            }.show()
        }
        holder.downloadButton.setOnClickListener{
            onPhotoDownload(photo)
            true

        }



        //mettre a jour la photo
        Glide.with(holder.itemView.context).load(photo.url).into(holder.photoImageView)
        Glide.with(holder.likeImageView.context).load(imageUrl).into(holder.likeImageView)

    }


}