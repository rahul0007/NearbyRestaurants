package com.nearby.task.dialogs
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.nearby.task.R
import com.nearby.task.extentions.safeDialogDismissAllowingStateLoss
import com.nearby.task.utils.AnimatedGifImageView

class MyDialogLoading : DialogFragment() {

    companion object {

        val TAG = MyDialogLoading::class.java.simpleName

        fun newInstance(): MyDialogLoading {
            return MyDialogLoading().apply {
                arguments = createBundle()
            }
        }

        fun dismiss(activity: FragmentActivity) {



            synchronized(activity) {
                activity.supportFragmentManager.findFragmentByTag(TAG)
                    ?.takeIf { it is DialogFragment }
                    ?.let { it as DialogFragment }
                    ?.also {
                        activity.supportFragmentManager.safeDialogDismissAllowingStateLoss(TAG, it)
                    }
            }
        }

        private fun createBundle(): Bundle {
            return Bundle()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.OLBLoadingDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        dialog?.window!!.setBackgroundDrawable(
            ColorDrawable(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.loading_dialog_background_color
                )
            )
        )
        dialog?.setCancelable(false)
        return inflater.inflate(R.layout.view_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val animation: AnimatedGifImageView = view.findViewById(R.id.imgGif)
        animation.setAnimatedGif(R.raw.loader, AnimatedGifImageView.TYPE.AS_IS)
    }

    override fun show(manager: FragmentManager, tag: String?) {
        val dialogFragment = manager.findFragmentByTag(TAG)
        if (dialogFragment == null) {
            val ft = manager.beginTransaction()
            ft.add(this, tag)
            ft.commitAllowingStateLoss()
        }
    }

}