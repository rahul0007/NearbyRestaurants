package com.nearby.task.ui.base
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.nearby.task.utils.LocationManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
abstract class BaseFragment : Fragment(), BaseView {
    @Inject
    internal lateinit var locationManager: LocationManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //(activity as DashBoardActivity?)?.onclicked()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(getLayoutId(), container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        locationManager.setActivity(requireActivity() as AppCompatActivity)
        onBinding()
    }




    override fun onDestroy() {
        super.onDestroy()
    }

    override fun setBackground(drawableId: Int) {

    }

    open fun onBackPressed() {

    }

    override fun stopLoading() {
    }

    override fun context(): Context? {
        return requireContext()
    }
}