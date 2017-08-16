package entities;

import android.util.Log;

import exceptions.InvalidMoveException;
import exceptions.InvalidRuleException;

/**
 * Created by neaGaze on 8/15/17.
 */

public class Rules {

    Game game;

    public Rules(Game game){
        this.game = game;
    }

    /**
    * Check the rules before placing any Piece
    ***/
    public boolean checkLegalMoves (int x, int y, COLORS currentTurn) throws InvalidRuleException, InvalidMoveException{

        Log.e("pos","("+x+", "+y+") "+ game.getBoard().checkIfBoardFilled());

        // can't insert at places in board where there are already pieces
        if (game.getBoard().getPiece(x, y) != null) throw new InvalidRuleException("Piece already exits");

        // check if the neighbours have pieces or no. Return true if they have neightbors , false otherwise
        boolean hasNoNeighbours = checkNeighbours(x, y, true, currentTurn);
        if(hasNoNeighbours)
            throw new InvalidRuleException("No neighbouring blocks has any pieces");

        // check if the piece is valid for individual player
        boolean hasValidTurn = checkNeighbours(x, y, false, currentTurn);
        if(hasValidTurn)
            throw new InvalidRuleException("No valid move for current player");

        return true;
    }

    private boolean checkNeighbours(int x, int y, boolean checkValidTurn, COLORS currentColor) {
        boolean hasNoNeighbours = true;
        if(x > 0 && y > 0)
            if ((((Piece)game.getBoard().getPiece(x - 1, y - 1)) != null) &&
                (checkValidTurn || (!((Piece)game.getBoard().getPiece(x - 1, y - 1)).isCounterColor(currentColor)))) hasNoNeighbours = false;
        if(x > 0)
            if (((Piece)game.getBoard().getPiece(x - 1, y)) != null &&
                (checkValidTurn || (!((Piece)game.getBoard().getPiece(x - 1, y)).isCounterColor(currentColor)))) hasNoNeighbours = false;
        if(x > 0 && y < (game.getBoard().getSize() - 1))
            if (((Piece)game.getBoard().getPiece(x - 1, y + 1)) != null &&
                (checkValidTurn || (!((Piece)game.getBoard().getPiece(x - 1, y + 1)).isCounterColor(currentColor)))) hasNoNeighbours = false;
        if(y > 0)
            if (((Piece)game.getBoard().getPiece(x, y - 1)) != null &&
                (checkValidTurn || (!((Piece)game.getBoard().getPiece(x, y - 1)).isCounterColor(currentColor)))) hasNoNeighbours = false;
        if(y < (game.getBoard().getSize() - 1))
            if (((Piece)game.getBoard().getPiece(x, y + 1)) != null &&
                (checkValidTurn || (!((Piece)game.getBoard().getPiece(x, y + 1)).isCounterColor(currentColor)))) hasNoNeighbours = false;
        if(x < (game.getBoard().getSize() - 1) && y > 0)
            if (((Piece)game.getBoard().getPiece(x + 1, y - 1)) != null &&
                (checkValidTurn || (!((Piece)game.getBoard().getPiece(x + 1, y - 1)).isCounterColor(currentColor)))) hasNoNeighbours = false;
        if(x < (game.getBoard().getSize() - 1))
            if (((Piece)game.getBoard().getPiece(x + 1, y)) != null &&
                (checkValidTurn || (!((Piece)game.getBoard().getPiece(x + 1, y)).isCounterColor(currentColor)))) hasNoNeighbours = false;
        if(x < (game.getBoard().getSize() - 1) && (y < game.getBoard().getSize() - 1))
            if (((Piece)game.getBoard().getPiece(x + 1, y + 1)) != null &&
                (checkValidTurn || (!((Piece)game.getBoard().getPiece(x + 1, y + 1)).isCounterColor(currentColor)))) hasNoNeighbours = false;

        return hasNoNeighbours;
    }

    /**
     * Rules for the game over
     **/
    public boolean isGameOver(Board board){
        if(board.checkIfBoardFilled()) return true;

        return false;
    }


}
