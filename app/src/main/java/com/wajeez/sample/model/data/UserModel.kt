package  com.wajeez.sample.model.data

data class UserModel(
    val id: String?,
    val name: String?,
    val profilePictureUrl: String?

    )

{
    constructor() : this("", "",
        ""
    )
}
