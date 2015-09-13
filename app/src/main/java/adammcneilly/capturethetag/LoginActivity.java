package adammcneilly.capturethetag;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.Firebase;

public class LoginActivity extends AppCompatActivity {
    private EditText mPlayerName;
    private Button mJoinGame;
    private Button mStartGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Firebase.setAndroidContext(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mPlayerName = (EditText) findViewById(R.id.player_name);
        mJoinGame = (Button) findViewById(R.id.join_game);
        mStartGame = (Button) findViewById(R.id.create_game);
        mJoinGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mPlayerName.getText().toString().isEmpty()){
                    mPlayerName.setError("Name cannot be blank.");
                    return;
                } else{
                    Global.currentPlayer = new Player(mPlayerName.getText().toString());
                }

                startGameListActivity();
            }
        });
        mStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mPlayerName.getText().toString().isEmpty()){
                    mPlayerName.setError("Name cannot be blank.");
                    return;
                } else{
                    Global.currentPlayer = new Player(mPlayerName.getText().toString());
                }

                startNewGameActivity();
            }
        });
    }

    private void startGameListActivity(){
        Intent gameListIntent = new Intent(LoginActivity.this, GameListActivity.class);
        startActivity(gameListIntent);
    }

    private void startNewGameActivity(){
        Intent newGameIntent = new Intent(LoginActivity.this, StartGameActivity.class);
        startActivity(newGameIntent);
    }
}
