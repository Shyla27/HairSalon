import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.time.LocalDateTime;

class ClientTest {
    Client newClient, newClient2;

    @BeforeEach
    public void setUp() {
        DB.sql2o = new Sql2o("jdbc:postgresql://localhost:5432/hair_salon_test", "kosgei", "12345678");
        newClient = new Client("jane","doe","0723320981","janedoe@gmail.com","Kenya","Nairobi",1);
        newClient2 = new Client("john","doe","0723320923","johndoe@gmail.com","Kenya","Mombasa",1);
    }
    @AfterEach
    public void tearDown() {
        try (Connection con = DB.sql2o.open()) {
            String sql = "DELETE FROM clients *;";
            con.createQuery(sql).executeUpdate();
        }
    }
    @Test
    public void newClient_instantiatesCorrectly() {
        assertTrue(newClient instanceof Client);
    }

    @Test
    public void newClient_getsFirstName_jane()
    {
        assertEquals("jane", newClient.getFirst_name());
    }

    @Test
    public void newClient_getsLastName_doe()
    {
        assertEquals("doe", newClient.getLast_name());
    }
    @Test
    public void newClient_getsPhone_0723320981()
    {
        assertEquals("0723320981",newClient.getPhone());
    }

    @Test
    public void newClient_getsEmail_janedoe()
    {
        assertEquals("janedoe@gmail.com", newClient.getEmail());
    }

    @Test
    public void newClient_getsCountry_Kenya()
    {
        assertEquals("Kenya", newClient.getCountry());
    }

    @Test
    public void newClient_getsCounty_Nairobi()
    {
        assertEquals("Nairobi", newClient.getCounty());
    }

    @Test
    public void newClient_getsStylistId_1()
    {
        assertEquals(1, newClient.getStylistid());
    }

    @Test
    public void all_returnsAllInstancesOfClients_true() {
       newClient.save();
       newClient2.save();

        assertEquals(true, Client.all().get(0).equals(newClient));
        assertEquals(true, Client.all().get(1).equals(newClient2));
    }

    @Test
    public void save_assignsIdToObject() {
        newClient.save();
        Client savedClient = Client.all().get(0);
        assertEquals(newClient.getId(), savedClient.getId());
    }

    @Test
    public void getId_clientInstantiateWithAnID() {
        newClient.save();
        assertTrue(newClient.getId() > 0);
    }

    @Test
    public void find_returnsClientWithSameId_newClient2() {
        newClient.save();
       newClient2.save();

        assertEquals(Client.find(newClient2.getId()), newClient2);
    }

    @Test
    public void delete_deletesClient_true() {
        newClient.save();
        int newClientId =newClient.getId();
        newClient.delete();
        assertEquals(null, Client.find(newClientId));
    }

    @Test
    public void update_updatesClient_true() {
        newClient.save();
        newClient.update("chris", "sean","0933772341","johndoe@gmail.com", "kenya", "kisumu",1);
        assertEquals("chris", Client.find(newClient.getId()).getFirst_name());
    }

}
