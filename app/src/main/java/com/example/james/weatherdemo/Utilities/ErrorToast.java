package com.example.james.weatherdemo.Utilities;

import android.content.Context;
import android.widget.Toast;


class ErrorToast {

    void ShowToastMsg(String string) {
        Context context = AppContext.getContext();
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, string, duration);
        toast.show();
    }
}
