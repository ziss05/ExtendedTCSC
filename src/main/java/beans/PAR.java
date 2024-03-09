package beans;

import com.github.kiprobinson.bigfraction.BigFraction;

import java.util.LinkedHashMap;
import java.util.TreeSet;

public abstract class PAR extends Constructor{
    public PAR(Node leftChild, Node rightChild) {
        super(leftChild, rightChild);
    }

    abstract TCMap[] getTCMaps() throws TimeoutException;

    @Override
    public TCMap calculateTCMap() throws TimeoutException {
        TCMap leftNode=getLeftChild().calculateTCMap();
        TCMap rightNode=getRightChild().calculateTCMap();

        if (leftNode == null || rightNode == null || !leftNode.isFunctional() || !rightNode.isFunctional()) {
            return null;
        }

        TCMap[] tcMaps=getTCMaps();
        TCMap a = tcMaps[0];
        TCMap b = tcMaps[1];
        exceededTimeout();


        TCMap result = new TCMap();
        TreeSet<Tuple> intervals = new TreeSet<>();
        TreeSet<BigFraction> points = getCombinedPointSet(a, b);
        intervals.addAll(getIntervalSet(points));

        BigFraction c1;
        BigFraction c2;
        CostOrigin[] costsA;
        CostOrigin[] costsB;
        for (Tuple t : intervals) {
            exceededTimeout();
            costsA=selectCost(t.getLow(),t.getUp(),a,true);
            costsB=selectCost(t.getLow(),t.getUp(),b,true);
            c1 = addCostValues(costsA[0].getCost(), costsB[0].getCost());
            c2 = addCostValues(costsA[1].getCost(), costsB[1].getCost());
            if (c1 == null || c2 == null) {
                c1 = null;
                c2 = null;
            }
            //provenance part
            LinkedHashMap<Quadruple, ValidTuple> prov = new LinkedHashMap<>();

            Quadruple qa=costsA[0].getQuadruples().get(0);
            if (qa == null) {
                prov.put(null,null);
            } else {
                prov.put(qa,new ValidTuple(t.getLow(),t.getUp()));
            }
            Quadruple qb=costsB[0].getQuadruples().get(0);
            if (qb == null) {
                prov.put(null,null);
            } else {
                prov.put(qb,new ValidTuple(t.getLow(),t.getUp()));
            }

            result.add(new Quadruple(getClass().getSimpleName(),t.getLow(), c1, t.getUp(), c2, prov));
        }
        return compress(result);
    }
}
