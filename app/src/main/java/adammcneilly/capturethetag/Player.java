package adammcneilly.capturethetag;

import java.io.Serializable;

/**
 * Created by adammcneilly on 9/12/15.
 */
public class Player{
    private String firstName;
    private String lastName;

    public Player(String firstName, String lastName){
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getName(){
        return firstName + " " + lastName;
    }
}
