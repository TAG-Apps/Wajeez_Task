package  com.wajeez.sample.model.response

import com.google.gson.annotations.SerializedName

class BaseResponse {

    @SerializedName("message")
    val message: String? = null
    @SerializedName("code")
    val status: Int? = null
    @SerializedName("status")
    var success: Boolean? = false


}

