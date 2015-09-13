package adammcneilly.capturethetag;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;
import java.util.List;

public class GameLobbyActivity extends AppCompatActivity {
    private ExpandableListView mExpandableListView;
    private Firebase ref;
    private String mGameName;
    private List<Team> mTeams = new ArrayList<>();
    private TeamPlayerAdapter mAdapter;
    private Button mStart;

    public static final String ARG_GAME = "game";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_lobby);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Get game name and team names
        mGameName = getIntent().getStringExtra(ARG_GAME);

        setTitle(mGameName + " Game Lobby");

        // Setup Firebase connection
        ref = new Firebase(Global.FirebaseURl);
        InitTeamListener();

        mExpandableListView = (ExpandableListView) findViewById(R.id.team_player_list_view);
        mAdapter = new TeamPlayerAdapter(this, mTeams, mGameName);
        mExpandableListView.setAdapter(mAdapter);
        mStart = (Button) findViewById(R.id.start);
        mStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // First make sure all teams have at least one player
                for(int i = 0; i < mAdapter.getGroupCount(); i++){
                    if(mAdapter.getChildrenCount(i) == 0){
                        Toast.makeText(getApplicationContext(), "Each team must have one player.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(Global.currentPlayer != null && Global.currentPlayer.isCaptain()){
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_game_lobby, menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    private void InitTeamListener(){
        Firebase gameRef = ref.child(mGameName);
        gameRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                mAdapter.insertTeam(new Team(dataSnapshot.getKey()));
                InitTeamPlayerListener(dataSnapshot.getKey());
                mAdapter.notifyDataSetChanged();
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

    private void InitTeamPlayerListener(final String teamName) {
        Firebase refWithPlayers = ref.child(mGameName).child(teamName).child(Global.PLAYERS);
        refWithPlayers.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                // Pull data from datasnapshot
                // Add player list item for each player returned
                String name = dataSnapshot.getKey();
                String isCaptain = dataSnapshot.child(Global.IS_CAPTAIN).getValue().toString();
                mAdapter.insertPlayer(new Team(teamName), new Player(name, Boolean.parseBoolean(isCaptain), teamName));
                invalidateOptionsMenu();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.getKey();
                mAdapter.removePlayer(new Team(teamName), new Player(name));
                invalidateOptionsMenu();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_set_flag:
                Intent flagWriteIntent = new Intent(GameLobbyActivity.this, FlagWriteActivity.class);
                flagWriteIntent.putExtra(FlagWriteActivity.ARG_GAME, mGameName);
                flagWriteIntent.putExtra(FlagWriteActivity.ARG_TEAM, Global.currentPlayer.getTeamName());
                startActivity(flagWriteIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
