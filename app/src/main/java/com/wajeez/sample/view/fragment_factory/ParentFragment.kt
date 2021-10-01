package  com.wajeez.sample.view.fragment_factory

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.wajeez.sample.model.utils.AppUtils
import com.wajeez.sample.model.interfaces.OnProgressLoadingListener
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
open class ParentFragment
constructor(layoutRest: Int) :
    Fragment(layoutRest) {


    private lateinit var listener: OnProgressLoadingListener

    @Inject
    lateinit var appUtils: AppUtils

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as OnProgressLoadingListener
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        appUtils.loading.observe(viewLifecycleOwner, {
            if (it)
                listener.showProgress()
            else
                listener.hideProgress()

        })

    }


    override fun onPause() {
        super.onPause()
        listener.hideProgress()
    }


}

