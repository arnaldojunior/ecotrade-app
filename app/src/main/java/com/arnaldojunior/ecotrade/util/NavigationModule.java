package com.arnaldojunior.ecotrade.util;

import android.content.Context;
import android.content.Intent;

import com.arnaldojunior.ecotrade.AdActivity;
import com.arnaldojunior.ecotrade.LoginActivity;
import com.arnaldojunior.ecotrade.MainActivity;
import com.arnaldojunior.ecotrade.NewAdActivity;
import com.arnaldojunior.ecotrade.SignupActivity;
import com.arnaldojunior.ecotrade.model.Anuncio;

public class NavigationModule {

    public static void goToLoginActivity(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    public static void goToSignupActivity(Context context) {
        Intent intent = new Intent(context, SignupActivity.class);
        context.startActivity(intent);
    }

    public static void goToNewAdActivity(Context context) {
        Intent intent = new Intent(context, NewAdActivity.class);
        context.startActivity(intent);
    }

    public static void goToAdActivity(Context context, Anuncio anuncio) {
        Intent intent = new Intent(context, AdActivity.class);
        intent.putExtra("Anuncio", anuncio);
        context.startActivity(intent);
    }

    public static void goToMainActivity(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
