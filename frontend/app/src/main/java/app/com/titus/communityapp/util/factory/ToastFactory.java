package app.com.titus.communityapp.util.factory;

import android.content.Context;
import android.widget.Toast;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Created by boti on 11.03.2018.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ToastFactory {
    public static Toast getToast(final Context context,String text){
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        return toast;
    }
}
