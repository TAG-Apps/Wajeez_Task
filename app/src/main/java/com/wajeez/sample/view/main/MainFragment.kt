package  com.wajeez.sample.view.main

import android.os.Bundle
import android.util.Log
import android.view.*
import android.viewbinding.library.fragment.viewBinding
import androidx.core.widget.doOnTextChanged
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.wajeez.sample.R
import com.wajeez.sample.databinding.FragmentMainBinding
import com.wajeez.sample.view.fragment_factory.ParentFragment
import com.wajeez.sample.viewmodel.MainFragmentViewModel
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.ktx.firestoreSettings
import com.wajeez.sample.model.data.UserModel
import com.wajeez.sample.model.interfaces.OnAdapterItemClicked
import com.wajeez.sample.view.main.dialogs.CreateUserFragment
import com.wajeez.sample.view.main.dialogs.FilterBottomSheet

class MainFragment : ParentFragment(R.layout.fragment_main), OnAdapterItemClicked {

    private val TAG = "MainFragment"

    private val mBinding: FragmentMainBinding by viewBinding()
    private val viewModel: MainFragmentViewModel by hiltNavGraphViewModels(R.id.nav_graph)
    private lateinit var mUsersAdapter: MainAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)
        mUsersAdapter = MainAdapter()

        mBinding.mainRecycle.setHasFixedSize(true)
        mBinding.mainRecycle.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )
        mBinding.mainRecycle.adapter = mUsersAdapter

        context?.let { FirebaseApp.initializeApp(it) }

        mBinding.btnFilter.setOnClickListener{
            showFilterDialogSheet()
        }

        mBinding.seatchEditText.doOnTextChanged { text, start, before, count ->
            viewModel.getUsers().observe(requireActivity(), { it ->

                mUsersAdapter.setUsersData(it.filter { it.name!!.contains(text.toString()) } as ArrayList<UserModel>)

            })
        }

        getUsersData()
    }

    fun showFilterDialogSheet() {

        val sheet = FilterBottomSheet(this)
        sheet.show(parentFragmentManager, sheet.tag)
    }

    private fun getUsersData() {

        viewModel.getUsers().observe(requireActivity(), {
            mUsersAdapter.setUsersData(it as ArrayList<UserModel>)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add_user -> findNavController().navigate(MainFragmentDirections.actionMainFragmentToCreateUserFragment())
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onItemClicked(position: Int, data: Any?) {
        // filter
        viewModel.getUsers().observe(requireActivity(), { it ->

            when (position) {

                1 ->  mUsersAdapter.setUsersData(ArrayList(it.sortedBy { it.profilePictureUrl!!.isEmpty()}) )
                2 ->  mUsersAdapter.setUsersData(ArrayList(it.sortedBy { it.profilePictureUrl!!.isNotEmpty()}) )
                else -> {
                     mUsersAdapter.setUsersData(ArrayList(it.sortedBy { it.profilePictureUrl!!.isEmpty()}) )
                }
            }

        })
    }
}