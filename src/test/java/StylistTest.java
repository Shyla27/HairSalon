import org.junit.*;
import org.sql2o.*;



public class StylistTest {

    @Before
    public void setUp(){
        DB.sql2o = new Sql2o("jdbc:postgresql://localhost:5432/hair_salon_test", "shyla","hair_salon");
    }

    @After
    public void tearDown(){
        try(Connection con = DB.sql2o.open()){
            String deleteClientQuery = "DELETE FROM clients*;";
            String deleteStylistQuery = "DELETE FROM stylists *;";
            con.createQuery(deleteClientQuery).executeUpdate();
            con.createQuery(deleteStylistQuery).executeUpdate();
        }
    }

}