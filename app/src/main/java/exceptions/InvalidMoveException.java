package exceptions;

import android.util.Log;

/**
 * Created by neaGaze on 8/15/17.
 */

public class InvalidMoveException extends Exception {

    public InvalidMoveException(String message){
        super(message);
        Log.e("PieceNotAssignException","");
    }
}
