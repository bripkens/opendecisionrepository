package nl.rug.search.odr.project;

import nl.rug.search.odr.AbstractEjbTest;
import nl.rug.search.odr.entities.RelationshipType;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class RelationshipTypeTest extends AbstractEjbTest {

    private RelationshipTypeLocal rtl;




    @Before
    public void setUp() {
        rtl = lookUp(RelationshipTypeBean.class, RelationshipTypeLocal.class);
    }




    @Test
    public void getPublicTypes() {
        String name = "something";

        RelationshipType type = new RelationshipType();
        type.setName(name);
        rtl.persist(type);

        assertTrue(rtl.getPublicTypes().isEmpty());

        String publicName = "somethingPublic";

        type = new RelationshipType();
        type.setName(publicName);
        type.setCommon(true);
        rtl.persist(type);

        assertEquals(publicName, rtl.getPublicTypes().get(0).getName());
    }
}
