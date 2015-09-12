package adammcneilly.capturethetag;

import java.io.Serializable;

/**
 * Created by adammcneilly on 9/12/15.
 */
public class Player{
    private String name;
    private boolean isCaptain;

    public Player(String name){
        this.name = name;
        this.isCaptain = false;
    }

    public String getName() {
        return name;
    }

    public Player(String name, boolean isCaptain){
        this.name = name;
        this.isCaptain = isCaptain;
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
}
