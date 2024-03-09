package beans;

public class BasicService extends Node {
    public BasicService(TCMap TCMap) {
        setTCMap(TCMap);
    }

    @Override
    public TCMap calculateTCMap() {
        return super.getTCMap();
    }

}
