import beans.*;
import com.github.kiprobinson.bigfraction.BigFraction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class TCCalculatorTest {
    private final String NAME="map";
    private final String NAME_SEQUENCE= "experiments/SEQ";
    private final String NAME_AND= "experiments/AND";
    private final String NAME_SYNCAND="SYNCAND";
    private final String NAME_XORC="XORC";
    private final String NAME_XORD="XORD";

    TCMap a;
    TCMap b;
    TCMap c;
    TCMap d;
    TCMap e;
    TCMap f;
    TCMap g;
    TCMap h;
    TCMap i;
    TCMap j;
    TCMap exp;
    TCMap res;

    @BeforeEach
    void setUp() {
        a = new TCMap();
        a.add(new Quadruple("a",1, 50, 3, 50));
        a.add(new Quadruple("a",3, 10, 10, 10));
        b = new TCMap();
        b.add(new Quadruple("b",2, 30, 4, 30));
        b.add(new Quadruple("b",4, 5, 6, 5));
        c = new TCMap();
        c.add(new Quadruple("c",1, 10, 3, 10));
        c.add(new Quadruple("c",3, 50, 10, 50));
        d = new TCMap();
        d.add(new Quadruple("d",2, 5, 4, 5));
        d.add(new Quadruple("d",4, 30, 6, 30));
        e = new TCMap();
        e.add(new Quadruple("e",1, 10, 3, 10));
        e.add(new Quadruple("e",3, 10, 10, 10));
        f = new TCMap();
        f.add(new Quadruple("f",2, 20, 4, 20));
        f.add(new Quadruple("f",4, 20, 6, 20));
        g = new TCMap();
        g.add(new Quadruple("g",1, 5, 3, 5));
        g.add(new Quadruple("g",3, 6, 10, 6));
        g.add(new Quadruple("g",10, 7, 12, 7));
        h = new TCMap();
        h.add(new Quadruple("h",2, 10, 4, 10));
        h.add(new Quadruple("h",4, 4, 6, 4));
        h.add(new Quadruple("h",6, 6, 9, 6));
        i = new TCMap();
        i.add(new Quadruple("i",1, 50, 11, 550));
        i.add(new Quadruple("i",11, 540, 20, 900));
        j = new TCMap();
        j.add(new Quadruple("jNAME",1, 150, 4, 200));
        j.add(new Quadruple("jNAME",4, 220, 6, 260));
        exp = new TCMap();
        res = new TCMap();
    }

    @AfterEach
    void tearDown() {
        a = null;
        b = null;
        c = null;
        d = null;
        e = null;
        f = null;
        g = null;
        h = null;
        i = null;
        j = null;
        exp = null;
    }

    @Test
    void addCostValues() {
        Constructor calc = new SEQ(new BasicService(null), new BasicService(null));
        BigFraction dec = calc.addCostValues(new BigFraction(1), new BigFraction(2));
        assertEquals(new BigFraction(3), dec);
    }

    @Test
    void addCostValuesNULLFirst() {
        Constructor calc = new SEQ(new BasicService(null), new BasicService(null));
        BigFraction dec = calc.addCostValues(null, new BigFraction(2));
        assertNull(dec);
    }

    @Test
    void addCostValuesInklNULLSecond() {
        Constructor calc = new SEQ(new BasicService(null), new BasicService(null));
        BigFraction dec = calc.addCostValues(new BigFraction(2), null);
        assertNull(dec);
    }

    @Test
    void testGetMin() {
        XOR calc = new XORD(new BasicService(null), new BasicService(null));
        BigFraction dec = calc.getMin(new BigFraction(1), new BigFraction(2));
        assertEquals(new BigFraction(1), dec);
    }

    @Test
    void testGetMinInklNULL() {
        XOR calc = new XORD(new BasicService(null), new BasicService(null));
        BigFraction dec = calc.getMin(new BigFraction(1), null);
        assertEquals(new BigFraction(1), dec);
    }


    @Test
    void testGetMinSet() {
        Constructor calc = new SEQ(new BasicService(null), new BasicService(null));
        Map<BigFraction,ArrayList<Quadruple>> set = new HashMap<>();
        ArrayList<Quadruple> a=new ArrayList<>();
        ArrayList<Quadruple> b=new ArrayList<>();
        ArrayList<Quadruple> c=new ArrayList<>();
        a.add(new Quadruple(NAME,1,2,2,3));
        b.add(new Quadruple(NAME,1,2,2,3));
        c.add(new Quadruple(NAME,1,2,2,3));
        set.put(new BigFraction(1),a);
        set.put(new BigFraction(3),b);
        set.put(null,c);

        assertEquals(new BigFraction(1), calc.getMin(set).getCost());
    }

    @Test
    void testGetMax() {
        XOR calc = new XORC(new BasicService(null), new BasicService(null));
        BigFraction dec = calc.getMax(new BigFraction(1), new BigFraction(2));
        assertEquals(new BigFraction(2), dec);
    }

    @Test
    void testGetMaxInklNULL() {
        XOR calc = new XORC(new BasicService(null), new BasicService(null));
        BigFraction dec = calc.getMax(new BigFraction(1), null);
        assertNull(dec);
    }


    @Test
    void testGetMaxSet() {
        Constructor calc = new SEQ(new BasicService(null), new BasicService(null));
        Map<BigFraction, ArrayList<Quadruple>> set = new HashMap<>();
        set.put(new BigFraction(1),null);
        set.put(new BigFraction(3),null);
        set.put(null,null);

        assertNull(calc.getMax(set).getCost());
    }

    @Test
    void testGetIntervalSet() {
        Constructor calc = new SEQ(new BasicService(null), new BasicService(null));
        TreeSet<Tuple> set = new TreeSet<>();
        set.add(new Tuple(1, 2));
        set.add(new Tuple(2, 3));
        set.add(new Tuple(3, 4));

        TreeSet<BigFraction> points = new TreeSet<>();
        points.add(new BigFraction(1));
        points.add(new BigFraction(2));
        points.add(new BigFraction(3));
        points.add(new BigFraction(4));

        try {
            assertEquals(set, calc.getIntervalSet(points));
        } catch (TimeoutException ex) {
            fail();
        }
    }

    @Test
    void testCalcTreeSingleService() {
        Node n = new BasicService(a);
        try {
            assertEquals(a, n.calculateTCMap());
        } catch (Exception ex) {
            fail();
        }
    }


    @Test
    void testCalcSEQID() {
        Node n = new SEQ(new BasicService(a), new BasicService(b));
        exp.add(new Quadruple(NAME_SEQUENCE,3, 80, 5, 80));
        exp.add(new Quadruple(NAME_SEQUENCE,5, 40, 7, 40));
        exp.add(new Quadruple(NAME_SEQUENCE,7, 15, 16, 15));

        try {
            res = n.calculateTCMap();
        } catch (Exception ex) {
            fail();
        }
        LinkedHashMap<Quadruple, ArrayList<ArrayList<ValidQuadruple>>> prov=res.calcTreeProvenance();
        assertEquals(exp, res);
    }

    @Test
    void testCalcSEQD() {
        Node n = new SEQ(new BasicService(c), new BasicService(d));
        exp.add(new Quadruple(NAME_SEQUENCE,3, 15, 7, 15));
        exp.add(new Quadruple(NAME_SEQUENCE,7, 40, 9, 40));
        exp.add(new Quadruple(NAME_SEQUENCE,9, 55, 14, 55));
        exp.add(new Quadruple(NAME_SEQUENCE,14, 80, 16, 80));
        try {
            res = n.calculateTCMap();
        } catch (Exception ex) {
            fail();
        }
        LinkedHashMap<Quadruple, ArrayList<ArrayList<ValidQuadruple>>> prov=res.calcTreeProvenance();
        assertEquals(exp, res);
    }

    @Test
    void testCalcSEQC() {
        Node n = new SEQ(new BasicService(e), new BasicService(f));
        exp.add(new Quadruple(NAME_SEQUENCE,3, 30, 16, 30));

        try {
            res = n.calculateTCMap();
        } catch (Exception ex) {
            fail();
        }
        LinkedHashMap<Quadruple, ArrayList<ArrayList<ValidQuadruple>>> prov=res.calcTreeProvenance();
        assertEquals(exp, res);
    }

    @Test
    void testCalcSEQM() {
        Node n = new SEQ(new BasicService(g), new BasicService(h));
        exp.add(new Quadruple(NAME_SEQUENCE,3, 15, 5, 15));
        exp.add(new Quadruple(NAME_SEQUENCE,5, 9, 9, 9));
        exp.add(new Quadruple(NAME_SEQUENCE,9, 10, 16, 10));
        exp.add(new Quadruple(NAME_SEQUENCE,16, 11, 18, 11));
        exp.add(new Quadruple(NAME_SEQUENCE,18, 12, 19, 12));
        exp.add(new Quadruple(NAME_SEQUENCE,19, 13, 21, 13));
        try {
            res = n.calculateTCMap();
        } catch (Exception ex) {
            fail();
        }
        LinkedHashMap<Quadruple, ArrayList<ArrayList<ValidQuadruple>>> prov=res.calcTreeProvenance();
        assertEquals(exp, res);
    }


    @Test
    void testCalcEXTID() {
        AND n = new AND(new BasicService(a), new BasicService(b));
        exp.add(new Quadruple(NAME_SEQUENCE,1, 50, 3, 50));
        exp.add(new Quadruple(NAME_SEQUENCE,3, 10, 13, 10));

        try {
            res = n.calcEXT(a, new BigFraction(3));
        } catch (TimeoutException ex) {
            fail();
        }
        LinkedHashMap<Quadruple, ArrayList<ArrayList<ValidQuadruple>>> prov=res.calcTreeProvenance();
        assertEquals(exp, res);
    }

    @Test
    void testCalcEXTD() {
        AND n = new AND(new BasicService(a), new BasicService(b));
        exp.add(new Quadruple(NAME_SEQUENCE,1, 10, 6, 10));
        exp.add(new Quadruple(NAME_SEQUENCE,6, 50, 13, 50));
        try {
            res = n.calcEXT(c, new BigFraction(3));
        } catch (TimeoutException ex) {
            fail();
        }
        LinkedHashMap<Quadruple, ArrayList<ArrayList<ValidQuadruple>>> prov=res.calcTreeProvenance();
        assertEquals(exp, res);
    }

    @Test
    void testCalcEXTC() {
        AND n = new AND(new BasicService(a), new BasicService(b));
        exp.add(new Quadruple(NAME_SEQUENCE,1, 10, 13, 10));
        try {
            res = n.calcEXT(e, new BigFraction(3));
        } catch (TimeoutException ex) {
            fail();
        }
        LinkedHashMap<Quadruple, ArrayList<ArrayList<ValidQuadruple>>> prov=res.calcTreeProvenance();
        assertEquals(exp, res);
    }

    @Test
    void testCalcEXTM() {
        AND n = new AND(new BasicService(a), new BasicService(b));
        g = new TCMap();
        g.add(new Quadruple(NAME_SEQUENCE,1, 10, 3, 10));
        g.add(new Quadruple(NAME_SEQUENCE,3, 9, 4, 9));
        g.add(new Quadruple(NAME_SEQUENCE,4, 11, 6, 11));
        g.add(new Quadruple(NAME_SEQUENCE,6, 7, 13, 7));
        exp.add(new Quadruple(NAME_SEQUENCE,1, 10, 3, 10));
        exp.add(new Quadruple(NAME_SEQUENCE,3, 9, 6, 9));
        exp.add(new Quadruple(NAME_SEQUENCE,6, 7, 16, 7));
        try {
            res = n.calcEXT(g, new BigFraction(3));
        } catch (TimeoutException ex) {
            fail();
        }
        LinkedHashMap<Quadruple, ArrayList<ArrayList<ValidQuadruple>>> prov=res.calcTreeProvenance();
        assertEquals(exp, res);
    }


    @Test
    void testCalcANDID() {
        Node n = new AND(new BasicService(a), new BasicService(b));
        exp.add(new Quadruple(NAME_AND,2, 80, 3, 80));
        exp.add(new Quadruple(NAME_AND,3, 40, 4, 40));
        exp.add(new Quadruple(NAME_AND,4, 15, 16, 15));
        try {
            res = n.calculateTCMap();
        } catch (TimeoutException ex) {
            fail();
        }
        LinkedHashMap<Quadruple, ArrayList<ArrayList<ValidQuadruple>>> prov=res.calcTreeProvenance();
        assertEquals(exp, res);
    }

    @Test
    void testCalcANDD() {
        Node n = new AND(new BasicService(c), new BasicService(d));
        exp.add(new Quadruple(NAME_AND,2, 15, 13, 15));
        exp.add(new Quadruple(NAME_AND,13, 55, 14, 55));
        exp.add(new Quadruple(NAME_AND,14, 80, 16, 80));
        try {
            res = n.calculateTCMap();
        } catch (TimeoutException ex) {
            fail();
        }
        LinkedHashMap<Quadruple, ArrayList<ArrayList<ValidQuadruple>>> prov=res.calcTreeProvenance();
        assertEquals(exp, res);
    }

    @Test
    void testCalcANDC() {
        Node n = new AND(new BasicService(e), new BasicService(f));
        exp.add(new Quadruple(NAME_AND,2, 30, 16, 30));
        try {
            res = n.calculateTCMap();
        } catch (TimeoutException ex) {
            fail();
        }
        LinkedHashMap<Quadruple, ArrayList<ArrayList<ValidQuadruple>>> prov=res.calcTreeProvenance();
        assertEquals(exp, res);
    }

    @Test
    void testCalcANDM() {
        Node n = new AND(new BasicService(g), new BasicService(h));
        exp.add(new Quadruple(NAME_AND,2, 15, 4, 15));
        exp.add(new Quadruple(NAME_AND,4, 9, 15, 9));
        exp.add(new Quadruple(NAME_AND,15, 10, 18, 10));
        exp.add(new Quadruple(NAME_AND,18, 12, 21, 12));
        try {
            res = n.calculateTCMap();
        } catch (TimeoutException ex) {
            fail();
        }
        LinkedHashMap<Quadruple, ArrayList<ArrayList<ValidQuadruple>>> prov=res.calcTreeProvenance();
        assertEquals(exp, res);
    }


    @Test
    void testCalcXORDID() {
        Node n = new XORD(new BasicService(a), new BasicService(b));
        exp.add(new Quadruple(NAME_XORD,1, 50, 2, 50));
        exp.add(new Quadruple(NAME_XORD,2, 30, 3, 30));
        exp.add(new Quadruple(NAME_XORD,3, 10, 4, 10));
        exp.add(new Quadruple(NAME_XORD,4, 5, 6, 5));
        exp.add(new Quadruple(NAME_XORD,6, 10, 10, 10));
        try {
            res = n.calculateTCMap();
        } catch (TimeoutException ex) {
            fail();
        }
        LinkedHashMap<Quadruple, ArrayList<ArrayList<ValidQuadruple>>> prov=res.calcTreeProvenance();
        assertEquals(exp, res);
    }

    @Test
    void testCalcXORDD() {
        Node n = new XORD(new BasicService(c), new BasicService(d));
        exp.add(new Quadruple(NAME_XORD,1, 10, 2, 10));
        exp.add(new Quadruple(NAME_XORD,2, 5, 4, 5));
        exp.add(new Quadruple(NAME_XORD,4, 30, 6, 30));
        exp.add(new Quadruple(NAME_XORD,6, 50, 10, 50));
        try {
            res = n.calculateTCMap();
        } catch (TimeoutException ex) {
            fail();
        }
        LinkedHashMap<Quadruple, ArrayList<ArrayList<ValidQuadruple>>> prov=res.calcTreeProvenance();
        assertEquals(exp, res);
    }

    @Test
    void testCalcXORDC() {
        Node n = new XORD(new BasicService(e), new BasicService(f));
        exp.add(new Quadruple(NAME_XORD,1, 10, 10, 10));
        try {
            res = n.calculateTCMap();
        } catch (TimeoutException ex) {
            fail();
        }
        LinkedHashMap<Quadruple, ArrayList<ArrayList<ValidQuadruple>>> prov=res.calcTreeProvenance();
        assertEquals(exp, res);
    }

    @Test
    void testCalcXORDM() {
        Node n = new XORD(new BasicService(g), new BasicService(h));
        exp.add(new Quadruple(NAME_XORD,1, 5, 3, 5));
        exp.add(new Quadruple(NAME_XORD,3, 6, 4, 6));
        exp.add(new Quadruple(NAME_XORD,4, 4, 6, 4));
        exp.add(new Quadruple(NAME_XORD,6, 6, 10, 6));
        exp.add(new Quadruple(NAME_XORD,10, 7, 12, 7));
        try {
            res = n.calculateTCMap();
        } catch (TimeoutException ex) {
            fail();
        }
        LinkedHashMap<Quadruple, ArrayList<ArrayList<ValidQuadruple>>> prov=res.calcTreeProvenance();
        assertEquals(exp, res);
    }


    @Test
    void testCalcXORCID() {
        Node n = new XORC(new BasicService(a), new BasicService(b));
        exp.add(new Quadruple(NAME_XORC,new BigFraction(2), new BigFraction(50), new BigFraction(3), new BigFraction(50)));
        exp.add(new Quadruple(NAME_XORC,new BigFraction(3), new BigFraction(30), new BigFraction(4), new BigFraction(30)));
        exp.add(new Quadruple(NAME_XORC,new BigFraction(4), new BigFraction(10), new BigFraction(6), new BigFraction(10)));
        try {
            res = n.calculateTCMap();
        } catch (TimeoutException ex) {
            fail();
        }
        LinkedHashMap<Quadruple, ArrayList<ArrayList<ValidQuadruple>>> prov=res.calcTreeProvenance();
        assertEquals(exp, res);
    }

    @Test
    void testCalcXORCD() {
        Node n = new XORC(new BasicService(c), new BasicService(d));
        exp.add(new Quadruple(NAME_XORC,2, 10, 3, 10));
        exp.add(new Quadruple(NAME_XORC,3, 50, 6, 50));

        try {
            res = n.calculateTCMap();

        } catch (TimeoutException ex) {
            fail();
        }
        LinkedHashMap<Quadruple, ArrayList<ArrayList<ValidQuadruple>>> prov=res.calcTreeProvenance();
        assertEquals(exp, res);
    }

    @Test
    void testCalcXORCC() {
        Node n = new XORC(new BasicService(e), new BasicService(f));
        exp.add(new Quadruple(NAME_XORC,2, 20, 6, 20));
        try {
            res = n.calculateTCMap();
            System.out.println(n);
        } catch (TimeoutException ex) {
            fail();
        }
        LinkedHashMap<Quadruple, ArrayList<ArrayList<ValidQuadruple>>> prov=res.calcTreeProvenance();
        assertEquals(exp, res);
    }

    @Test
    void testCalcXORCM() {
        Node n = new XORC(new BasicService(g), new BasicService(h));
        exp.add(new Quadruple(NAME_XORC,2, 10, 4, 10));
        exp.add(new Quadruple(NAME_XORC,4, 6, 9, 6));
        try {
            res = n.calculateTCMap();
        } catch (TimeoutException ex) {
            fail();
        }
        LinkedHashMap<Quadruple, ArrayList<ArrayList<ValidQuadruple>>> prov=res.calcTreeProvenance();
        assertEquals(exp, res);
    }

    @Test
    void testCalcIntersectionPoints() {
        Constructor n = new XORC(new BasicService(a), new BasicService(b));
        TreeSet<BigFraction> set = new TreeSet<>();
        set.add(new BigFraction(4));
        set.add(new BigFraction("4.(6)"));
        i.addAll(j.getTCMap());
        TreeSet<BigFraction> res = null;
        try {
            res = new TreeSet<>(n.calcIntersectionPoints(i));
        } catch (TimeoutException ex) {
            fail();
        }
        boolean check = true;
        for (BigFraction d : res) {
            check = set.contains(d);
        }
        assertTrue(check);
    }

    @Test
    void testCompress() {
        Constructor n = new SEQ(new BasicService(a), new BasicService(b));
        TCMap map = new TCMap();
        map.add(new Quadruple(NAME,3, 80, 5, 80));
        map.add(new Quadruple(NAME,5, 80, 7, 80));
        map.add(new Quadruple(NAME,5, 55, 7, 55));
        map.add(new Quadruple(NAME,7, 55, 9, 55));
        map.add(new Quadruple(NAME,5, 40, 7, 40));
        map.add(new Quadruple(NAME,7, 40, 14, 40));
        map.add(new Quadruple(NAME,7, 15, 9, 15));
        map.add(new Quadruple(NAME,9, 15, 16, 15));

        exp.add(new Quadruple(NAME_SEQUENCE,3, 80, 5, 80));
        exp.add(new Quadruple(NAME_SEQUENCE,5, 40, 7, 40));
        exp.add(new Quadruple(NAME_SEQUENCE,7, 15, 16, 15));

        TCMap res = null;
        try {
            res = n.compress(map);
        } catch (TimeoutException ex) {
            fail();
        }
        assertEquals(exp, res);
    }

   @Test
    void testProv() {
        a.clear();
        a.add(new Quadruple("a",
                new BigFraction(22),
                new BigFraction(447),
                new BigFraction(89),
                new BigFraction(205)
        ));
        a.add(new Quadruple("a",
                new BigFraction(89),
                new BigFraction(283),
                new BigFraction(146),
                new BigFraction(146)
        ));
        /*a.add(new Quadruple("a",
                new BigFraction(146),
                new BigFraction(354),
                new BigFraction(193),
                new BigFraction(198)
        ));
        a.add(new Quadruple("a",
                new BigFraction(193),
                new BigFraction(482),
                new BigFraction(231),
                new BigFraction(551)
        ));
        a.add(new Quadruple("a",
                new BigFraction(231),
                new BigFraction(83),
                new BigFraction(550),
                new BigFraction(388)
        ));*/
        b.clear();
        b.add(new Quadruple("b",
                new BigFraction(22),
                new BigFraction(89),
                new BigFraction(55),
                new BigFraction(41)
        ));
        b.add(new Quadruple("b",
                new BigFraction(55),
                new BigFraction(424),
                new BigFraction(71),
                new BigFraction(242)
        ));/*
        b.add(new Quadruple("b",
                new BigFraction(71),
                new BigFraction(489),
                new BigFraction(166),
                new BigFraction(106)
        ));
        b.add(new Quadruple("b",
                new BigFraction(166),
                new BigFraction(70),
                new BigFraction(232),
                new BigFraction(264)
        ));
        b.add(new Quadruple("b",
                new BigFraction(232),
                new BigFraction(361),
                new BigFraction(550),
                new BigFraction(230)
        ));*/
        c.clear();
        c.add(new Quadruple("c",
                new BigFraction(9),
                new BigFraction(117),
                new BigFraction(32),
                new BigFraction(119)
        ));
        c.add(new Quadruple("c",
                new BigFraction(32),
                new BigFraction(137),
                new BigFraction(37),
                new BigFraction(41)
        ));
       /* c.add(new Quadruple("c",
                new BigFraction(37),
                new BigFraction(143),
                new BigFraction(41),
                new BigFraction(98)
        ));
        c.add(new Quadruple("c",
                new BigFraction(41),
                new BigFraction(140),
                new BigFraction(61),
                new BigFraction(124)
        ));
        c.add(new Quadruple("c",
                new BigFraction(61),
                new BigFraction(41),
                new BigFraction(136),
                new BigFraction(100)
        ));*/
        d.clear();
        d.add(new Quadruple("d",
                new BigFraction(9),
                new BigFraction(43),
                new BigFraction(21),
                new BigFraction(30)
        ));
        d.add(new Quadruple("d",
                new BigFraction(21),
                new BigFraction(134),
                new BigFraction(41),
                new BigFraction(10)
        ));/*
        d.add(new Quadruple("d",
                new BigFraction(41),
                new BigFraction(72),
                new BigFraction(49),
                new BigFraction(120)
        ));
        d.add(new Quadruple("d",
                new BigFraction(49),
                new BigFraction(12),
                new BigFraction(64),
                new BigFraction(10)
        ));
        d.add(new Quadruple("d",
                new BigFraction(64),
                new BigFraction(119),
                new BigFraction(136),
                new BigFraction(55)
        ));*/
        e.clear();
        e.add(new Quadruple("e",
                new BigFraction(4),
                new BigFraction(57),
                new BigFraction(12),
                new BigFraction(39)
        ));
        e.add(new Quadruple("e",
                new BigFraction(12),
                new BigFraction(118),
                new BigFraction(21),
                new BigFraction(85)
        ));/*
        e.add(new Quadruple("e",
                new BigFraction(21),
                new BigFraction(81),
                new BigFraction(31),
                new BigFraction(139)
        ));
        e.add(new Quadruple("e",
                new BigFraction(31),
                new BigFraction(25),
                new BigFraction(45),
                new BigFraction(130)
        ));
        e.add(new Quadruple("e",
                new BigFraction(45),
                new BigFraction(108),
                new BigFraction(136),
                new BigFraction(11)
        ));*/
        f.clear();
        f.add(new Quadruple("f",
                new BigFraction(4),
                new BigFraction(56),
                new BigFraction(5),
                new BigFraction(39)
        ));
        f.add(new Quadruple("f",
                new BigFraction(5),
                new BigFraction(34),
                new BigFraction(11),
                new BigFraction(69)
        ));/*
        f.add(new Quadruple("f",
                new BigFraction(11),
                new BigFraction(91),
                new BigFraction(29),
                new BigFraction(5)
        ));
        f.add(new Quadruple("f",
                new BigFraction(29),
                new BigFraction(66),
                new BigFraction(52),
                new BigFraction(133)
        ));
        f.add(new Quadruple("f",
                new BigFraction(52),
                new BigFraction(94),
                new BigFraction(136),
                new BigFraction(74)
        ));*/
        g.clear();
        g.add(new Quadruple("g",
                new BigFraction(22),
                new BigFraction(89),
                new BigFraction(63),
                new BigFraction(117)
        ));
        g.add(new Quadruple("g",
                new BigFraction(63),
                new BigFraction(153),
                new BigFraction(103),
                new BigFraction(441)
        ));/*
        g.add(new Quadruple("g",
                new BigFraction(103),
                new BigFraction(289),
                new BigFraction(120),
                new BigFraction(185)
        ));
        g.add(new Quadruple("g",
                new BigFraction(120),
                new BigFraction(237),
                new BigFraction(179),
                new BigFraction(119)
        ));
        g.add(new Quadruple("g",
                new BigFraction(179),
                new BigFraction(437),
                new BigFraction(550),
                new BigFraction(103)
        ));*/

        Node n1=new SEQ(new BasicService(e), new BasicService(f));//XORD
        Node n2=new AND(new BasicService(c), new BasicService(d));//XORC
        Node n12=new SEQ(n1,n2);//XORD
        Node n121=new XORC(n12,new BasicService(g));
        Node n3=new XORD(new BasicService(a),new BasicService(b));
        Node n= new AND(n3,n121);//XORC
//        Node n = new AND(new XORD(new BasicService(a), new BasicService(b)), new XORC(new SEQ(new AND(new BasicService(c), new BasicService(d)), new SEQ(new BasicService(e), new BasicService(f))), new BasicService(g)));
        TCMap res = null;
        try {
            res = n.calculateTCMap();
//            res.normalize();

            /*n1.getTCMap().normalize();
            n2.getTCMap().normalize();
            n12=new SEQ(n1,n2);
            n12.calculateTCMap();
            n12.getTCMap().normalize();
            n121=new XORC(n12,new BasicService(g));
            n121.calculateTCMap();
            n121.getTCMap().normalize();
            n3.getTCMap().normalize();
            exp=new AND(n3,n121).calculateTCMap();
            exp.normalize();*/
        } catch (TimeoutException ex) {
            fail();
        }
        assertEquals(exp, res);
    }
}
