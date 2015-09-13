package adammcneilly.capturethetag.Utilities;

import android.provider.ContactsContract;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import adammcneilly.capturethetag.Global;

/**
 * Created by zadekaakarni on 9/12/15.
 */
public class PlayerUtility {

    public void AddPlayer(String gameName, String teamName, String playerName)
    {
        SetIsPlayerCaptain(gameName, teamName, playerName, false);
    }

    public void AddPlayerAsCaptain(String gameName, String teamName, String playerName)
    {
        SetIsPlayerCaptain(gameName, teamName, playerName, true);
    }

    public void RemovePlayer(String gameName, String teamName, String playerName)
    {
        new Firebase(Global.FirebaseURl).child(gameName).child(teamName).child(Global.PLAYERS).child(playerName).removeValue();
    }

    public void SetIsPlayerCaptain(String gameName, String teamName, String playerName, boolean IsCaptain)
    {
        new Firebase(Global.FirebaseURl).child(gameName).child(teamName).child(Global.PLAYERS).child(playerName)
                .child(Global.IS_CAPTAIN).setValue(IsCaptain);
    }

    // Returns gameName,teamName,flagName of the flag the user has captured
    public String GetPlayersCapturedFlagInfo(String gameName, String teamName, String playerName)
    {
        String temp;
        Firebase ref = new Firebase(Global.FirebaseURl).child(gameName).child(teamName).child(playerName);
        Query refQ = ref.orderByKey().equalTo("flag");
        refQ.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                DataCarriage(dataSnapshot);
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

        try {
            temp = this.thisIsTotallyOkayToDo.getValue().toString();
        } catch (Exception e) {
            temp = "";
        }

        this.thisIsTotallyOkayToDo = null;

        return temp;
    }

    private DataSnapshot thisIsTotallyOkayToDo;

    private void DataCarriage(DataSnapshot temp)
    {
        this.thisIsTotallyOkayToDo = temp;
    }

    public void RemoveCapturedFlagFromUser(String gameName, String teamName, String playerName)
    {
        new Firebase(Global.FirebaseURl).child(gameName).child(teamName).child(playerName).child(Global.FLAG).removeValue();
    }
}
