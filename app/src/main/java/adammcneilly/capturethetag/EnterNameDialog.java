package adammcneilly.capturethetag;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import adammcneilly.capturethetag.Utilities.PlayerUtility;

/**
 * Created by adammcneilly on 9/12/15.
 */
public class EnterNameDialog extends DialogFragment {
    private EditText mPlayerName;
    private Button mCancel;
    private Button mSubmit;
    private TextView mTitle;
    private String mTeamName;
    private String mGameName;
    private boolean mCaptain;

    public static final String ARG_TEAM = "team";
    public static final String ARG_GAME = "game";
    public static final String ARG_CAPTAIN = "captain";

    public static EnterNameDialog NewInstance(String teamName, String gameName, boolean isCaptain){
        EnterNameDialog dialogFragment = new EnterNameDialog();
        Bundle args = new Bundle();
        args.putString(ARG_TEAM, teamName);
        args.putString(ARG_GAME, gameName);
        args.putBoolean(ARG_CAPTAIN, isCaptain);
        dialogFragment.setArguments(args);
        return dialogFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_enter_name, container, false);

        getUIElements(view);

        mTitle.setText("Join Team " + mTeamName);

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitPlayer();
            }
        });

        return view;
    }

    private void submitPlayer(){
        if (mPlayerName.getText().toString().isEmpty()) {
            mPlayerName.setError("Name cannot be blank.");
            return;
        }

        Global.currentPlayer = new Player(mPlayerName.getText().toString(), mCaptain, mTeamName);
        if(mCaptain){
            new PlayerUtility().AddPlayerAsCaptain(mGameName, mTeamName, mPlayerName.getText().toString());
        } else{
            new PlayerUtility().AddPlayer(mGameName, mTeamName, mPlayerName.getText().toString());
        }
        dismiss();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTeamName = getArguments().getString(ARG_TEAM);
        mGameName = getArguments().getString(ARG_GAME);
        mCaptain = getArguments().getBoolean(ARG_CAPTAIN);
    }

    private void getUIElements(View view){
        mPlayerName = (EditText) view.findViewById(R.id.player_name);
        mCancel = (Button) view.findViewById(R.id.cancel);
        mSubmit = (Button) view.findViewById(R.id.submit);
        mTitle = (TextView) view.findViewById(R.id.title);
    }
}
