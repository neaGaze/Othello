package com.practice.neagaze.androidpractice;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import entities.BlackPiece;
import entities.Board;
import entities.COLORS;
import entities.Game;
import entities.Piece;
import entities.PieceInterface;
import entities.WhitePiece;
import exceptions.InvalidMoveException;
import exceptions.InvalidRuleException;
import exceptions.WrongPersonException;

/**
 * Created by neaGaze on 8/15/17.
 */

public class GameFragment extends Fragment {

    private Board board;
    private Game game;

    private TextView tvPlayer1, tvPlayer2, tvTurn;

    public GameFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        game = (Game)getArguments().getSerializable("GAME");
        board = game.getBoard();

        View view = inflater.inflate(R.layout.game_fragment, container, false);

        tvPlayer1 = (TextView) view.findViewById(R.id.tvPlayer1);
        tvPlayer2 = (TextView) view.findViewById(R.id.tvPlayer2);
        tvTurn = (TextView) view.findViewById(R.id.tvTurns);

        final RecyclerView boardRecyclerView = (RecyclerView)view.findViewById(R.id.boardRecyclerView);
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), board.getSize());
        boardRecyclerView.setLayoutManager(gridLayoutManager);
        final RecyclerViewAdapter adapter = new RecyclerViewAdapter(getActivity(), board);
        adapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                Log.e("clicked at",""+position);
                int x = position / board.getSize();
                int y = position % board.getSize();
                boolean hasErrorInMove = false;

                try {
                    game.addPiece(x, y, new Game.OnUIUpdateListener(){
                        @Override
                        public void changeUIPieceColor(int x, int y, COLORS color) {
                            boolean isBlack = color == COLORS.BLACK;
                            // code to change the individual pieces that gets overturned
                            Piece piece = (isBlack ? new BlackPiece() : new WhitePiece());
                            adapter.updateGridPosition(x, y, piece);
                            adapter.notifyDataSetChanged();

                            // change the turn text as well
                            tvTurn.setText(isBlack ? "WHITE TURN" : "BLACK TURN");
                            tvTurn.setTextColor(getResources().getColor(isBlack ? R.color.colorWhite : R.color.colorBlack));
                        }

                        @Override
                        public void onShowSuggestions(boolean suggestionGrid[][]) {
                            adapter.updateSuggestions(suggestionGrid);
                            adapter.notifyDataSetChanged();
                        }
                    });

                } catch (WrongPersonException e) {
                    e.printStackTrace();
                    hasErrorInMove = true;
                    Log.e("WrongPersonExcp",""+e.getMessage());
                } catch (InvalidMoveException e) {
                    e.printStackTrace();
                    hasErrorInMove = true;
                    Log.e("InvalidMoveExcp",""+e.getMessage());
                } catch (InvalidRuleException e) {
                    e.printStackTrace();
                    hasErrorInMove = true;
                    Log.e("InvalidRuleExcp",""+e.getMessage());
                } finally {
                    Log.v("move status",""+hasErrorInMove);
                    if(!hasErrorInMove) {
                        // change the color of the pieces
                        Piece p = (Piece) game.getBoard().getPiece(x, y);
                        view.setBackgroundColor(getResources().getColor((p.color == COLORS.WHITE) ?
                                R.color.colorWhite : R.color.colorBlack));
                    }
                }
            }
        });
        boardRecyclerView.setAdapter(adapter);
        return view;
    }

    public void setBoard(Board board) {this.board = board;}

    /**
     * RecyclerView Adapter
     * */

    public static class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

        Context context;
        Board board;
        PieceInterface grids[][];
        boolean suggestionGrid[][];

        private OnItemClickListener onItemClickListener;

        public interface OnItemClickListener {
            public void onClick(View view, int position);
        }

        RecyclerViewAdapter(Context context, Board board){
            this.context = context;
            this.grids = board.getPieces();
            this.board = board;
            this.suggestionGrid = new boolean[board.getSize()][board.getSize()];
            resetSuggestions();
        }

        public void resetSuggestions(){
            for(int i = 0; i < board.getSize(); i++)
                for(int j = 0; j < board.getSize(); j++)
                    suggestionGrid[i][j] = false;
        }

        public void updateSuggestions(boolean suggestionGrid[][]){
            //resetSuggestions();
            this.suggestionGrid = suggestionGrid; //[x][y]=sugg;
        }

        public void setOnItemClickListener(OnItemClickListener onItemClickListener){this.onItemClickListener = onItemClickListener;}

        public void updateGridPosition(int x, int y, Piece piece) {
            grids[x][y] = piece;
        }

        @Override
        public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.piece, parent, false);
            ViewHolder vh = new ViewHolder(view);
            return vh;
        }

        @Override
        public void onBindViewHolder(RecyclerViewAdapter.ViewHolder holder, int position) {
            int x = position / board.getSize();
            int y = position % board.getSize();

            if((x+y) % 2  == 0) holder.imageView.setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryDark));

            Piece piece = (Piece)grids[x][y];
            if(grids[x][y] != null) {
                Log.v("Piece",""+piece.color);
                holder.imageView.setBackgroundColor(piece.color == COLORS.BLACK ?
                        context.getResources().getColor(R.color.colorBlack) : context.getResources().getColor(R.color.colorWhite));
            } else if(suggestionGrid[x][y]){
                holder.imageView.setBackgroundColor(context.getResources().getColor(R.color.colorYellow));
            }
        }

        @Override
        public int getItemCount() {
            return board.getSize() * board.getSize();
        }


        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

            Button imageView;

            public ViewHolder(View itemView) {
                super(itemView);
                imageView = (Button) itemView.findViewById(R.id.btPiece);
                imageView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                if(onItemClickListener != null)
                    onItemClickListener.onClick(view, getAdapterPosition());
            }
        }
    }
}
