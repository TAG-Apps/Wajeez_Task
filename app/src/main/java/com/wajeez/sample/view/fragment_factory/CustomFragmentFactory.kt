package  com.wajeez.sample.view.fragment_factory

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.wajeez.sample.model.utils.AppUtils

import javax.inject.Inject

class CustomFragmentFactory
@Inject constructor(
) : FragmentFactory() {


    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when (className) {

            else -> super.instantiate(classLoader, className)
        }
    }

}