package com.arnaldojunior.ecotrade.util;

import android.util.Patterns;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;

public class TextInputValidator {

    public static boolean validate(TextInputLayout inputLayout, boolean isEmail, boolean isPassword) {
        EditText editText = inputLayout.getEditText();
        if (isEmpty(editText)) {
            inputLayout.setError("Campo obrigatório!");
            inputLayout.setErrorEnabled(true);
            return false;
        } else {
            if (isEmail && !validateEmail(editText)) {
                inputLayout.setError("E-mail inválido!");
                inputLayout.setErrorEnabled(true);
                return false;
            } else {
                if (isPassword && !validatePassword(editText)) {
                    inputLayout.setError("A senha deve possuir ao menos 6 dígitos!");
                    inputLayout.setErrorEnabled(true);
                    return false;
                } else {
                    inputLayout.setErrorEnabled(false);
                }
            }
        }
        return true;
    }

    public static boolean isEmpty(EditText editText) {
        String text = editText.getText().toString().trim();
        return text.length() == 0 ? true : false;
    }

    public static boolean validateEmail(EditText editText) {
        String email = editText.getText().toString().trim();
        return Patterns.EMAIL_ADDRESS.matcher(email).matches() ? true : false;
    }

    public static boolean validatePassword(EditText editText) {
        String password = editText.getText().toString().trim();
        return password.length() >= 6 ? true : false;
    }
}
