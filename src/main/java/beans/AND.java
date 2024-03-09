package beans;

import com.github.kiprobinson.bigfraction.BigFraction;


public class AND extends PAR{
    final String DUMMY = "Dummy";
    public AND(Node leftChild, Node rightChild) {
        super(leftChild, rightChild);
    }

    public TCMap calcEXT(TCMap tcMap, BigFraction d) throws TimeoutException {
        if (tcMap == null || d == null) {
            return null;
        }
        TCMap map = new TCMap();
        map.add(new Quadruple(DUMMY,BigFraction.ZERO, BigFraction.ZERO, d, BigFraction.ZERO));
        Node node=new SEQ(new BasicService(tcMap),new BasicService(map));
        return node.calculateTCMap();
    }

    @Override
    TCMap[] getTCMaps() throws TimeoutException{
        TCMap a=getLeftChild().getTCMap();
        TCMap b=getRightChild().getTCMap();
        BigFraction m = (BigFraction) a.getMax().max(b.getMax());
        TCMap[] tcMaps=new TCMap[2];
        tcMaps[0]=calcEXT(a,m);
        tcMaps[1]=calcEXT(b,m);
        return tcMaps;
    }
}
