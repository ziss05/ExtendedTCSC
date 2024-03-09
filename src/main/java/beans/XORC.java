package beans;


import com.github.kiprobinson.bigfraction.BigFraction;

import java.util.LinkedHashMap;
import java.util.Map;


public class XORC extends XOR {
    public XORC(Node leftChild, Node rightChild) {
        super(leftChild, rightChild);
    }

    @Override
    public CostOrigin[] selectXORCost(BigFraction d1, BigFraction d2, TCMap t) throws TimeoutException {
        return selectCost(d1, d2, t, false);
    }

    @Override
    BigFraction selectMinOrMax(BigFraction d1, BigFraction d2) {
        return getMax(d1, d2);
    }

    @Override
    Map<Quadruple, ValidTuple> selectProvenance(CostOrigin[] costsA, CostOrigin[] costsB, BigFraction d1, BigFraction c1, BigFraction d2, BigFraction c2) {
        LinkedHashMap<Quadruple, ValidTuple> prov = new LinkedHashMap<>();

        Quadruple a=costsA[0].getQuadruples().get(0);
        if (a == null) {
            prov.put(null,null);
        } else {
            prov.put(a,new ValidTuple(d1, d2));
        }
        Quadruple b=costsB[0].getQuadruples().get(0);
        if (b == null) {
            prov.put(null,null);
        } else {
            prov.put(b,new ValidTuple(d1, d2));
        }
        return prov;
    }
}
