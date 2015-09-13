package adammcneilly.capturethetag;

import java.io.Serializable;

/**
 * Created by adammcneilly on 9/12/15.
 */
public class Player{
    private String name;
    private boolean isCaptain;
    private String teamName;

    public Player(String name){
        this.name = name;
        this.isCaptain = false;
        this.teamName = "";
    }

    public String getName() {
        return name;
    }

    public Player(String name, boolean isCaptain, String teamName){
        this.name = name;
        this.isCaptain = isCaptain;
        this.teamName = teamName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCaptain() {
        return isCaptain;
    }

    public void setIsCaptain(boolean isCaptain) {
        this.isCaptain = isCaptain;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
}
