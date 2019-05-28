import org.junit.*;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import static org.junit.Assert.*;

public class clientTest{

    @Before
    public void setUp(){
        DB.sql2o = new Sql2o("jdbc:postgresql://localhost:5432/hair_salon_test", "mbugua", "hair");
    }

    @After
    public void tearDown(){
        try(Connection con = DB.sql2o.open()){
            String sqlClient = "DELETE FROM clients *;";
            String sqlStylist = "DELETE FROM stylists *;";
            con.createQuery(sqlClient).executeUpdate();
            con.createQuery(sqlStylist).executeUpdate();
        }
    }

    @Test
    public void client_instantiatesCorrectly_true() {
        client newClient = new client("Mary", 01,"email",1, "female");
        assertEquals(true, newClient instanceof client);
    }

    @Test
    public void client_instantiatesWithName_String(){
        client newClient = new client("Mary", 01,"email", 1,"female");
        assertEquals("Mary", newClient.getName());
    }

    @Test
    public void client_instantiatesWithNumber_Integer(){
        client newClient = new client("Mary", 01,"email",1,"female");
        assertEquals(01, newClient.getNumber());
    }

    @Test
    public void client_instantiatesWithEmail_String(){
        client newClient = new client("Mary", 01,"email", 1,"female");
        assertEquals("email", newClient.getEmail());
    }

    @Test
    public void all_ReturnsAllInstancesOfClient_true(){
        client firstClient = new client("Mary", 01,"email",1,"female");
        firstClient.save();
        client secondClient = new client("John", 02, "email",1,"male");
        secondClient.save();
        assertEquals(true, client.all().contains(firstClient));
        assertEquals(true, client.all().contains(secondClient));
    }

    @Test
    public void save_returnsTrue(){
        client newClient = new client("Mary", 01,"email",1,"female");
        newClient.save();
        assertTrue(client.all().get(0).equals(newClient));

    }

    @Test
    public void find_returnsInstanceOfSpecificId_true(){
        client firstClient = new client("Mary", 01,"email",1, "female");
        firstClient.save();
        client secondClient = new client("John", 02, "email",1,"male");
        secondClient.save();
        assertTrue(client.find(secondClient.getId()).equals(secondClient));
    }

    @Test
    public void update_updatesAClientDetails_true(){
        client newClient = new client("Mary", 01,"email",1,"female");
        newClient.save();
        newClient.update("John", 02, "email",2,"male");
        client updated = new client("John", 02, "email",2,"male");
        assertEquals(updated.getName(),client.find(newClient.getId()).getName() );
        assertEquals(updated.getEmail(),client.find(newClient.getId()).getEmail() );
        assertEquals(updated.getNumber(), client.find(newClient.getId()).getNumber());
        assertEquals(updated.getStylistId(), client.find(newClient.getId()).getStylistId());
        assertEquals(updated.getGender(), client.find(newClient.getId()).getGender());
    }

    @Test
    public void delete_removesAClientFromTheDB_true(){
        client newClient = new client("Mary", 01, "email", 1, "female");
        newClient.save();
        int clientId = newClient.getId();
        newClient.deleteClient();
        assertEquals(null, client.find(clientId));
    }

    @Test
    public void count_returnsNumberOfClients_int(){
        client firstClient = new client("Mary", 01, "email", 1, "female");
        firstClient.save();
        client secondClient = new client("John", 02, "email", 1, "male");
        secondClient.save();
        assertEquals(2, client.getCount());
    }

    @Test
    public void search_returnsSearchForClient_Client(){
        client firstClient = new client("Mary", 01, "email", 1, "female");
        firstClient.save();
        client secondClient = new client("John", 02, "email", 1, "male");
        secondClient.save();
        client searched = client.search("Jo");
        assertEquals(searched.getName(), secondClient.getName());
    }
}
