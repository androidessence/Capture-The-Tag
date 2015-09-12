package adammcneilly.capturethetag;

/**
 * Created by zadekaakarni on 9/12/15.
 */
public class Global {

    public static String FirebaseURl = "https://capture-the-tag.firebaseio.com/";
    public static final String IS_CAPTAIN = "IsCaptain";

    // keys in firebase
    public static String PLAYERS = "players";
    public static String IS_CAPTAIN = "IsCaptain";
    public static String FLAGS = "flags";
    public static String STATUS = "status";
    public static String SERIAL = "serial";

    public enum FlagStatus
    {
        Not_Captured,
        In_Progress,
        Captured
    }

    public static String ByteArrToSerial(byte[] arr)
    {
        String temp = "";
        for (int i = 0; i < arr.length; i++)
        {
            temp += String.format("%02X", arr[i] & 0xFF);
            if(!(i == arr.length - 1))
                temp += ":";
        }
        return temp;
    }
}
