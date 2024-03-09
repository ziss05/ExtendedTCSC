package beans;

import com.github.kiprobinson.bigfraction.BigFraction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

public class TCMapTest {
    TCMap map = new TCMap();
    private final String NAME="map";
    @BeforeEach
    public void setUp() throws Exception {
        map.add(new Quadruple(NAME,3, 80, 5, 80));
        map.add(new Quadruple(NAME,5, 40, 7, 40));
        map.add(new Quadruple(NAME,7, 15, 9, 15));
        map.add(new Quadruple(NAME,9, 15, 14, 15));
        map.add(new Quadruple(NAME,14, 15, 16, 15));
    }

    @AfterEach
    public void tearDown() throws Exception {
        map =null;
    }

    @Test
    public void normalize() {
        TCMap exp = new TCMap();
        exp.add(new Quadruple(NAME,3, 80, 5, 80));
        exp.add(new Quadruple(NAME,5, 40, 7, 40));
        exp.add(new Quadruple(NAME,7, 15, 16, 15));
        map.normalize();
        assertEquals(exp,map);
    }

    @Test
    public void getRange() {
        BigFraction[] range=map.getRange();
        BigFraction[] exp={BigFraction.valueOf(3), BigFraction.valueOf(16)};

        assertArrayEquals(exp,range);
    }

    @Test
    public void isFunctional() {
        assertTrue(map.isFunctional());
    }

    @Test
    public void isNonFunctional() {
        map.add(new Quadruple(NAME,9,10,15,10));
        assertFalse(map.isFunctional());
    }

    @Test
    public void getPointSet() {
        HashSet<BigFraction> set=new HashSet<>();
        set.add(BigFraction.valueOf(3));
        set.add(BigFraction.valueOf(5));
        set.add(BigFraction.valueOf(7));
        set.add(BigFraction.valueOf(9));
        set.add(BigFraction.valueOf(14));
        set.add(BigFraction.valueOf(16));

        assertEquals(set,map.getPointSet());
    }

}