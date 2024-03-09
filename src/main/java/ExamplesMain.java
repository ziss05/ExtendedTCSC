import beans.*;
import com.github.kiprobinson.bigfraction.BigFraction;


public class ExamplesMain {
    public static void main(String[] args) {
        TCMap transfer=new TCMap();
        transfer.add(new Quadruple("T",1,30,12,30));
        transfer.add(new Quadruple("T",12,15,24,15));
        TCMap analyticalA=new TCMap();
        analyticalA.add(new Quadruple("A",1,50,2,50));
        analyticalA.add(new Quadruple("A",2,52,12,72));
        TCMap analyticalB=new TCMap();
        analyticalB.add(new Quadruple("B",1,30,2,30));
        analyticalB.add(new Quadruple("B",2,40,3,40));
        analyticalB.add(new Quadruple("B",3,45,15,105));
        TCMap monitorSmall=new TCMap();
        monitorSmall.add(new Quadruple("MS",1,5,12,60));
        monitorSmall.add(new Quadruple("MS",12,65,24,185));
        TCMap monitorBig=new TCMap();
        monitorBig.add(new Quadruple("MB",1,30,20,600));


        XORC c = new XORC(new BasicService(monitorSmall), new BasicService(monitorBig));
        XORD d= new XORD(new BasicService(analyticalA),new BasicService(analyticalB));
        AND a= new AND(c,d);
        SEQ comp=new SEQ(new BasicService(transfer),a);
        try {
            System.out.println(comp.calculateTCMap());
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}
