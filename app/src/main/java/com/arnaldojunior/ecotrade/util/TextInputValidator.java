package com.arnaldojunior.ecotrade.util;

import android.util.Patterns;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

public class TextInputValidator {

    public static boolean validate(TextInputLayout inputLayout) {
        EditText editText = inputLayout.getEditText();
        if (isEmpty(editText)) {
            inputLayout.setError("Campo obrigatório!");
            inputLayout.setErrorEnabled(true);
            return false;
        } else {
            if (isEmail(editText) && !validateEmail(editText)) {
                inputLayout.setError("E-mail inválido!");
                inputLayout.setErrorEnabled(true);
                return false;
            } else {
                if (isPassword(editText) && !validatePassword(editText)) {
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

    /**
     * Check if all inputs are valid.
     * @return a boolean
     */
    public static boolean validateAllInputs(View view) {
        List<TextInputLayout> textInputLayouts = Utils.findViewsWithType(view, TextInputLayout.class);
        boolean noErrors = true;

        for (TextInputLayout textInputLayout : textInputLayouts) {
            if (!validate(textInputLayout))
                noErrors = false;
        }
        return noErrors;
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

    public static boolean isEmail(EditText editText) {
        return editText.getInputType() == 33 ? true : false;
    }

    public static boolean isPassword(EditText editText) {
        return editText.getInputType() == 129 ? true : false;
    }
}
