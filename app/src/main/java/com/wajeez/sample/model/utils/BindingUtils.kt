package  com.wajeez.sample.model.utils

import android.widget.TextView
import androidx.databinding.BindingAdapter

object BindingUtils {


    @JvmStatic
    @BindingAdapter("addDate", "isTime")
    fun splitText(view: TextView, date: String?, isTime: Boolean) {

        if (date.isNullOrBlank()) {
            view.text = ""
            return
        }

        if (!isTime) {
            view.text = date.split("|")[0]
        } else {
            view.text = date.split("|")[1]
        }

    }





}