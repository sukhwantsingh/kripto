package braille.kofefe.app.modules_.Invite_.fusedlocationapi;

/**
 * Created by Snow-Dell-05 on 4/25/2017.
 */

public class ReservedLocation {
    private static ReservedLocation objSingleton = null;

    private String curret_lat;
    private String current_lng;


    private ReservedLocation() {
    }

    public static ReservedLocation getSingletonInstance() {

        if (objSingleton == null) {
            objSingleton = new ReservedLocation();
        }
        return objSingleton;
    }

    public String getCurret_lat() {
        return objSingleton.curret_lat;
    }

    public void setCurret_lat(String curret_lat) {
        objSingleton.curret_lat = curret_lat;
    }

    public String getCurrent_lng() {
        return objSingleton.current_lng;
    }

    public void setCurrent_lng(String current_lng) {
        objSingleton.current_lng = current_lng;
    }

}
