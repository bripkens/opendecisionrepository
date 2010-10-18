/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.rug.search.odr;

/**
 *
 * @author Stefan
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseCleaner {

    private Connection connect = null;
    private Statement statement = null;
    private ResultSet list = null;

    public void delete() {
        for (int z = 0; z < 5; z++) {
            try {

                Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
                connect = DriverManager.getConnection("jdbc:derby://localhost:1527/sun-appserv-samples");
                String[] entityNames = new String[]{ "ProjectMember", "StakeholderRole", "Iteration", "Person", "Project", "Project_Stakeholderrole"};

//                {"Requirement", "ActionType", "ProjectMember", "Version", "StakeholderRole",
//                    "Status", "Actions", "OPRLink", "ArchitecturalDecision", "Iteration", "Person", "Project"};

                Statement statement1 = connect.createStatement();

                for (int i = 0; i < 10; i++) {

                    for (int j = 0; j < entityNames.length; j++) {

                        try {

                            list = statement1.executeQuery("Select id From " + entityNames[j]);

                            while (list.next()) {
                                try {
                                    statement1.execute("Delete from " + entityNames[j] + "  WHERE ID = " + list.getInt(1));
                                } catch (Exception e) {
                                }
                            }
                        } catch (Exception e) {
                        }

                    }

//                    ResultSet list1 = statement1.executeQuery("Select ITERATION_ID From Iteration_Version");
//                    while (list1.next()) {
//                        try {
//                            statement1.execute("Delete from  Iteration_Version  WHERE ITERATION_ID = " + list1.getInt(1));
//                        } catch (Exception e) {
//                        }
//                    }

                    ResultSet list2 = statement1.executeQuery("Select PROJECT_ID From PROJECT_ITERATION");
                    while (list2.next()) {
                        try {
                            statement1.execute("Delete from  PROJECT_ITERATION  WHERE PROJECT_ID = " + list2.getInt(1));
                        } catch (Exception e) {
                        }
                    }

//                    ResultSet list3 = statement1.executeQuery("Select VERSION_ID From VERSION_REQUIREMENT");
//                    while (list3.next()) {
//                        try {
//                            statement1.execute("Delete from  VERSION_REQUIREMENT  WHERE VERSION_ID = " + list3.getInt(1));
//                        } catch (Exception e) {
//                        }
//                    }

                    ResultSet list4 = statement1.executeQuery("Select SEQ_NAME From SEQUENCE");
                    while (list4.next()) {
                        try {
                            statement1.execute("Delete from  SEQUENCE  WHERE SEQ_NAME = " + list4.getInt(1));
                        } catch (Exception e) {
                        }
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();

            } finally {
                close();
            }
        }
    }

    private void close() {
        try {
            if (list != null) {
                list.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connect != null) {
                connect.close();
            }
        } catch (Exception e) {
        }
    }

    public static void main(String[] args) {
        DatabaseCleaner clean = new DatabaseCleaner();
        clean.delete();
    }
}
