package adammcneilly.capturethetag.Utilities;

import adammcneilly.capturethetag.Global;
import com.firebase.client.Firebase;

/**
 * Created by zadekaakarni on 9/12/15.
 */
public class FlagUtility {

    public void AddFlag(String gameName, String teamName, String flagName, String tagSerial)
    {
        SetFlagCapturedStatus(gameName, teamName, flagName, Global.FlagStatus.Not_Captured);
        SetSerial(gameName, teamName, flagName, tagSerial);
    }

    public void RemoveFlag(String gameName, String teamName, String flagName)
    {
        new Firebase(Global.FirebaseURl).child(gameName).child(teamName).child(Global.FLAGS).child(flagName).removeValue();
    }

    public void SetFlagCapturedStatus(String gameName, String teamName, String flagName, Global.FlagStatus capturedStatus)
    {
        new Firebase(Global.FirebaseURl).child(gameName).child(teamName).child(Global.FLAGS).child(flagName)
                .child(Global.STATUS).setValue(capturedStatus);
    }

    public void SetSerial(String gameName, String teamName, String flagName, String strSerial)
    {
        new Firebase(Global.FirebaseURl).child(gameName).child(teamName).child(Global.FLAGS).child(flagName)
                .child(Global.SERIAL).setValue(strSerial);
    }
}
