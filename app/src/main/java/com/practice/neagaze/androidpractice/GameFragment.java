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

import entities.Board;
import entities.COLORS;
import entities.Game;
import entities.Piece;
import entities.PieceInterface;
import exceptions.InvalidMoveException;
import exceptions.InvalidRuleException;
import exceptions.WrongPersonException;

/**
 * Created by neaGaze on 8/15/17.
 */

public class GameFragment extends Fragment {

    private Board board;
    private Game game;

    public GameFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        game = (Game)getArguments().getSerializable("GAME");
        board = game.getBoard();

        View view = inflater.inflate(R.layout.game_fragment, container, false);
        RecyclerView boardRecyclerView = (RecyclerView)view.findViewById(R.id.boardRecyclerView);
        boardRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), board.getSize()));
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getActivity(), board);
        adapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                Log.e("clicked at",""+position);
                int x = position / board.getSize();
                int y = position % board.getSize();
                boolean hasErrorInMove = false;
                try {
                    game.addPiece(x, y);
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
                    Log.d("move status","Error: "+hasErrorInMove);
                    if(!hasErrorInMove) {
                        Piece p = (Piece) game.getBoard().getPiece(x, y).flip();
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

        private OnItemClickListener onItemClickListener;

        public interface OnItemClickListener {
            public void onClick(View view, int position);
        }

        RecyclerViewAdapter(Context context, Board board){
            this.context = context;
            this.grids = board.getPieces();
            this.board = board;
        }

        public void setOnItemClickListener(OnItemClickListener onItemClickListener){this.onItemClickListener = onItemClickListener;}

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

            if(grids[x][y] != null) {
                Piece piece = (Piece)grids[x][y];
                Log.e("Piece",""+piece.color);
                holder.imageView.setBackgroundColor(piece.color == COLORS.BLACK ?
                        context.getResources().getColor(R.color.colorBlack) : context.getResources().getColor(R.color.colorWhite));
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
