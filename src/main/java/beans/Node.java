package beans;

import java.util.LinkedHashMap;

public abstract class Node {
    private TCMap tcMap;
    private static long startTime;
    private static long benchmark = 0;

    public static void setStartTime(long startTime) {
        Node.startTime = startTime;
    }

    public static void setBenchmark(long benchmark) {
        Node.benchmark = benchmark;
    }


    protected static void exceededTimeout() throws TimeoutException {
        if (benchmark != 0 && System.nanoTime() - startTime >= benchmark) {
            throw new TimeoutException("Benchmark was reached!");
        }
    }

    public TCMap getTCMap(){
        if (tcMap==null){
            return null;
        }
        return tcMap;
    }

    protected void setTCMap(TCMap tcMap) {
        if (tcMap==null){
            tcMap=null;
        }
        else this.tcMap = new TCMap(tcMap);
    }

    public abstract TCMap calculateTCMap() throws TimeoutException;

    @Override
    public String toString() {
        if (getTCMap()==null){
            return "Node{" + this.getClass().getSimpleName() +
                    ": tcMap= null}";
        }
        return "Node{" + this.getClass().getSimpleName() +
                ": tcMap= " + tcMap.toString() + "}";
    }
}
