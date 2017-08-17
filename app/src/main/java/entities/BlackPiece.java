package entities;

import java.io.Serializable;

/**
 * Created by neaGaze on 8/15/17.
 */

public class BlackPiece extends Piece implements Serializable{

    public BlackPiece(){
        this.color = COLORS.BLACK;
    }

    @Override
    public Piece flip() {
        color = COLORS.WHITE;
        return this;
    }

}
