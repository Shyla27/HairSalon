import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.time.LocalDateTime;

public class StylistTest {
    @BeforeEach
    public void setUp() {
        DB.sql2o = new Sql2o("jdbc:postgresql://localhost:5432/hair_salon_test", "kosgei", "12345678");
    }

    @AfterEach
    public void tearDown() {
        try (Connection con = DB.sql2o.open()) {
            String sql = "DELETE FROM stylists *;";
            con.createQuery(sql).executeUpdate();
        }
    }

    @Test
    public void newStylist_instantiatesCorrectly() {
        Stylist newStyList = new Stylist("john", "doe", "johndoe@gmail.com", 30);
        assertTrue(newStyList instanceof Stylist);
    }

    @Test
    public void newStylist_getsFirstName_john() {
        Stylist newStyList = new Stylist("john", "doe", "johndoe@gmail.com", 30);
        assertEquals("john", newStyList.getFirstName());
    }

    @Test
    public void newStylist_getsSecondName_doe() {
        Stylist newStyList = new Stylist("john", "doe", "johndoe@gmail.com", 30);
        assertEquals("doe", newStyList.getSecondName());
    }

    @Test
    public void newStylist_getsEmail_johndoe() {
        Stylist newStyList = new Stylist("john", "doe", "johndoe@gmail.com", 30);
        assertEquals("johndoe@gmail.com", newStyList.getEmail());
    }

    @Test
    public void newStylist_getsAge_30() {
        Stylist newStyList = new Stylist("john", "doe", "johndoe@gmail.com", 30);
        assertEquals(30, newStyList.getAge());
    }

    @Test
    public void all_returnsAllInstancesOfStylist_true() {
        Stylist firstStyList = new Stylist("john", "doe", "johndoe@gmail.com", 30);
        firstStyList.save();
        Stylist secondStyList = new Stylist("john", "doe", "johndoe@gmail.com", 30);
        secondStyList.save();

        assertEquals(true, Stylist.all().get(0).equals(firstStyList));
        assertEquals(true, Stylist.all().get(1).equals(secondStyList));
    }

    @Test
    public void save_assignsIdToObject() {
        Stylist firstStyList = new Stylist("john", "doe", "johndoe@gmail.com", 30);
        firstStyList.save();
        Stylist savedStylist = Stylist.all().get(0);
        assertEquals(firstStyList.getId(), savedStylist.getId());
    }

    @Test
    public void getId_stylistInstantiateWithAnID() {
        Stylist firstStyList = new Stylist("john", "doe", "johndoe@gmail.com", 30);
        firstStyList.save();
        assertTrue(firstStyList.getId() > 0);
    }

    @Test
    public void find_returnsStyListWithSameId_secondStyList() {
        Stylist firstStyList = new Stylist("john", "doe", "johndoe@gmail.com", 30);
        firstStyList.save();
        Stylist secondStyList = new Stylist("jane", "doe", "janedoe@gmail.com", 30);
        secondStyList.save();
        assertEquals(Stylist.find(secondStyList.getId()), secondStyList);
    }

    @Test
    public void getClients_retrievesALlClientsFromDatabase_clientsList() {
        Stylist firstStyList = new Stylist("john", "doe", "johndoe@gmail.com", 30);
        firstStyList.save();

        Client newClient = new Client("jane", "doe", "0723320981", "janedoe@gmail.com", "Kenya", "Nairobi", firstStyList.getId());
        newClient.save();
        Client newClient2 = new Client("john", "doe", "0723320923", "johndoe@gmail.com", "Kenya", "Mombasa", firstStyList.getId());
        newClient2.save();

        Client[] clients = new Client[]{newClient, newClient2};

        assertTrue(firstStyList.getClients().containsAll(Arrays.asList(clients)));
    }

    @Test
    public void delete_deletesStylist_true() {
        Stylist firstStyList = new Stylist("john", "doe", "johndoe@gmail.com", 30);
        firstStyList.save();
        int newStylistId = firstStyList.getId();
        firstStyList.delete();
        assertEquals(null, Client.find(newStylistId));
    }

    @Test
    public void update_updatesStylist_true() {
        Stylist firstStyList = new Stylist("john", "doe", "johndoe@gmail.com", 30);
        firstStyList.save();
        firstStyList.update("jane", "doe", "johndoe@gmail.com", 30);
        assertEquals("jane", Stylist.find(firstStyList.getId()).getFirstName());
    }


}