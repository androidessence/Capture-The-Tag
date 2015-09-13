package adammcneilly.capturethetag;

/**
 * Created by adammcneilly on 9/13/15.
 */
public class Flag {
    private String name;
    private String serialNumber;
    private Global.FlagStatus status;
    private String teamName;

    public Flag(String name, String serialNumber, Global.FlagStatus flagStatus){
        this.name = name;
        this.serialNumber = serialNumber;
        this.status = flagStatus;
    }

    public Flag(String name, String serialNumber, Global.FlagStatus flagStatus, String teamName){
        this.name = name;
        this.serialNumber = serialNumber;
        this.status = flagStatus;
        this.teamName = teamName;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Global.FlagStatus getStatus() {
        return status;
    }

    public void setStatus(Global.FlagStatus status) {
        this.status = status;
    }
}
