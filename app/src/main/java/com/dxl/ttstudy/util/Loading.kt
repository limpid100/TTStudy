package com.dxl.ttstudy.util

import android.content.Context
import com.dxl.ttstudy.util.Loading.showLoading
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.impl.LoadingPopupView
import java.lang.ref.WeakReference

private object Loading {

    private var loadingDialogReference: WeakReference<LoadingPopupView>? = null

    fun showLoading(context: Context, text: String = "加载中...") {
        var loadingDialog = loadingDialogReference?.get()
        if (loadingDialog == null) {
            loadingDialog = XPopup.Builder(context).dismissOnBackPressed(false)
                .dismissOnTouchOutside(false)
                .isDestroyOnDismiss(false)
                .asLoading(text)
            loadingDialogReference = WeakReference(loadingDialog)
        }
        loadingDialog?.setTitle(text)?.show()

    }

    fun dismissLoading(){
        loadingDialogReference?.get()?.smartDismiss()
    }

}

fun Context.showLoading(text: String = "加载中") {
    showLoading(this, text)
}

fun Context.dismissLoading() {
    Loading.dismissLoading()
}

