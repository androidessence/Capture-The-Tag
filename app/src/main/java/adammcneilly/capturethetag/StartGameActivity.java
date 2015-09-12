package adammcneilly.capturethetag;

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
        mAddTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEditText();
            }
        });
    }

    private void getUIElements(){
        mTeamLayout = (LinearLayout) findViewById(R.id.team_layout);
        mTeamEditTexts.add((EditText) findViewById(R.id.team_1_name));
        mTeamEditTexts.add((EditText) findViewById(R.id.team_2_name));
        mAddTeam = (Button) findViewById(R.id.add_team);
    }

    private void addEditText(){
        // The new team's id is the size of mTeamEditTexts + 1.
        int newTeamNumber = mTeamEditTexts.size() + 1;

        // Create TIL and set width to match_parent and height to wrap_content
        TextInputLayout textInputLayout = new TextInputLayout(this);
        textInputLayout.setLayoutParams(
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));

        // Create ET and set width to match_parent and height to match_parent.
        // The hint is 'team x name'
        EditText editText = new EditText(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(0, 0, 0, 16);
        editText.setLayoutParams(layoutParams);
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        editText.setHint("Team " + newTeamNumber + " name");

        mTeamEditTexts.add(editText);
        textInputLayout.addView(editText);
        mTeamLayout.addView(textInputLayout);
    }
}
