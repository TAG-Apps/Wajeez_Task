package  com.wajeez.sample.view.fragment_factory

import android.content.Context
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CustomNavHost :NavHostFragment(){

    @Inject
    lateinit var factory: CustomFragmentFactory


    override fun onAttach(context: Context) {
        super.onAttach(context)
        childFragmentManager.fragmentFactory = factory
    }


}