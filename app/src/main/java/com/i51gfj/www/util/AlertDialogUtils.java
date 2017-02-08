/*
package com.i51gfj.www.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.Window;



public class AlertDialogUtils {
    public static AlertDialog alertDialog;
    public static Dialog dialog;
    public static Window window;

    private AlertDialogUtils() {

    }

    */
/**
     * 未设置动画
     *
     * @param context
     * @param rid
     * @return
     *//*

    public static AlertDialog getInstance(Context context, int rid) {
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.show();
        alertDialog.setContentView(rid);
        window = alertDialog.getWindow();
        return alertDialog;
    }

    public static Dialog getDialogInstance(Context context, int rid) {
        dialog = new Dialog(context, R.style.Dialog_Fullscreen);
        dialog.show();
        dialog.setContentView(rid);
        window = dialog.getWindow();
        return dialog;
    }

    */
/**
     * 设置动画
     *
     * @param context
     * @param rid
     * @return
     *//*

    public static AlertDialog getInstance(Context context, int rid, int animid) {
        alertDialog = new AlertDialog.Builder(context).create();
        if (alertDialog != null) {
            alertDialog.show();
            alertDialog.setContentView(rid);
            window = alertDialog.getWindow();
            window.setWindowAnimations(animid);
        }
        return alertDialog;
    }

    public static Window getWindow() {
        if (window != null) {
            return window;
        }
        return null;
    }

}
*/
