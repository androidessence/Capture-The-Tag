package adammcneilly.capturethetag;

/**
 * Created by zadekaakarni on 9/12/15.
 */
public class Global {

    public static String FirebaseURl = "https://capture-the-tag.firebaseio.com/";
    public static final String IS_CAPTAIN = "IsCaptain";

    public static Player currentPlayer;



    public enum FlagStatus
    {
        Not_Captured,
        In_Progress,
        Captured
    }
}
