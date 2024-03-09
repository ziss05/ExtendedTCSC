import beans.*;
import com.github.kiprobinson.bigfraction.BigFraction;
import generated.Tree;

public class Transformer {
    private static Transformer instance;
    private Transformer() {
    }

    public static Transformer getInstance() {
        if (instance == null) {
            instance = new Transformer();
        }
        return instance;
    }

    public Node parseTree(Tree tree) {
        return parseNode(tree.getNode());
    }

    public Node parseNode(generated.Node node) {
        if (node == null){return null;}
        Node rootNode;
        if (node instanceof generated.BasicService) {
            rootNode = parseBasicService((generated.BasicService) node);
        } else {
            rootNode = parseConstructor((generated.Constructor) node);
        }
        return rootNode;
    }

    public Quadruple parseQuadruple(String name,generated.Quadruple quadruple) {
        if (quadruple == null) {
            return null;
        }
        return new Quadruple(name,new BigFraction(quadruple.getD1()), new BigFraction(quadruple.getC1()), new BigFraction(quadruple.getD2()), new BigFraction(quadruple.getC2()));
    }

    public BasicService parseBasicService(generated.BasicService basicService) {
        TCMap tcMap = new TCMap();
        for (generated.Quadruple q : basicService.getQuadruple()) {
            tcMap.add(parseQuadruple(basicService.getIdentifier(),q));
        }
        return new BasicService(tcMap);
    }

    public Constructor parseConstructor(generated.Constructor constructor) {
        if (constructor == null){return null;}
        Node child1;
        Node child2;
        if (constructor.getLeft() instanceof generated.BasicService) {
            child1 = parseBasicService((generated.BasicService) constructor.getLeft());
        } else {
            child1 = parseConstructor((generated.Constructor) constructor.getLeft());
        }
        if (constructor.getRight() instanceof generated.BasicService) {
            child2 = parseBasicService((generated.BasicService) constructor.getRight());
        } else {
            child2 = parseConstructor((generated.Constructor) constructor.getRight());
        }
        return parseType(constructor.getType(),child1,child2);
    }

    public Constructor parseType(generated.Type type, Node left, Node right) {
        if (type == null){return null;}
        switch (type) {
            case AND:
                return new AND(left, right);
            case SYNCAND:
                return new SYNCAND(left, right);
            case XORC:
                return new XORC(left, right);
            case XORD:
                return new XORD(left, right);
            case SEQ:
                return new SEQ(left, right);
        }
        return null;
    }
}
