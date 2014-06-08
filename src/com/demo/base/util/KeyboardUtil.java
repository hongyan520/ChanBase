package com.demo.base.util;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class KeyboardUtil
{
    public static void showSoftInputMethod(Context context, View v) {
        if (v != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(v, InputMethodManager.RESULT_SHOWN);
        }
    }
}
