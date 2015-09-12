package adammcneilly.capturethetag;

import java.io.Serializable;

/**
 * Created by adammcneilly on 9/12/15.
 */
public class Team{
    private String name;

    public Team(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Team)) {
            return false;
        } else{
            return this.name.equalsIgnoreCase(((Team)o).getName());
        }
    }
}
