package beans;

import com.github.kiprobinson.bigfraction.BigFraction;

import java.util.*;

public abstract class Constructor extends Node {
    Node leftChild;
    Node rightChild;

    public Constructor(Node leftChild, Node rightChild) {
        this.leftChild = leftChild;
        this.rightChild = rightChild;
    }

    public Node getLeftChild() {
        return leftChild;
    }

    public Node getRightChild() {
        return rightChild;
    }

    @Override
    public String toString() {
        if (super.getTCMap() == null) {
            return "Constructor{" + this.getClass().getSimpleName() +
                    ": tcMap=null, leftChild=" + leftChild.toString() +
                    ", rightChild=" + rightChild.toString() +
                    "}";
        }
        return "Constructor{" + this.getClass().getSimpleName() +
                ": tcMap=" + super.getTCMap().toString() +
                "; leftChild=" + leftChild.toString() +
                ", rightChild=" + rightChild.toString() + '}';
    }


//    Basic Operations

    public BigFraction addCostValues(BigFraction c1, BigFraction c2) {
        if (c1 == null || c2 == null) {
            return null;
        }
        return c1.add(c2);
    }


    public CostOrigin getMin(Map<BigFraction, ArrayList<Quadruple>> s) {
        CostOrigin min = new CostOrigin();
        if (s == null || s.size() == 0 || s.size() == 1 && s.keySet().contains(null)) {
            min.setCost(null);
            min.addQuadruple(null);
            return min;
        } else {
            s.remove(null);
        }
        if (s.size() == 1) {
            BigFraction[] set = s.keySet().toArray(new BigFraction[0]);
            min.setCost(set[0]);
            min.addQuadruples(s.get(set[0]));
            return min;
        }
        min.setCost(s.keySet().stream().min(BigFraction::compareTo).get());
        min.addQuadruples(s.get(min.getCost()));
        return min;
    }

    public CostOrigin getMax(Map<BigFraction, ArrayList<Quadruple>> s) {
        CostOrigin max = new CostOrigin();
        if (s == null) {
            max.setCost(null);
            max.setQuadruples(null);
            return max;
        }
        if (s.size() == 0) {
            max.setCost(null);
            max.addQuadruple(null);
            return max;
        }
        if (s.keySet().contains(null)) {
            max.setCost(null);
            max.setQuadruples(s.get(null));
            return max;
        }
        if (s.size() == 1) {
            BigFraction[] set = s.keySet().toArray(new BigFraction[0]);
            max.setCost(set[0]);
            max.setQuadruples(s.get(set[0]));
            return max;
        }
        max.setCost(s.keySet().stream().max(BigFraction::compareTo).get());
        max.setQuadruples(s.get(max.getCost()));
        return max;
    }

    public CostOrigin[] selectCost(BigFraction d1, BigFraction d2, TCMap t, boolean useMin) throws TimeoutException {
        boolean foundLow = false;
        boolean foundUp = false;

        if (d1 == null || d2 == null) {
            return null;
        }

        CostOrigin[] points = new CostOrigin[2];
        HashMap<BigFraction, ArrayList<Quadruple>> setLow = new HashMap<>();
        HashMap<BigFraction, ArrayList<Quadruple>> setUp = new HashMap<>();
        TreeSet<Quadruple> tSet = (TreeSet<Quadruple>) t.getTCMap();

        for (Quadruple q : tSet) {
            exceededTimeout();
            if (isDurationWithinInterval(d1, q)) {
                if (q.getGradient() == null) {
                    if (!setLow.containsKey(null)) {
                        ArrayList<Quadruple> temp = new ArrayList<>();
                        temp.add(q);
                        setLow.put(null, temp);
                    } else {
                        setLow.get(null).add(q);
                    }
                } else {
                    BigFraction tempD = q.getC1().add(q.getGradient().multiply(d1.subtract(q.getD1())));
                    if (!setLow.containsKey(tempD)) {
                        ArrayList<Quadruple> temp = new ArrayList<>();
                        temp.add(q);
                        setLow.put(tempD, temp);
                    } else {
                        setLow.get(tempD).add(q);
                    }
                }
            } else if (q.getD1().compareTo(d1) > 0) {
                foundLow = true;
            }

            if (isUpperDurationWithinInterval(d2, q)) {

                if (q.getGradient() == null) {
                    if (!setUp.containsKey(null)) {
                        ArrayList<Quadruple> temp = new ArrayList<>();
                        temp.add(q);
                        setUp.put(null, temp);
                    } else {
                        setUp.get(null).add(q);
                    }
                } else {
                    BigFraction tempD = q.getC1().add(q.getGradient().multiply(d2.subtract(q.getD1())));
                    if (!setUp.containsKey(tempD)) {
                        ArrayList<Quadruple> temp = new ArrayList<>();
                        temp.add(q);
                        setUp.put(tempD, temp);
                    } else {
                        setUp.get(tempD).add(q);
                    }
                }
            } else if (q.getD1().compareTo(d2) >= 0) {
                foundUp = true;
            }

            if (foundLow && foundUp) {

                if (useMin) {
                    points[0] = getMin(setLow);
                    points[1] = getMin(setUp);
                } else {
                    points[0] = getMax(setLow);
                    points[1] = getMax(setUp);
                }

                return points;
            }
        }
        if (useMin) {
            points[0] = getMin(setLow);
            points[1] = getMin(setUp);
        } else {
            points[0] = getMax(setLow);
            points[1] = getMax(setUp);
        }

        return points;
    }

    private static boolean isUpperDurationWithinInterval(BigFraction d, Quadruple q) {
        return q.getD1().compareTo(d) < 0 && d.compareTo(q.getD2()) <= 0;
    }

    private static boolean isDurationWithinInterval(BigFraction d, Quadruple q) {
        return q.getD1().compareTo(d) <= 0 && d.compareTo(q.getD2()) < 0;
    }

    public Set<Tuple> getIntervalSet(Set<BigFraction> pointSet) throws TimeoutException {
        if (pointSet == null) {
            return null;
        }
        HashSet<Tuple> intervals = new HashSet<>();
        TreeSet<BigFraction> points = new TreeSet<>(pointSet);
        Iterator<BigFraction> iterator = points.iterator();
        BigFraction last;
        if (points.size() >= 2) {
            last = iterator.next();
        } else return null;
        while (iterator.hasNext()) {
            exceededTimeout();
            BigFraction current = iterator.next();
            intervals.add(new Tuple(last, current));
            last = current;
        }
        return intervals;
    }

    public TreeSet<BigFraction> getCombinedPointSet(TCMap a, TCMap b) {
        TreeSet<BigFraction> points = new TreeSet<>();
        points.addAll(a.getPointSet());
        points.addAll(b.getPointSet());
        return points;
    }

    public Set<BigFraction> calcIntersectionPoints(TCMap a) throws TimeoutException {

        if (a == null) {
            return null;
        }
        TreeSet<BigFraction> intersects = new TreeSet<>();
        Object[] quadruples = a.getTCMap().toArray();

        if (quadruples == null) {
            return null;
        }
        boolean remainingQuadruplesOutOfCurrentDurationRange = false;

        for (int i = 0; i < quadruples.length; i++) {
            exceededTimeout();
            Quadruple qa = (Quadruple) quadruples[i];
            if (qa.getC1() != null) {
                for (int j = i + 1; j < quadruples.length && !remainingQuadruplesOutOfCurrentDurationRange; j++) {
                    exceededTimeout();
                    Quadruple qb = (Quadruple) quadruples[j];
                    if (qb.getC1() != null) {
                        if (!(qa.getD1().compareTo(qb.getD1()) == 0 && qa.getC1().compareTo(qb.getC1()) == 0)) {
                            if (isDurationWithinInterval(qb.getD1(), qa)) {
                                if (qa.getGradient().compareTo(qb.getGradient()) != 0) {
                                    BigFraction intersectingPoint = (qb.getC1().subtract(qa.getC1())
                                            .add(
                                                    (qa.getGradient().multiply(qa.getD1()))
                                                            .subtract((qb.getGradient().multiply(qb.getD1()))))
                                    )

                                            .divide((qa.getGradient().subtract(qb.getGradient())));
                                    if (testIfIntersectionpointIsWithinIntervals(qa, qb, intersectingPoint))
                                        intersects.add(intersectingPoint);
                                }
                            } else {
                                remainingQuadruplesOutOfCurrentDurationRange = true;
                            }
                        }
                    }
                }
                remainingQuadruplesOutOfCurrentDurationRange = false;
            }
        }
        return intersects;
    }

    private static boolean testIfIntersectionpointIsWithinIntervals(Quadruple qa, Quadruple qb, BigFraction intersectingPoint) {
        return ((BigFraction) qa.getD1().max(qb.getD1())).compareTo(intersectingPoint) <= 0
                && intersectingPoint.compareTo(qa.getD2().min(qb.getD2())) <= 0;
    }

    public TCMap compress(TCMap map) throws TimeoutException {

        if (map == null || map.getTCMap() == null) {
            return null;
        }

        TreeSet<BigFraction> set = new TreeSet<>(map.getPointSet());
        TreeSet<BigFraction> intersects = (TreeSet<BigFraction>) calcIntersectionPoints(map);
        for (BigFraction d : intersects
        ) {
            set.add(d);
        }

        TreeSet<Tuple> intervals = new TreeSet<>(getIntervalSet(set));
        TCMap compressed = new TCMap();

        CostOrigin[] costs;

        for (Tuple t : intervals) {
            exceededTimeout();
            costs = selectCost(t.getLow(), t.getUp(), map, true);

            if (costs[0].getCost() == null || costs[1].getCost() == null) {
                costs[0].setCost(null);
                costs[1].setCost(null);
            }

            //provenance part
            LinkedHashMap<Quadruple, ValidTuple> prov = new LinkedHashMap<>();

            if (costs[0].getQuadruples() != null && costs[1].getQuadruples() != null) {
                if (costs[0].getQuadruples().size() == 1 && costs[1].getQuadruples().size() == 1) {
                    Quadruple q = costs[0].getQuadruples().get(0);
                    Quadruple q2 = costs[1].getQuadruples().get(0);
                    if (q == null & q2 == null) {
                        prov = null;
                    } else if (costs[0].getQuadruples().get(0).equals(costs[1].getQuadruples().get(0))) {
                        prov.put(q, new ValidTuple(t.getLow(), t.getUp()));
                    }
                } else if (costs[0].getQuadruples().size() == 1 && costs[1].getQuadruples().size() > 1) {
                    if ((costs[1].getQuadruples().contains(costs[0].getQuadruples().get(0)))) {
                        Quadruple q = costs[0].getQuadruples().get(0);
                        if (q == null) {
                            prov = null;
                        } else {
                            prov.put(q, new ValidTuple(t.getLow(), t.getUp()));
                        }
                    }

                } else if (costs[0].getQuadruples().size() > 1 && costs[1].getQuadruples().size() == 1) {
                    if ((costs[0].getQuadruples().contains(costs[1].getQuadruples().get(0)))) {
                        Quadruple q = costs[1].getQuadruples().get(0);
                        if (q == null) {
                            prov = null;
                        } else {
                            prov.put(q, new ValidTuple(t.getLow(), t.getUp()));
                        }
                    }

                } else if (costs[0].getQuadruples().size() > 1 && costs[1].getQuadruples().size() > 1) {
                    for (Quadruple q : costs[0].getQuadruples()) {
                        if (costs[1].getQuadruples().contains(q)) {
                            if (q == null) {
                                prov = null;
                            } else {
                                prov.put(q, new ValidTuple(t.getLow(), t.getUp()));
                            }
                            break;
                        }
                    }
                }
            }else prov = null;

            compressed.add(new Quadruple(getClass().getSimpleName(), t.getLow(), costs[0].getCost(), t.getUp(), costs[1].getCost(), prov));
        }
        compressed.normalize();
        setTCMap(compressed);
        return compressed;
    }


}
