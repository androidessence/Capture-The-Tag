package adammcneilly.capturethetag;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class StartGameActivity extends AppCompatActivity {
    private LinearLayout mTeamLayout;
    private List<EditText> mTeamEditTexts =  new ArrayList<>();
    private Button mAddTeam;
    private Button mSubmit;
    private EditText mGameName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_game);

        // Setup toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Get UI elements
        getUIElements();

        // Set click listener on button
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame();
            }
        });

        // Commented out. This is a stretch goal.
//        mAddTeam.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                addEditText();
//            }
//        });
    }

    /**
     * Retrieves all necessary UI elements for this activity.
     */
    private void getUIElements(){
        mTeamLayout = (LinearLayout) findViewById(R.id.team_layout);
        mTeamEditTexts.add((EditText) findViewById(R.id.team_1_name));
        mTeamEditTexts.add((EditText) findViewById(R.id.team_2_name));
        mAddTeam = (Button) findViewById(R.id.add_team);
        mSubmit = (Button) findViewById(R.id.submit);
        mGameName = (EditText) findViewById(R.id.game_name);
    }

    /**
     * Called when the submit button is clicked.
     */
    private void startGame(){
        if(!validateInput()){
            return;
        }

        //TODO: Do something with teams.
        Intent gameLobbyIntent = new Intent(StartGameActivity.this, GameLobbyActivity.class);
        gameLobbyIntent.putExtra(GameLobbyActivity.ARG_GAME, mGameName.getText().toString());
        String[] teamNames = new String[mTeamEditTexts.size()];
        for(int i = 0; i < teamNames.length; i++){
            teamNames[i] = mTeamEditTexts.get(i).getText().toString();
        }
        gameLobbyIntent.putExtra(GameLobbyActivity.ARG_TEAMS, teamNames);
        startActivity(gameLobbyIntent);
    }

    /**
     * Validates the input of team names and game name.
     * @return False if any EditTexts are blank.
     */
    private boolean validateInput(){
        boolean isValid = true;

        if(mGameName.getText().toString().isEmpty()){
            mGameName.setError("Name cannot be blank.");
            isValid = false;
        }

        // Because Adam is too lazy to care, user cannot have any empty EditTexts.
        for(EditText teamEditText : mTeamEditTexts){
            if(teamEditText.getText().toString().isEmpty()){
                teamEditText.setError("Name cannot be blank.");
                isValid = false;
            }
        }

        return isValid;
    }

    /**
     * Adding of teams has been commented out as a stretch goal.
     */
//    private void addEditText(){
//        // The new team's id is the size of mTeamEditTexts + 1.
//        int newTeamNumber = mTeamEditTexts.size() + 1;
//
//        // Create TIL and set width to match_parent and height to wrap_content
//        TextInputLayout textInputLayout = new TextInputLayout(this);
//        textInputLayout.setLayoutParams(
//                new LinearLayout.LayoutParams(
//                        LinearLayout.LayoutParams.MATCH_PARENT,
//                        LinearLayout.LayoutParams.WRAP_CONTENT));
//
//        // Create ET and set width to match_parent and height to match_parent.
//        // The hint is 'team x name'
//        EditText editText = new EditText(this);
//        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
//        layoutParams.setMargins(0, 0, 0, 16);
//        editText.setLayoutParams(layoutParams);
//        editText.setInputType(InputType.TYPE_CLASS_TEXT);
//        editText.setHint("Team " + newTeamNumber + " name");
//
//        mTeamEditTexts.add(editText);
//        textInputLayout.addView(editText);
//        mTeamLayout.addView(textInputLayout);
//    }
}
