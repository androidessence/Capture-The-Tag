package adammcneilly.capturethetag;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import java.lang.reflect.Array;
import java.sql.Ref;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by adammcneilly on 9/12/15.
 */
public class GameAdapter extends RecyclerView.Adapter<GameAdapter.ViewHolder>{
    private Context mContext;
    private List<Game> mGames;
    private Firebase mFirebase;

    public GameAdapter(Context context){
        this.mContext = context;
        this.mGames = new ArrayList<>();
        monitorGames();
    }

    private void monitorGames(){
        mFirebase = new Firebase(Global.FirebaseURl);
        mFirebase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String gameName = dataSnapshot.getKey();
                mGames.add(new Game(gameName));
                notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String gameName = dataSnapshot.getKey();
                for (Game game : mGames) {
                    if (game.getName().equals(gameName)) {
                        mGames.remove(game);
                        break;
                    }
                }
                notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_game, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Game game = mGames.get(position);
        holder.gameNameTextView.setText(game.getName());
    }

    @Override
    public int getItemCount() {
        return mGames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final TextView gameNameTextView;
        public final Button joinGameButton;

        public ViewHolder(View view){
            super(view);
            gameNameTextView = (TextView) view.findViewById(R.id.game_name);
            joinGameButton = (Button) view.findViewById(R.id.join_game);
            joinGameButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Game game = mGames.get(getAdapterPosition());
            Intent gameLobbyIntent = new Intent(mContext, GameLobbyActivity.class);
            gameLobbyIntent.putExtra(GameLobbyActivity.ARG_GAME, game.getName());

            // Get teamNames
            final List<String> teamNames = new ArrayList<>();
            Query fbQuery = mFirebase.child(game.getName()).orderByKey();
            fbQuery.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    teamNames.add(dataSnapshot.getKey());
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });

            mContext.startActivity(gameLobbyIntent);
        }
    }
}
