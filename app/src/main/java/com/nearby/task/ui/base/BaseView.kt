package com.nearby.task.ui.base

import android.content.Context
import android.view.View

public interface BaseView {

    /**
     * Set LayoutId
     */
    fun getLayoutId(): Int


    /**
     * Binding view
     */
    fun onBinding()


    /**
     * Sets background.
     *
     * @param drawableId the drawable id
     */
    fun setBackground(drawableId: Int)


    /**
     * Show error.
     *
     * @param error the error
     */
    fun showErrorMessage(error: String?, view: View?)

    /**
     * Show success message.
     *
     * @param message the message
     */
    fun showSuccessMessage(message: String?, view: View?)

    /**
     * Show success message.
     *
     * @param message the message
     */




//    fun showBottomToastMessage(
//        message: String?,
//        link: String? = null,
//        view: View?,
//        anyItemClickListener: AnyItemClickListener,
//        anchorView: View?
//    )


    /**
     * Show a view with a progress bar indicating a loading process.
     */
    fun showLoading()


    /**
     * Hide a loading view.
     */
    fun hideLoading()

    /**
     * Stop a loading view.
     */
    fun stopLoading()


    /**
     * Context context.
     *
     * @return the context
     */
    fun context(): Context?

}