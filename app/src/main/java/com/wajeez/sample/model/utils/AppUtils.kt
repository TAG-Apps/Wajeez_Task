package  com.wajeez.sample.model.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.wajeez.sample.R
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppUtils @Inject constructor(
    @ApplicationContext val context: Context
) {

    var loading = MutableLiveData(false)
    val db = Firebase.firestore
    val firebaseStore = FirebaseStorage.getInstance()
    val storageReference = FirebaseStorage.getInstance().reference

     fun setup() {
        // [START get_firestore_instance]
        // [END get_firestore_instance]

        // [START set_firestore_settings]
        val settings = firestoreSettings {
            isPersistenceEnabled = true
        }
        db.firestoreSettings = settings
        // [END set_firestore_settings]

    }

     fun setupCacheSize() {

        // [START fs_setup_cache]
        val settings = firestoreSettings {
            cacheSizeBytes = FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED
        }
        db.firestoreSettings = settings
        // [END fs_setup_cache]
    }

    @Singleton
    private fun getShared(): SharedPreferences {

        return EncryptedSharedPreferences.create(
            USER_DATA,
            MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

    }

    fun clearAllData() = getShared().edit().clear().apply()


    fun savePreferencesString(key: String, value: String?) =
        getShared().edit().putString(key, value).apply()


    fun savePreferencesNumber(key: String, value: Int) =
        getShared().edit().putInt(key, value).apply()


    fun savePreferencesBoolean(key: String, value: Boolean) {
        getShared().edit().putBoolean(key, value).apply()
    }




    fun getPreferencesString(key: String): String =
        getShared().getString(key, "") ?: ""


    fun getPreferencesStringIfNull(key: String): String? =
        getShared().getString(key, null)


    fun getPreferencesBoolean(key: String?): Boolean = getShared().getBoolean(key, false)



    fun getPreferencesInt(key: String?): Int =
        getShared().getInt(key, 0)


    fun checkViews(vararg views: EditText): Boolean {
        for (ed in views) {
            if (ed.text.isNullOrBlank()) {
                ed.error = context.getString(R.string.empty_field)
                return false
            }
        }
        return true
    }


    fun showSnack(activity: Activity, message: String?) {
        val snackbar = Snackbar.make(
            activity.findViewById(android.R.id.content),
            message!!, Snackbar.LENGTH_LONG
        )
        snackbar.setActionTextColor(ContextCompat.getColor(activity, R.color.purple_500))
        snackbar.setAction(
            activity.getString(android.R.string.ok)
        ) { v: View? -> snackbar.dismiss() }
        snackbar.show()
    }


    fun hideKeyboard(view: View) {

        val keyboardManager =
            context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        keyboardManager.hideSoftInputFromWindow(view.windowToken, 0)

    }


    @SuppressLint("MissingPermission")
    fun isInternetAvailable(context: Context): Boolean {

        var result = false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val actNw =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            result = when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.run {
                connectivityManager.activeNetworkInfo?.run {
                    result = when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }

                }
            }
        }

        if (!result)
            Toast.makeText(
                context,
                "No internet connection, please check your connection",
                Toast.LENGTH_LONG
            ).show()


        return result
    }

    fun setLocale(activity: Activity, languageCode: String) {

        val resources = activity.resources
        val config = resources.configuration

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            config.setLocale(Locale(languageCode.lowercase(Locale.getDefault())))
        } else {

            val locale = Locale(languageCode)
            Locale.setDefault(locale)
            config.setLocale(locale)


            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N)
                context.createConfigurationContext(config)
        }

        resources.updateConfiguration(config, resources.displayMetrics)


    }


    fun setLocale(config: Configuration?) {

        if (config == null)
            return

        val resources = context.resources

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N)
            context.createConfigurationContext(config)
        else
            resources.updateConfiguration(config, resources.displayMetrics)


    }


    fun calcDifferentTime(firstDate: String, lastDate: String): Boolean {

        val date1: Date?
        val date2: Date?


        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        try {
            date1 = simpleDateFormat.parse(firstDate)
            date2 = simpleDateFormat.parse(lastDate)
        } catch (e: ParseException) {
            e.printStackTrace()

            return false
        }

        val difference = date2!!.time - date1!!.time

        if (difference > 0)
            return true

        return false

    }


    fun createPartFromText(text: String): RequestBody {
        return text.toRequestBody(MultipartBody.FORM)
    }



}