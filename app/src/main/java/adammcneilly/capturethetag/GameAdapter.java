package adammcneilly.capturethetag;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import java.sql.Ref;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by adammcneilly on 9/12/15.
 */
public class GameAdapter extends RecyclerView.Adapter<GameAdapter.ViewHolder>{
    private Context mContext;
    private List<Game> mGames;

    public GameAdapter(Context context){
        this.mContext = context;
        this.mGames = new ArrayList<>();
        monitorGames();
    }

    private void monitorGames(){
        Firebase ref = new Firebase(Global.FirebaseURl);
        ref.addChildEventListener(new ChildEventListener() {
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
                mGames.remove(new Game(gameName));
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

    public class ViewHolder extends RecyclerView.ViewHolder{
        public final TextView gameNameTextView;

        public ViewHolder(View view){
            super(view);
            gameNameTextView = (TextView) view.findViewById(R.id.game_name);
        }
    }
}
