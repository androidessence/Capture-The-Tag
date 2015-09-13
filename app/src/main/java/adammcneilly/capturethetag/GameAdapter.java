package adammcneilly.capturethetag;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.client.Query;

import java.sql.Ref;
import java.util.List;

/**
 * Created by adammcneilly on 9/12/15.
 */
public class GameAdapter extends RecyclerView.Adapter<GameAdapter.ViewHolder>{
    private Context mContext;
    private List<Game> mGames;

    public GameAdapter(Context context, List<Game> games){
        this.mContext = context;
        this.mGames = games;
    }

    private void monitorGames(){
        Firebase ref = new Firebase(Global.FirebaseURl);
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
