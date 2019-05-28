import org.junit.*;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.Arrays;

import static org.junit.Assert.*;

public class stylistTest{

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
    public void stylist_instantiatesCorrectly(){
        Stylist newStylist = new Stylist("Jessica", 01, 21, "female");
        assertEquals(true, newStylist instanceof Stylist);
    }

    @Test
    public void stylist_instantiatesWithAllDetails(){
        Stylist newStylist = new Stylist("Jessica", 01, 21, "female");
        assertEquals("Jessica", newStylist.getName());
        assertEquals(01, newStylist.getNumber());
        assertEquals(21, newStylist.getAge() );
        assertEquals("female", newStylist.getGender());
    }

    @Test
    public void all_returnsAllInstancesOfStylists_true(){
        Stylist firstStylist = new Stylist("Jessica", 01, 21, "female");
        firstStylist.save();
        Stylist secondStylist = new Stylist("James", 02, 25, "male");
        secondStylist.save();
        assertTrue(Stylist.all().contains(firstStylist));
        assertTrue(Stylist.all().contains(secondStylist));
    }

    @Test
    public void save_returnsTrue(){
        Stylist newStylist = new Stylist("Jessica", 01, 21, "female");
        newStylist.save();
        assertTrue(Stylist.all().get(0).equals(newStylist));
    }

    @Test
    public void find_returnsStylistWithSameId_true(){
        Stylist firstStylist = new Stylist("Jessica", 01, 21, "female");
        firstStylist.save();
        Stylist secondStylist = new Stylist("James", 02, 25, "male");
        secondStylist.save();
        assertEquals(secondStylist, Stylist.find(secondStylist.getId()));
    }

    @Test
    public void update_updatesStylistDetails(){
        Stylist newStylist = new Stylist("Jessica", 01, 21, "female");
        newStylist.save();
        newStylist.update("James", 02, 25, "male");
        Stylist updated = new Stylist("James", 02, 25, "male");
        assertEquals(updated.getName(), Stylist.find(newStylist.getId()).getName());
        assertEquals(updated.getAge(), Stylist.find(newStylist.getId()).getAge());
        assertEquals(updated.getGender(), Stylist.find(newStylist.getId()).getGender());
        assertEquals(updated.getNumber(), Stylist.find(newStylist.getId()).getNumber());
    }

    @Test
    public void getClients_returnsAllClientsForSpecificStylist(){
        Stylist stylist = new Stylist("Jessica", 01, 21, "female");
        stylist.save();
        client firstClient = new client("Mary", 01,"email",stylist.getId(),"male");
        firstClient.save();
        client secondClient = new client("John", 02, "email",stylist.getId(),"male");
        secondClient.save();
        client[] clients = new client[] {firstClient,secondClient};
        assertTrue(stylist.getClients().containsAll(Arrays.asList(clients)));
    }

    @Test
    public void delete_removesAStylistWithItsClients(){
        Stylist stylist = new Stylist("Jessica", 01, 21, "female");
        stylist.save();
        client firstClient = new client("Mary", 01,"email",stylist.getId(),"male");
        firstClient.save();
        client secondClient = new client("John", 02, "email",stylist.getId(),"male");
        secondClient.save();
        int stylistid = stylist.getId();
        int clientOneId = firstClient.getId();
        int clientTwoId = secondClient.getId();
        stylist.deleteStylist();
        assertEquals(null, Stylist.find(stylistid));
        assertEquals(null, client.find(clientOneId));
        assertEquals(null, client.find(clientTwoId));
    }

    @Test
    public void count_returnsTheNumberOfStylists_int(){
        Stylist firstStylist = new Stylist("Jessica", 01, 21, "female");
        firstStylist.save();
        Stylist secondStylist = new Stylist("James", 02, 25, "male");
        secondStylist.save();
        assertEquals(2, Stylist.getCount());
    }

    @Test
    public void search_returnSearchedStylist_Stylist(){
        Stylist firstStylist = new Stylist("Jessica", 01, 21, "female");
        firstStylist.save();
        Stylist secondStylist = new Stylist("James", 02, 25, "male");
        secondStylist.save();
        Stylist searched = Stylist.search("Ja");
        assertEquals(searched.getName(), secondStylist.getName());
    }
}