package adammcneilly.capturethetag;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameLobbyActivity extends AppCompatActivity {
    private ExpandableListView mExpandableListView;
    private Firebase ref;
    private String mGameName;
    private List<Team> mTeams = new ArrayList<>();
    private TeamPlayerAdapter mAdapter;

    public static final String ARG_GAME = "game";
    public static final String ARG_TEAMS = "teams";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_lobby);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Get game name and team names
        mGameName = getIntent().getStringExtra(ARG_GAME);
        String[] teams = getIntent().getStringArrayExtra(ARG_TEAMS);
        for(String teamName : teams){
            mTeams.add(new Team(teamName));
        }

        setTitle(mGameName + " Game Lobby");

        // Setup Firebase connection
        ref = new Firebase(Global.FirebaseURl);
        // Not in this method but
        // for each team InitTeamListener
        for(Team team : mTeams){
            InitTeamListener(team.getName());
        }

        mExpandableListView = (ExpandableListView) findViewById(R.id.team_player_list_view);
        mAdapter = new TeamPlayerAdapter(this, mTeams, mGameName);
        mExpandableListView.setAdapter(mAdapter);
    }

    private void InitTeamListener(final String teamName) {
        Firebase refWithPlayers = ref.child(mGameName).child(teamName).child(Global.PLAYERS);
        refWithPlayers.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                // Pull data from datasnapshot
                // Add player list item for each player returned
                String name = dataSnapshot.getKey();

                String isCaptain = dataSnapshot.child(Global.IS_CAPTAIN).getValue().toString();
                mAdapter.insertPlayer(new Team(teamName), new Player(name, Boolean.parseBoolean(isCaptain)));
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
    }
}
