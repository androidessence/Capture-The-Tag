package adammcneilly.capturethetag;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.firebase.client.Firebase;
import com.firebase.client.Query;
import com.getbase.floatingactionbutton.AddFloatingActionButton;

import java.util.List;

public class GameListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView gameRecyclerView = (RecyclerView) findViewById(R.id.game_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        gameRecyclerView.setLayoutManager(layoutManager);

        GameAdapter gameAdapter = new GameAdapter(this);
        gameRecyclerView.setAdapter(gameAdapter);

        AddFloatingActionButton fab = (AddFloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startGameIntent = new Intent(GameListActivity.this, StartGameActivity.class);
                startActivity(startGameIntent);
            }
        });
    }
}
