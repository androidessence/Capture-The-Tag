package adammcneilly.capturethetag.Utilities;

import com.firebase.client.Firebase;

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
        new Firebase(Global.FirebaseURl).child(gameName).child(teamName).child(PLAYERS).child(playerName).removeValue();
    }

    public void SetIsPlayerCaptain(String gameName, String teamName, String playerName, boolean IsCaptain)
    {
        new Firebase(Global.FirebaseURl).child(gameName).child(teamName).child(PLAYERS).child(playerName)
                .child(IS_CAPTAIN).setValue(IsCaptain);
    }

    private static String PLAYERS = "players";
    private static String IS_CAPTAIN = "IsCaptain";
}
