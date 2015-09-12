package adammcneilly.capturethetag.Utilities;

import adammcneilly.capturethetag.Global;
import com.firebase.client.Firebase;

/**
 * Created by zadekaakarni on 9/12/15.
 */
public class FlagUtility {

    public void AddFlag(String gameName, String teamName, String flagName)
    {
        SetFlagCapturedStatus(gameName, teamName, flagName, Global.FlagStatus.Not_Captured);
    }

    public void RemoveFlag(String gameName, String teamName, String flagName)
    {
        new Firebase(Global.FirebaseURl).child(gameName).child(teamName).child(FLAGS).child(flagName).removeValue();
    }

    public void SetFlagCapturedStatus(String gameName, String teamName, String flagName, Global.FlagStatus capturedStatus)
    {
        new Firebase(Global.FirebaseURl).child(gameName).child(teamName).child(FLAGS).child(flagName)
                .child(STATUS).setValue(capturedStatus);
    }

    public static String FLAGS = "flags";
    public static String STATUS = "status";
}
