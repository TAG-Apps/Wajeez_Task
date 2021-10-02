package  com.wajeez.sample.view.main.dialogs

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.viewbinding.library.fragment.viewBinding
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.wajeez.sample.R
import com.wajeez.sample.databinding.FragmentCreateUserBinding
import com.wajeez.sample.databinding.FragmentMainBinding
import com.wajeez.sample.view.fragment_factory.ParentDialogFragment
import com.wajeez.sample.view.main.MainFragmentDirections
import com.wajeez.sample.viewmodel.CreateUserFragmentViewModel
import com.wajeez.sample.viewmodel.MainFragmentViewModel
import java.io.IOException
import java.util.*
import kotlin.collections.HashMap

class CreateUserFragment : ParentDialogFragment(R.layout.fragment_create_user) {

    private val TAG = "CreateUserFragment"

    private val mBinding: FragmentCreateUserBinding by viewBinding()
    private val viewModel: CreateUserFragmentViewModel by hiltNavGraphViewModels(R.id.nav_graph)

    private var profilePictureUrl: String = ""
    private val PICK_IMAGE_REQUEST = 71
    private var filePath: Uri? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(false)
        mBinding.callback = this
    }

    fun chooseImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    fun done() {
        viewModel.uploadImage(filePath)?.observe(requireActivity(), {
            if(it.isNotEmpty())
                addUser(it)
        })
    }

    private fun addUser(imagePath: String) {

        mBinding.editTextTextPersonName

        viewModel.checkViews(
            UUID.randomUUID().toString(),
            mBinding.editTextTextPersonName,
            imagePath
        )?.observe(requireActivity(), {
            if (it)
                findNavController().navigate(CreateUserFragmentDirections.actionCreateUserFragmentToMainFragment())
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data == null || data.data == null) {
                return
            }
            filePath = data.data
        }
    }
}