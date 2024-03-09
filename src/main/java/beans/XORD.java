package beans;


import com.github.kiprobinson.bigfraction.BigFraction;

import java.util.LinkedHashMap;
import java.util.Map;

public class XORD extends XOR {

    public XORD(Node leftChild, Node rightChild) {
        super(leftChild, rightChild);
    }

    @Override
    public CostOrigin[] selectXORCost(BigFraction d1, BigFraction d2, TCMap t) throws TimeoutException {
        return selectCost(d1, d2, t, true);
    }

    @Override
    BigFraction selectMinOrMax(BigFraction d1, BigFraction d2) {
        return getMin(d1, d2);
    }

    @Override
    Map<Quadruple, ValidTuple> selectProvenance(CostOrigin[] costsA, CostOrigin[] costsB, BigFraction d1, BigFraction c1, BigFraction d2, BigFraction c2) {
        LinkedHashMap<Quadruple, ValidTuple> prov = new LinkedHashMap<>();
        Quadruple x;

        if (c1==null){ //if c1==null =>c2, because XORD: a and b = null --> option not available
            x=null;
        }
//        compare lower and upper cost if not equal and select right predecessor
        else if (costsA[0].getCost()!=null && costsB[0].getCost() !=null && costsA[0].getCost().compareTo(costsB[0].getCost()) != 0) {
            if (costsA[0].getCost().compareTo(c1) == 0) {
                x = costsA[0].getQuadruples().get(0); //get(0) - because max 2 if intersection point which it is not

            } else {
                x = costsB[0].getQuadruples().get(0);
            }

        } else if (costsA[1].getCost()!=null && costsB[1].getCost() !=null && costsA[1].getCost().compareTo(costsB[1].getCost()) != 0) {
            if (costsA[1].getCost().compareTo(c2) == 0) {
                x = costsA[1].getQuadruples().get(0);
            } else {
                x = costsB[1].getQuadruples().get(0);
            }
        }
        else if (costsA[0].getCost()==null){
            x=costsB[0].getQuadruples().get(0);
        } else if (costsA[1].getCost()==null){
            x=costsB[1].getQuadruples().get(0);
        }
        else if (costsB[0].getCost()==null){
            x=costsA[0].getQuadruples().get(0);
        } else if (costsB[1].getCost()==null){
            x=costsA[1].getQuadruples().get(0);
        }
//        if equal
        else x=costsA[0].getQuadruples().get(0);
        prov.put(x,new ValidTuple(d1, d2));

        return prov;
    }
}
