package entities;

import android.util.Log;

import exceptions.InvalidMoveException;
import exceptions.InvalidRuleException;

/**
 * Created by neaGaze on 8/15/17.
 */

public class Rules {

    Game game;
    Board board;


    enum DIRECTION {
        TOP_LEFT,
        TOP,
        TOP_RIGHT,
        LEFT,
        RIGHT,
        BOTTOM_LEFT,
        BOTTOM,
        BOTTOM_RIGHT
    };

    public Rules(Game game){
        this.game = game;
        this.board = game.getBoard();
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

        // check if the piece is valid to place (piece can only be placed next to the opposite color)
        boolean hasValidTurn = checkNeighbours(x, y, false, currentTurn);
        if(hasValidTurn)
            throw new InvalidRuleException("No valid move for current player");

        // TODO check if the the line has a same color at the end of the row
        //

        return true;
    }

    private boolean checkNeighbours(int x, int y, boolean checkValidTurn, COLORS currentColor) {
        boolean hasNoNeighbours = true;
        if(x > 0 && y > 0)
            if (((board.getPiece(x - 1, y - 1)) != null) &&
                (checkValidTurn || ((board.getPiece(x - 1, y - 1)).isCounterColor(currentColor)))) hasNoNeighbours = false;
        if(x > 0)
            if ((game.getBoard().getPiece(x - 1, y)) != null &&
                (checkValidTurn || ((game.getBoard().getPiece(x - 1, y)).isCounterColor(currentColor)))) hasNoNeighbours = false;
        if(x > 0 && y < (game.getBoard().getSize() - 1))
            if ((game.getBoard().getPiece(x - 1, y + 1)) != null &&
                (checkValidTurn || ((game.getBoard().getPiece(x - 1, y + 1)).isCounterColor(currentColor)))) hasNoNeighbours = false;
        if(y > 0)
            if ((game.getBoard().getPiece(x, y - 1)) != null &&
                (checkValidTurn || ((game.getBoard().getPiece(x, y - 1)).isCounterColor(currentColor)))) hasNoNeighbours = false;
        if(y < (game.getBoard().getSize() - 1))
            if ((game.getBoard().getPiece(x, y + 1)) != null &&
                (checkValidTurn || ((game.getBoard().getPiece(x, y + 1)).isCounterColor(currentColor)))) hasNoNeighbours = false;
        if(x < (game.getBoard().getSize() - 1) && y > 0)
            if ((game.getBoard().getPiece(x + 1, y - 1)) != null &&
                (checkValidTurn || ((game.getBoard().getPiece(x + 1, y - 1)).isCounterColor(currentColor)))) hasNoNeighbours = false;
        if(x < (game.getBoard().getSize() - 1))
            if ((game.getBoard().getPiece(x + 1, y)) != null &&
                (checkValidTurn || ((game.getBoard().getPiece(x + 1, y)).isCounterColor(currentColor)))) hasNoNeighbours = false;
        if(x < (game.getBoard().getSize() - 1) && (y < game.getBoard().getSize() - 1))
            if ((game.getBoard().getPiece(x + 1, y + 1)) != null &&
                (checkValidTurn || ((game.getBoard().getPiece(x + 1, y + 1)).isCounterColor(currentColor)))) hasNoNeighbours = false;

        return hasNoNeighbours;
    }

    /**
     * Convert the color of the lines
     **/
    public void convertColor(int x, int y, COLORS currentTurn, Game.OnUIUpdateListener callback){

        Piece p = (Piece) board.getPiece(x, y);
        if(p == null) return;


        if(x > 0 && y > 0 && board.getPiece(x - 1, y - 1) != null)
            recursiveCheck(x - 1, y - 1, currentTurn, DIRECTION.TOP_LEFT, true, callback);

        if(x > 0 && board.getPiece(x - 1, y) != null)
            recursiveCheck(x - 1, y, currentTurn, DIRECTION.TOP, true, callback);

        if(x > 0 && y < (game.getBoard().getSize() - 1) && board.getPiece(x - 1, y + 1) != null)
            recursiveCheck(x - 1, y + 1, currentTurn, DIRECTION.TOP_RIGHT, true, callback);

        if(y > 0 && board.getPiece(x, y - 1) != null)
            recursiveCheck(x, y - 1, currentTurn, DIRECTION.LEFT, true, callback);

        if(y < (game.getBoard().getSize() - 1) && board.getPiece(x, y + 1) != null)
            recursiveCheck(x, y + 1, currentTurn, DIRECTION.RIGHT, true, callback);

        if(x < (game.getBoard().getSize() - 1) && y > 0 && board.getPiece(x + 1, y - 1) != null)
            recursiveCheck(x + 1, y - 1, currentTurn, DIRECTION.BOTTOM_LEFT, true, callback);

        if(x < (game.getBoard().getSize() - 1) && board.getPiece(x + 1, y) != null)
            recursiveCheck(x + 1, y, currentTurn, DIRECTION.BOTTOM, true, callback);

        if(x < (game.getBoard().getSize() - 1) && (y < (game.getBoard().getSize() - 1)) && board.getPiece(x + 1, y + 1) != null)
            recursiveCheck(x + 1, y + 1, currentTurn, DIRECTION.BOTTOM_RIGHT, true, callback);

        board.setBoardState(x, y, true);

    }

    /**
     * Recursive check
     * */
    public boolean recursiveCheck(int x, int y, COLORS currentTurn, DIRECTION direction, boolean shouldFlip,
                                  Game.OnUIUpdateListener callback) {

        // don't go further if hit the wall
        if (x < 0 || y < 0 || x > (board.getSize() - 1) || y > (board.getSize() - 1)) return false;

        // empty pieces are not valid either
        Piece p = (Piece) board.getPiece(x, y);
        if (p == null) return false;

        // don't go any further if previously visited
        if (board.getBoardState()[x][y]) return false;

        // change board state
        board.setBoardState(x, y, true);

        // don't go further if the color is the same
        if (!p.isCounterColor(currentTurn))
            return true;


        boolean status = false;
        if (direction == DIRECTION.TOP_LEFT) {
            status = recursiveCheck(x - 1, y - 1, currentTurn, direction, shouldFlip, callback);
            if (status && shouldFlip) {
                Piece p1 = (Piece)board.getPiece(x, y).flip();
                callback.changeUIPieceColor(x, y, p1.color);
                Log.v("Rules -> TOP-LEFT","("+x+","+y+") -> "+p1.color);
            }
        }

        if (direction == DIRECTION.TOP) {
            status = recursiveCheck(x - 1, y, currentTurn, direction, shouldFlip, callback);
            if (status && shouldFlip){
                Piece p1 = (Piece)board.getPiece(x, y).flip();
                callback.changeUIPieceColor(x, y, p1.color);
                Log.d("Rules -> TOP","("+x+","+y+") -> "+p1.color);
            }
        }

        if (direction == DIRECTION.TOP_RIGHT) {
            status = recursiveCheck(x - 1, y + 1, currentTurn, direction, shouldFlip, callback);
            if (status && shouldFlip){
                Piece p1 = (Piece)board.getPiece(x, y).flip();
                callback.changeUIPieceColor(x, y, p1.color);
                Log.d("Rules -> TOP-RIGHT","("+x+","+y+") -> "+p1.color);
            }
        }

        if(direction == DIRECTION.LEFT) {
            status = recursiveCheck(x, y - 1, currentTurn, direction, shouldFlip, callback);
            if (status && shouldFlip){
                Piece p1 = (Piece)board.getPiece(x, y).flip();
                callback.changeUIPieceColor(x, y, p1.color);
                Log.d("Rules -> LEFT","("+x+","+y+") -> "+p1.color);
            }
        }

        if(direction == DIRECTION.RIGHT) {
            status = recursiveCheck(x, y + 1, currentTurn, direction, shouldFlip, callback);
            if (status && shouldFlip){
                Piece p1 = (Piece)board.getPiece(x, y).flip();
                callback.changeUIPieceColor(x, y, p1.color);
                Log.d("Rules -> RIGHT","("+x+","+y+") -> "+p1.color);
            }
        }

        if(direction == DIRECTION.BOTTOM_LEFT) {
            status = recursiveCheck(x + 1, y - 1, currentTurn, direction, shouldFlip, callback);
            if (status && shouldFlip){
                Piece p1 = (Piece)board.getPiece(x, y).flip();
                callback.changeUIPieceColor(x, y, p1.color);
                Log.d("Rules -> BOTTOM-LEFT","("+x+","+y+") -> "+p1.color);
            }
        }

        if(direction == DIRECTION.BOTTOM) {
            status = recursiveCheck(x + 1, y, currentTurn, direction, shouldFlip, callback);
            if (status && shouldFlip){
                Piece p1 = (Piece)board.getPiece(x, y).flip();
                callback.changeUIPieceColor(x, y, p1.color);
                Log.d("Rules -> LEFT","("+x+","+y+") -> "+p1.color);
            }
        }

        if(direction == DIRECTION.BOTTOM_RIGHT) {
            status = recursiveCheck(x + 1, y + 1, currentTurn, direction, shouldFlip, callback);
            if (status && shouldFlip){
                Piece p1 = (Piece)board.getPiece(x, y).flip();
                callback.changeUIPieceColor(x, y, p1.color);
                Log.d("Rules -> BOTTOM-RIGHT","("+x+","+y+") -> "+p1.color);
            }
        }

        return status;
    }

    /**
     * Rules for the game over
     **/
    public boolean isGameOver(Board board){
        if(board.checkIfBoardFilled()) return true;

        return false;
    }


}
