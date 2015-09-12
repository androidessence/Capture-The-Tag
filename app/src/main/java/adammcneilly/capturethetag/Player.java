package adammcneilly.capturethetag;

import java.io.Serializable;

/**
 * Created by adammcneilly on 9/12/15.
 */
public class Player{
    private String name;

    public Player(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
