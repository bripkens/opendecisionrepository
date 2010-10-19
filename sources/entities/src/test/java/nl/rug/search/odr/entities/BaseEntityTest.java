
package nl.rug.search.odr.entities;

import org.junit.Test;
import static org.junit.Assert.*;
import static nl.rug.search.odr.TestUtil.*;
/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class BaseEntityTest {
    
    @Test
    public void testIdComparison() {
        Person person1 = new Person();
        person1.setId(1l);

        Person person2 = new Person();
        person2.setId(1l);

        assertEquals(person1, person2);
    }

    @Test
    public void testIdComparisonFail() {
        Person person1 = new Person();
        person1.setId(1l);

        Person person2 = new Person();
        person2.setId(2l);

        assertNotEquals(person1, person2);
    }

    @Test
    public void testComparison() {
        Person person1 = new Person();
        person1.setName("12345");
        person1.setId(1l);

        Person person2 = new Person();
        person2.setName("12345");

        assertEquals(person1, person2);
    }
}
