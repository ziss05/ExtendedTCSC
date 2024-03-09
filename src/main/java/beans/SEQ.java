package beans;

import org.apache.poi.ss.formula.functions.T;

import java.util.*;

public class SEQ extends Constructor{
    public SEQ(Node leftChild, Node rightChild) {
        super(leftChild, rightChild);
    }

    private Set<Quadruple> concat(Quadruple a, Quadruple b) throws TimeoutException {
        exceededTimeout();
        TreeSet<Quadruple> concats = new TreeSet<>();
        boolean isNull = false;
        if (a.getGradient() == null || b.getGradient() == null) {
            isNull = true;
        }

        if (!isNull && a.getGradient().compareTo(b.getGradient()) <0) {

            LinkedHashMap<Quadruple, ValidTuple> prov = new LinkedHashMap<>();
            Quadruple x= new Quadruple(a);
            prov.put(x,new ValidTuple(a.getD1(),a.getD2()));
            Quadruple y= new Quadruple(b);
            prov.put(y,new ValidTuple(b.getD1(),b.getD1()));

            Quadruple quadruple1 = new Quadruple(getClass().getSimpleName(),a.getD1().add(b.getD1()), addCostValues(a.getC1(), b.getC1()),
                    a.getD2().add(b.getD1()), addCostValues(a.getC2(), b.getC1()),prov);
            concats.add(quadruple1);

            LinkedHashMap<Quadruple, ValidTuple> prov2 = new LinkedHashMap<>();
            Quadruple qa= new Quadruple(a);
            prov2.put(qa,new ValidTuple(a.getD2(),a.getD2()));
            Quadruple qb= new Quadruple(b);
            prov2.put(qb,new ValidTuple(b.getD1(),b.getD2()));

            Quadruple quadruple2 = new Quadruple(getClass().getSimpleName(),a.getD2().add(b.getD1()), addCostValues(a.getC2(), b.getC1()),
                    a.getD2().add(b.getD2()), addCostValues(a.getC2(), b.getC2()),prov2);
            concats.add(quadruple2);

        } else {
            LinkedHashMap<Quadruple, ValidTuple> prov = new LinkedHashMap<>();
            Quadruple x= new Quadruple(a);
            prov.put(x,new ValidTuple(a.getD1(),a.getD1()));
            Quadruple y= new Quadruple(b);
            prov.put(y,new ValidTuple(b.getD1(),b.getD2()));
            concats.add(new Quadruple(getClass().getSimpleName(),a.getD1().add(b.getD1()), addCostValues(a.getC1(), b.getC1()),
                    a.getD1().add(b.getD2()), addCostValues(a.getC1(), b.getC2()),prov));

            LinkedHashMap<Quadruple, ValidTuple> prov2 = new LinkedHashMap<>();
            Quadruple qa= new Quadruple(a);
            prov2.put(qa,new ValidTuple(a.getD1(),a.getD2()));
            Quadruple qb= new Quadruple(b);
            prov2.put(qb,new ValidTuple(b.getD2(),b.getD2()));
            concats.add(new Quadruple(getClass().getSimpleName(),a.getD1().add(b.getD2()), addCostValues(a.getC1(), b.getC2()),
                    a.getD2().add(b.getD2()), addCostValues(a.getC2(), b.getC2()),prov2));
        }

        return concats;
    }

    @Override
    public TCMap calculateTCMap() throws TimeoutException {
        TCMap a=getLeftChild().calculateTCMap();
        TCMap b=getRightChild().calculateTCMap();
        if (a == null || b == null || !a.isFunctional() || !b.isFunctional()) {
            return null;
        }
        TCMap result = new TCMap();
        TreeSet<Quadruple> aSet = (TreeSet<Quadruple>) a.getTCMap();
        TreeSet<Quadruple> bSet = (TreeSet<Quadruple>) b.getTCMap();
        exceededTimeout();
        for (Quadruple qa : aSet) {
            for (Quadruple qb : bSet) {
                result.addAll(concat(qa, qb));
            }
        }
        return super.compress(result);
    }
}
