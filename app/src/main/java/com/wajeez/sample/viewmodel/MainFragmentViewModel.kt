package  com.wajeez.sample.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wajeez.sample.model.data.UserModel
import com.wajeez.sample.model.utils.AppUtils
import com.wajeez.sample.model.utils.USERS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainFragmentViewModel @Inject constructor(
    private val appUtils: AppUtils
) : ViewModel() {

    fun getUsers(filterType: Int): LiveData<List<UserModel>> {


        val mUsersLiveData = MutableLiveData<List<UserModel>>()


        appUtils.loading.postValue(true)

        viewModelScope.launch(Dispatchers.IO) {
        }

        val arrayList = ArrayList<UserModel>()

            val response =
                appUtils.db.collection(USERS)
                    .get()
                    .addOnSuccessListener { documents ->
                for (document in documents) {
                    arrayList.add(document.toObject(UserModel::class.java))
                }

            appUtils.loading.postValue(false)

            if (arrayList.size > 0) {
                mUsersLiveData.postValue(arrayList)
            }
        }

        return mUsersLiveData
    }
}