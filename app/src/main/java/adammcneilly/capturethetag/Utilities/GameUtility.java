package adammcneilly.capturethetag.Utilities;

import com.firebase.client.Firebase;

import java.util.List;

import adammcneilly.capturethetag.Global;
import adammcneilly.capturethetag.Team;
import java.lang.IllegalArgumentException;

/**
 * Created by adammcneilly on 9/12/15.
 */
public class GameUtility {

    public void InitGameAndTeams(String gameName, List<Team> teams){
        if (teams.size() < 2)
            throw new IllegalArgumentException("Yo, it needs to be at least two teams");

        Firebase gameRef = new Firebase(Global.FirebaseURl).child(gameName);
        for (Team t : teams)
            gameRef.child(t.getName()).setValue("temp");
    }
}
