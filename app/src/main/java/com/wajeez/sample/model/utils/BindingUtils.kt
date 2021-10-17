package  com.wajeez.sample.model.utils

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

object BindingUtils {

    @JvmStatic
    @BindingAdapter("loadUserImage")
    fun bindImage(image: ImageView, url: String?) {
        if (!url.isNullOrBlank()) {
            Glide.with(image.context)  //2
                .load(url) //3
                .circleCrop()
                .centerCrop() //4
                .into(image)
        }else{
            image.setImageBitmap(null)
        }
    }
}