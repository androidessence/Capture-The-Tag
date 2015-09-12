package adammcneilly.capturethetag.Utilities;

import com.firebase.client.Firebase;

import java.util.List;

import adammcneilly.capturethetag.Global;
import adammcneilly.capturethetag.Team;

/**
 * Created by adammcneilly on 9/12/15.
 */
public class GameUtility {
    public void addGame(String gameName, List<Team> teams){
        Firebase ref = new Firebase(Global.FirebaseURl);
        ref.child(gameName).push();
        for(Team team : teams){
            //TODO:
        }
    }
}
