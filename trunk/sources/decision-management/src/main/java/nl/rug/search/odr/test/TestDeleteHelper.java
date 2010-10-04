package nl.rug.search.odr.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import nl.rug.search.odr.entities.BaseEntity;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Stateless
public class TestDeleteHelper implements TestDeleteHelperLocal {

    private static String dbURL = "jdbc:derby://localhost:1527/sun-appserv-samples;create=true;user=APP;password=APP";

    private static Connection conn;

    static {
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
        } catch (InstantiationException ex) {
        } catch (IllegalAccessException ex) {
        } catch (ClassNotFoundException ex) {
        }
        try {
            DriverManager.getConnection(dbURL);
        } catch (SQLException ex) {
        }
    }
    @PersistenceContext
    private EntityManager entityManager;

    public void truncateAll() {
        
    }

    @Override
    public void deleteAll() {
        String[] entityNames = new String[]{"ProjectMember", "Person", "Project", "StakeholderRole",
            "Iteration", "Version", "ArchitecturalDecision", "Status", "Requirement"};

        for (int i = 0; i < 20; i++) {

            for (String entityName : entityNames) {
                Query q = entityManager.createQuery("SELECT x FROM " + entityName + " x");
                List<BaseEntity> entities = (List<BaseEntity>) q.getResultList();

                for (BaseEntity entity : entities) {
                    try {
                        q = entityManager.createQuery("DELETE FROM " + entityName + " x WHERE x.id = :id");
                        q.setParameter("id", entity.getId());
                        q.executeUpdate();
                    } catch (Exception e) {
                    }
                }

            }
//
//            try {
//                Query q = entityManager.createQuery("DELETE FROM ProjectMember");
//                q.executeUpdate();
//                System.out.println(q.getResultList().size());
//            } catch (Exception e) {
//            }
//
//            try {
//                Query q = entityManager.createQuery("DELETE FROM Person");
//                q.executeUpdate();
//                System.out.println(q.getResultList().size());
//            } catch (Exception e) {
//            }
//
//            try {
//                Query q = entityManager.createQuery("DELETE FROM Project");
//                q.executeUpdate();
//                System.out.println(q.getResultList().size());
//            } catch (Exception e) {
//            }
//
//            try {
//                Query q = entityManager.createQuery("DELETE FROM StakeholderRole");
//                q.executeUpdate();
//                System.out.println(q.getResultList().size());
//            } catch (Exception e) {
//            }
//            try {
//                Query q = entityManager.createQuery("DELETE FROM Iteration");
//                q.executeUpdate();
//                System.out.println(q.getResultList().size());
//            } catch (Exception e) {
//            }
//            try {
//                Query q = entityManager.createQuery("DELETE FROM Status");
//                q.executeUpdate();
//                System.out.println(q.getResultList().size());
//            } catch (Exception e) {
//            }
//            try {
//                Query q = entityManager.createQuery("DELETE FROM Version");
//                q.executeUpdate();
//                System.out.println(q.getResultList().size());
//            } catch (Exception e) {
//            }
//            try {
//                Query q = entityManager.createQuery("DELETE FROM Requirement");
//                q.executeUpdate();
//                System.out.println(q.getResultList().size());
//            } catch (Exception e) {
//            }
//            try {
//                Query q = entityManager.createQuery("DELETE FROM ArchitecturalDecision");
//                q.executeUpdate();
//                System.out.println(q.getResultList().size());
//            } catch (Exception e) {
//            }
        }
    }
}
