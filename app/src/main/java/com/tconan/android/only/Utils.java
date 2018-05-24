package com.tconan.android.only;

import android.widget.Toast;

public class Utils {

    private static Toast toast = null;
    public static void toast(String content) {
        if (toast != null) {
            toast.cancel();
        }

        toast = Toast.makeText(App.getContext(), content, Toast.LENGTH_SHORT);
        toast.show();
    }
}
