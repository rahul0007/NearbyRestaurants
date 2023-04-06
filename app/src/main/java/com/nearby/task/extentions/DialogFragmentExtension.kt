package com.nearby.task.extentions

import android.os.Handler
import android.os.Looper
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

fun FragmentManager?.safeDialogDismissAllowingStateLoss(
    tag: String,
    dialogFragmentDefault: Fragment?
) {
    this?.let {
        Handler(Looper.getMainLooper()).postDelayed({
            val fragment = this.findFragmentByTag(tag)
            fragment?.let {
                if (fragment is DialogFragment) {
                    fragment.dismissAllowingStateLoss()
                }
            } ?: kotlin.run {
                dialogFragmentDefault?.let {
                    if (dialogFragmentDefault is DialogFragment) {
                        dialogFragmentDefault.dismissAllowingStateLoss()
                    }
                }
            }
        }, 500)
    }
}