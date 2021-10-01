package  com.wajeez.sample.view.main

import android.os.Bundle
import android.util.Log
import android.view.*
import android.viewbinding.library.fragment.viewBinding
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
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
import com.google.firebase.firestore.ktx.firestoreSettings
import com.wajeez.sample.model.data.UserModel
import com.wajeez.sample.view.main.dialogs.CreateUserFragment

class MainFragment : ParentFragment(R.layout.fragment_main) {

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

        getUsersData()
    }

    private fun getUsersData() {

        viewModel.getUsers(1).observe(requireActivity(), {
            mUsersAdapter.setUsersData(it as ArrayList<UserModel>)
            mUsersAdapter.notifyDataSetChanged()
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
}