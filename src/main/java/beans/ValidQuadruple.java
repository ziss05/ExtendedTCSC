package beans;

import com.github.kiprobinson.bigfraction.BigFraction;

import java.util.LinkedHashMap;
import java.util.Objects;

public final class ValidQuadruple implements Comparable {
    private final BigFraction d1;
    private final BigFraction c1;
    private final BigFraction d2;
    private final BigFraction c2;
    private final BigFraction gradient;
    private final String name;

    private LinkedHashMap<ValidQuadruple, ValidTuple> ancestors;


    public ValidQuadruple(String name, BigFraction d1, BigFraction c1, BigFraction d2, BigFraction c2, LinkedHashMap<ValidQuadruple, ValidTuple> ancestorList) {
        this(name, d1, c1, d2, c2);
        if (ancestorList != null) {
            setAncestors(ancestorList);
        } else ancestors = new LinkedHashMap<>();
    }

    public ValidQuadruple(String name, BigFraction d1, BigFraction c1, BigFraction d2, BigFraction c2) {
        if (d1 == null || d2 == null) {
            throw new IllegalArgumentException("Durations need to be a positive decimal, not null!", new Throwable());
        } else if (d1.compareTo(BigFraction.ZERO) < 0 || d2.compareTo(BigFraction.ZERO) < 0) {
            throw new IllegalArgumentException("Durations need to be greater than or equal to 0!", new Throwable());
        } else if (c1 != null && c1.compareTo(BigFraction.ZERO) < 0) {
            throw new IllegalArgumentException("Cost value c1 needs to be greater than or equal to 0!", new Throwable());
        } else if (c2 != null && c2.compareTo(BigFraction.ZERO) < 0) {
            throw new IllegalArgumentException("Cost value c2 needs to be greater than or equal to 0!", new Throwable());
        } else if ((c1 == null && c2 != null) || (c1 != null && c2 == null)) {
            throw new IllegalArgumentException("If one cost value is null, the other needs to be null as well!", new Throwable());
        } else if (d1.compareTo(d2) > 0) {
            throw new IllegalArgumentException("Second duration argument needs to be greater or equal than the first one!", new Throwable());
        } else {
            this.name = name;
            this.d1 = d1;
            this.c1 = c1;
            this.d2 = d2;
            this.c2 = c2;
            if (c1 == null) {
                gradient = null;
            } else if (d1.compareTo(d1) == 0) {
                gradient = new BigFraction(0);
            } else this.gradient = this.c2.subtract(this.c1).divide(this.d2.subtract(this.d1));
            this.ancestors = new LinkedHashMap<>();
        }
    }

    public ValidQuadruple(String name, int d1, int c1, int d2, int c2, LinkedHashMap<ValidQuadruple, ValidTuple> ancestorList) {
        this(name, d1, c1, d2, c2);
        if (ancestorList != null) {
            setAncestors(ancestorList);
        } else ancestors = new LinkedHashMap<>();
    }

    public ValidQuadruple(String name, int d1, int c1, int d2, int c2) {
        if (d1 <= 0 && d2 <= 0) {
            throw new IllegalArgumentException("Durations need to be greater than 0!", new Throwable());
        } else if (c1 < 0 && c2 < 0) {
            throw new IllegalArgumentException("Cost values need to be greater than or equal to 0!", new Throwable());
        } else if (d1 > d2) {
            throw new IllegalArgumentException("Second argument needs to be greater or equal than the first one!", new Throwable());
        } else {
            this.name = name;
            this.d1 = new BigFraction(d1);
            this.c1 = new BigFraction(c1);
            this.d2 = new BigFraction(d2);
            this.c2 = new BigFraction(c2);
            if (d1 == d2) {
                gradient = new BigFraction(0);
            } else this.gradient = this.c2.subtract(this.c1).divide(this.d2.subtract(this.d1));
            this.ancestors = new LinkedHashMap<>();
        }
    }

    public ValidQuadruple(ValidQuadruple quadruple) {
        this.name = quadruple.getName();
        this.d1 = quadruple.getD1();
        this.d2 = quadruple.getD2();
        this.c1 = quadruple.getC1();
        this.c2 = quadruple.getC2();
        this.gradient = quadruple.getGradient();
        this.ancestors = quadruple.getAncestors();
    }

    public String getName() {
        return name;
    }

    public BigFraction getD1() {
        return this.d1;
    }

    public BigFraction getD2() {
        return this.d2;
    }

    public BigFraction getC1() {
        return this.c1;
    }

    public BigFraction getC2() {
        return this.c2;
    }

    public BigFraction getGradient() {
        return this.gradient;
    }

    public LinkedHashMap<ValidQuadruple, ValidTuple> getAncestors() {
        LinkedHashMap<ValidQuadruple, ValidTuple> set = new LinkedHashMap<>();
        for (ValidQuadruple q : this.ancestors.keySet()) {
            set.put(new ValidQuadruple(q), new ValidTuple(this.ancestors.get(q)));
        }
        return set;
    }

    public void setAncestors(LinkedHashMap<ValidQuadruple, ValidTuple> ancestors) {
        this.ancestors = ancestors;
    }

    public void addAncestor(ValidQuadruple ancestor, ValidTuple validPeriod) {
        this.ancestors.put(ancestor, validPeriod);
    }

    public ValidTuple getValidPeriod(ValidQuadruple ancestor) {
        return new ValidTuple(this.ancestors.get(ancestor));
    }

    public void setValidPeriod(ValidQuadruple ancestor, ValidTuple validPeriod) {
        this.ancestors.put(ancestor, validPeriod);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ValidQuadruple quadruple = (ValidQuadruple) o;
        return d1.compareTo(quadruple.d1) == 0 && d2.compareTo(quadruple.d2) == 0 && c1.compareTo(quadruple.c1) == 0 && c2.compareTo(quadruple.c2) == 0 && this.name.equals(quadruple.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(d1, c1, d2, c2);
    }

    @Override
    public String toString() {
        /*if (c1 == null || c2 == null) {
            return "Quadruple{" +
                    "name=" + name +
                    ", d1=" + d1.toString() +
                    ", c1= null" +
                    ", d2=" + d2.toString() +
                    ", c2= null" +
                    ", gradient= null" +
                    '}';
        }
        return "Quadruple{" +
                "name=" + name +
                ", d1=" + d1.toString() +
                ", c1=" + c1.toString() +
                ", d2=" + d2.toString() +
                ", c2=" + c2.toString() +
                ", gradient=" + gradient.toString()+
                '}';*/
        return toStringWithAncestors();
    }

    public String toStringWithAncestors() {
        StringBuilder q = new StringBuilder();
        if (c1 == null || c2 == null) {
            q.append("Quadruple{name=").append(name).append(", d1=").append(d1.toString()).append(", c1= null").append(", d2=")
                    .append(d2.toString()).append(", c2= null").append(", gradient= null");
        } else q.append("Quadruple{name=").append(name).append(", d1=").append(d1.toString()).append(", c1=")
                .append(c1.toString()).append(", d2=").append(d2.toString()).append(", c2=").append(c2.toString()).append(", gradient=").append(gradient.toString());

        if (ancestors != null) {
            q.append(", provenance:").append(ancestors.toString());
        } else q.append(", provenance: null");

        return q.append('}').toString();

    }

    @Override
    public int compareTo(Object o) {
        final int BEFORE = -1;
        final int EQUAL = 0;
        final int AFTER = 1;
        ValidQuadruple q = (ValidQuadruple) o;
        if (c1 == null || q.c1 == null) {
            if (this.d1.compareTo(q.d1) == 0 && this.d2.compareTo(q.d2) == 0 && c1 == q.c1 && c2 == q.c2 && this.name.equals(q.getName())) {
                return EQUAL;
            }
        } else if (this.d1.compareTo(q.d1) == 0 && this.d2.compareTo(q.d2) == 0
                && this.c1.compareTo(q.c1) == 0 && this.c2.compareTo(q.c2) == 0 && this.name.equals(q.getName())) {
            return EQUAL;
        }
        if (this.d1.compareTo(q.d1) < 0) {
            return BEFORE;
        } else if (this.d1.compareTo(q.d1) == 0 && this.d2.compareTo(q.d2) < 0) {
            return BEFORE;
        } else if (this.d1.compareTo(q.d1) == 0 && this.d2.compareTo(q.d2) >= 0) {
            return AFTER;
        } else if (this.d1.compareTo(q.d1) > 0) {
            return AFTER;
        }
        return EQUAL;
    }

    public boolean joinable(ValidQuadruple q) {
        if (q.getC1() == null) {
            return false;
        }

        if (d1.compareTo(q.getD1()) < 0 && d2.compareTo(q.getD1()) == 0 && c2.compareTo(q.getC1()) == 0) {
            if (this.gradient.compareTo(q.getGradient()) == 0) {
                return true;
            }
        } else if (d1.compareTo(q.getD1()) > 0 && q.getD2().compareTo(d1) == 0 && q.getC2().compareTo(c1) == 0) {
            if (this.gradient.compareTo(q.getGradient()) == 0) {
                return true;
            }
        }
        return false;
    }
}
