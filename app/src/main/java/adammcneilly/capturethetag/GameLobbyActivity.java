package adammcneilly.capturethetag;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameLobbyActivity extends AppCompatActivity {
    private ExpandableListView mExpandableListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_lobby);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mExpandableListView = (ExpandableListView) findViewById(R.id.team_player_list_view);
        TeamPlayerAdapter adapter = new TeamPlayerAdapter(this, getTeams(), getTeamPlayers());
        mExpandableListView.setAdapter(adapter);
    }

    private List<Team> getTeams(){
        List<Team> teams = new ArrayList<>();
        teams.add(new Team("Red"));
        teams.add(new Team("Blue"));
        teams.add(new Team("Green"));
        return teams;
    }

    private HashMap<Team, List<Player>> getTeamPlayers(){
        HashMap<Team, List<Player>> teamPlayers = new HashMap<>();
        List<Player> redPlayers = new ArrayList<>();
        List<Player> bluePlayers = new ArrayList<>();
        List<Player> greenPlayers = new ArrayList<>();
        redPlayers.add(new Player("Player", "One"));
        redPlayers.add(new Player("Player", "Two"));
        redPlayers.add(new Player("Player", "Three"));
        bluePlayers.add(new Player("Player", "Four"));
        bluePlayers.add(new Player("Player", "Five"));
        greenPlayers.add(new Player("Player", "Six"));
        teamPlayers.put(new Team("Red"), redPlayers);
        teamPlayers.put(new Team("Blue"), bluePlayers);
        teamPlayers.put(new Team("Green"), greenPlayers);
        return teamPlayers;
    }
}
