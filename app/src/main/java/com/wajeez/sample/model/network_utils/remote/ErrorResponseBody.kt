package  com.wajeez.sample.model.network_utils.remote

import com.google.gson.annotations.SerializedName

data class ErrorResponseBody(
    @SerializedName("success")
    val success: Boolean?,
    @SerializedName("status")
    val status: Int?,
    @SerializedName("message")
    val message: String?
)