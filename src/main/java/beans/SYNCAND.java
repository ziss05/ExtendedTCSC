package beans;

public class SYNCAND extends PAR{
    public SYNCAND(Node leftChild, Node rightChild) {
        super(leftChild, rightChild);
    }

    @Override
    TCMap[] getTCMaps() {
        TCMap[] tcMaps=new TCMap[2];
        tcMaps[0]=getLeftChild().getTCMap();
        tcMaps[1]=getRightChild().getTCMap();
        return tcMaps;
    }
}