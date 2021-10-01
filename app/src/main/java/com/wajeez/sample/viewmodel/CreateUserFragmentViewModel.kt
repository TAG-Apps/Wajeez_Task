package  com.wajeez.sample.viewmodel

import android.net.Uri
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.UploadTask
import com.wajeez.sample.model.data.UserModel
import com.wajeez.sample.model.utils.AppUtils
import com.wajeez.sample.model.utils.USERS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CreateUserFragmentViewModel @Inject constructor(
    private val appUtils: AppUtils
) : ViewModel() {


    fun checkViews(
        id: String,
        name: EditText,
        profilePictureUrl: String
    ): LiveData<Boolean>? {

        if (!appUtils.checkViews(name)) {
            return null
        }

        val userData =
            UserModel(
                id = id,
                name = name.text.toString(),
                profilePictureUrl = profilePictureUrl,
            )

        return addUser(userData)

    }

     fun uploadImage(filePath: Uri?): LiveData<String> {

        val mUploadeImageLiveData = MutableLiveData<String>()

        if(filePath != null){
            val ref = appUtils.storageReference?.child("usersImages/" + UUID.randomUUID().toString())
            val uploadTask = ref?.putFile(filePath!!)

            val urlTask = uploadTask?.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                return@Continuation ref.downloadUrl
            })?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result

                    mUploadeImageLiveData.postValue(downloadUri.toString())
                } else {
                    // Handle failures
                }
            }?.addOnFailureListener{

            }
        }else{
            //Toast.makeText(context, "Please Upload an Image", Toast.LENGTH_SHORT).show()
        }

        return mUploadeImageLiveData
    }

    private fun addUser(userData: UserModel): LiveData<Boolean> {

        val mUserLiveData = MutableLiveData<Boolean>()

        viewModelScope.launch(Dispatchers.IO) {

            if (appUtils.loading.value == true) return@launch

            appUtils.loading.postValue(true)

            appUtils.db.collection(USERS).add(userData)
                .addOnSuccessListener { documentReference ->
                    mUserLiveData.postValue(true)
                }
                .addOnFailureListener { e ->
                    mUserLiveData.postValue(false)
                }
            appUtils.loading.postValue(false)
        }

        return mUserLiveData
    }
}