package adammcneilly.capturethetag;

/**
 * Created by adammcneilly on 9/13/15.
 */
public class Flag {
    private String name;
    private String serialNumber;
    private Global.FlagStatus status;

    public Flag(String name, String serialNumber, Global.FlagStatus flagStatus){
        this.name = name;
        this.serialNumber = serialNumber;
        this.status = flagStatus;
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
