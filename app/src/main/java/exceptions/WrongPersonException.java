package exceptions;

import android.util.Log;

/**
 * Created by neaGaze on 8/15/17.
 */

public class WrongPersonException extends Exception {

    public WrongPersonException(String message){
        super(message);
        Log.e("WrongPersonException",""+message);
    }
}
