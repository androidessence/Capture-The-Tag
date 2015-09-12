package adammcneilly.capturethetag;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.firebase.client.Firebase;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Firebase.setAndroidContext(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_flag_write:
                startFlagWriteActivity();
            case R.id.action_start_game:
                startStartGameActivity();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Starts the activity to write to a flag.
     */
    private void startFlagWriteActivity(){
        Intent flagWriteIntent = new Intent(MainActivity.this, FlagWriteActivity.class);
        startActivity(flagWriteIntent);
    }

    /**
     * Starts the Activity used to start a game.
     */
    private void startStartGameActivity(){
        Intent startGameIntent = new Intent(MainActivity.this, StartGameActivity.class);
        startActivity(startGameIntent);
    }
}
