package visapps.moviedatabase.app.utils;

import android.content.Context;
import android.util.DisplayMetrics;

public class Utils{
    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        return (int) (dpWidth / 180);
    }

    public static boolean checkPassword(String password){
        if (password.length() >= 8 && password.length() < 50) {
            return password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$");
        } else {
            return false;
        }
    }
}
