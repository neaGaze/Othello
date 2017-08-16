package exceptions;

import android.util.Log;

/**
 * Created by neaGaze on 8/16/17.
 */

public class InvalidRuleException extends Exception {

    public InvalidRuleException(String message){
        super(message);
        Log.e("PieceNotAssignException","");
    }
}
