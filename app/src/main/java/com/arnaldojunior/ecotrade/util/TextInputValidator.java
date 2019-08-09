package com.arnaldojunior.ecotrade.util;

import android.widget.EditText;

public class TextInputValidator {

    public static boolean isEmpty(EditText editText) {
        String text = editText.getText().toString();
        return text.length() == 0 ? true : false;
    }
}
