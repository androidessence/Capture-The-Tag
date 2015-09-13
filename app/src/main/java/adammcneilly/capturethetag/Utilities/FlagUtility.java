package adammcneilly.capturethetag.Utilities;

import adammcneilly.capturethetag.Global;
import com.firebase.client.Firebase;

/**
 * Created by zadekaakarni on 9/12/15.
 */
public class FlagUtility {

    public void AddFlag(String gameName, String teamName, String flagName, String tagSerial)
    {
        SetFlagCapturedStatus(gameName, teamName, tagSerial, Global.FlagStatus.Not_Captured);
        SetFlagName(gameName, teamName, tagSerial, flagName);
    }

    public void RemoveFlag(String gameName, String teamName, String flagName)
    {
        new Firebase(Global.FirebaseURl).child(gameName).child(teamName).child(Global.FLAGS).child(flagName).removeValue();
    }

    public void SetFlagCapturedStatus(String gameName, String teamName, String tagSerial, Global.FlagStatus capturedStatus)
    {
        new Firebase(Global.FirebaseURl).child(gameName).child(teamName).child(Global.FLAGS).child(tagSerial)
                .child(Global.STATUS).setValue(capturedStatus);
    }

    public void SetFlagName(String gameName, String teamName, String tagSerial, String flagName)
    {
        new Firebase(Global.FirebaseURl).child(gameName).child(teamName).child(Global.FLAGS).child(tagSerial)
                .child(Global.FLAG_NAME).setValue(flagName);
    }
}
