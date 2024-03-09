package beans;

import com.github.kiprobinson.bigfraction.BigFraction;

import java.util.*;

public abstract class XOR extends Constructor {

    abstract CostOrigin[] selectXORCost(BigFraction d1, BigFraction d2, TCMap t) throws TimeoutException;

    abstract BigFraction selectMinOrMax(BigFraction d1, BigFraction d2);
    abstract Map<Quadruple, ValidTuple> selectProvenance(CostOrigin[] costsA, CostOrigin[] costsB, BigFraction d1, BigFraction c1, BigFraction d2, BigFraction c2);

    public XOR(Node leftChild, Node rightChild) {
        super(leftChild, rightChild);
    }

    public BigFraction getMin(BigFraction a, BigFraction b) {
        if (a == null && b == null) {
            return null;
        } else if (a == null) {
            return b;
        } else if (b == null) {
            return a;
        } else return a.compareTo(b) <= 0 ? a : b;
    }

    public BigFraction getMax(BigFraction a, BigFraction b) {
        if (a == null || b == null) {
            return null;
        }
        return a.compareTo(b) >= 0 ? a : b;
    }

    @Override
    public final TCMap calculateTCMap() throws TimeoutException {
        TCMap a = getLeftChild().calculateTCMap();
        TCMap b = getRightChild().calculateTCMap();

        if (a == null || b == null || !a.isFunctional() || !b.isFunctional()) {
            return null;
        }
        TCMap result = new TCMap();
        TreeSet<Tuple> intervals = new TreeSet<>();
        TreeSet<BigFraction> points = getCombinedPointSet(a, b);
        TCMap map = new TCMap();
        map.addAll(a.getTCMap());
        map.addAll(b.getTCMap());
        TreeSet<BigFraction> intersects = (TreeSet<BigFraction>) calcIntersectionPoints(map);
        for (BigFraction o : intersects) {
            exceededTimeout();
            if (o != null && o.compareTo(BigFraction.ZERO) != 0 && o.compareTo(BigFraction.ZERO) != -1) {
                points.add(o);
            }
        }

        intervals.addAll(getIntervalSet(points));
        BigFraction c1;
        BigFraction c2;
        CostOrigin[] costsA;
        CostOrigin[] costsB;

        for (Tuple t : intervals) {
            exceededTimeout();
            costsA = selectXORCost(t.getLow(), t.getUp(), a);
            costsB = selectXORCost(t.getLow(), t.getUp(), b);
            c1 = selectMinOrMax(costsA[0].getCost(), costsB[0].getCost());
            c2 = selectMinOrMax(costsA[1].getCost(), costsB[1].getCost());
            if (c1 == null || c2 == null) {
                c1 = null;
                c2 = null;
            }

            //provenance part
            LinkedHashMap<Quadruple, ValidTuple> prov = new LinkedHashMap<>();
            prov.putAll(selectProvenance(costsA, costsB, t.getLow(),c1,t.getUp(),c2));

            result.add(new Quadruple(getClass().getSimpleName(), t.getLow(), c1, t.getUp(), c2, prov));
        }
        return compress(result);
    }
}
