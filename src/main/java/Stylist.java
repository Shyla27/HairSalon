import org.sql2o.*;

import java.util.List;

public class Stylist{
    private String name;
    private int number;
    private int id;
    private int age;
    private String gender;

    public Stylist(String name, int number, int age, String gender){
        this.name = name;
        this.number = number;
        this.age = age;
        this.gender = gender;
    }

    public String getName(){
        return name;
    }
    public int getNumber(){
        return number;
    }
    public int getAge(){
        return age;
    }
    public String getGender(){
        return gender;
    }
    public int getId(){
        return id;
    }

    @Override
    public boolean equals(Object otherStylist){
        if(!(otherStylist instanceof Stylist)){
            return false;
        } else {
            Stylist newStylist = (Stylist) otherStylist;
            return this.getName().equals(newStylist.getName()) &&
                    this.getNumber() == newStylist.getNumber() &&
                    this.getAge() == newStylist.getAge() &&
                    this.getGender().equals(newStylist.getGender());
        }
    }

    public static List<Stylist> all(){
        try(Connection con = DB.sql2o.open()){
            String sql = "SELECT id, name, number, age, gender FROM stylists;";
           return con.createQuery(sql).executeAndFetch(Stylist.class);
        }
    }

    public void save(){
        try(Connection con = DB.sql2o.open()){
            String sql = "INSERT INTO stylists (name, number, age, gender) VALUES (:name, :number, :age, :gender) ";
           this.id = (int) con.createQuery(sql,true)
                    .addParameter("name", name)
                    .addParameter("number", number)
                    .addParameter("age", age)
                    .addParameter("gender", gender)
                   .executeUpdate()
                   .getKey();

        }
    }

    public static Stylist find(int id){
        try(Connection con = DB.sql2o.open()){
            String sql = "SELECT * FROM stylists WHERE id=:id";
            Stylist stylist = con.createQuery(sql)
                    .addParameter("id", id)
                    .executeAndFetchFirst(Stylist.class);
            return stylist;
        }
    }

    public void update(String name, int number, int age, String gender){
        try(Connection con = DB.sql2o.open()){
            String sql = "UPDATE stylists SET name = :name, number = :number, age = :age, gender = :gender WHERE id = :id";
            con.createQuery(sql)
                    .addParameter("name", name)
                    .addParameter("number", number)
                    .addParameter("age", age)
                    .addParameter("gender", gender)
                    .addParameter("id", id)
                    .executeUpdate();
        }
    }

    public List<client> getClients(){
        try(Connection con = DB.sql2o.open()){
            String sql = "SELECT * FROM clients WHERE stylistId =:id";
           return con.createQuery(sql)
                    .addParameter("id", id)
                    .executeAndFetch(client.class);

        }
    }

    public void deleteStylist(){
        try(Connection con = DB.sql2o.open()){
            String sqlStylist = "DELETE FROM stylists WHERE id = :id";
            String sqlClients = "DELETE FROM clients WHERE stylistId = :id";
            con.createQuery(sqlClients)
                    .addParameter("id", id)
                    .executeUpdate();
            con.createQuery(sqlStylist)
                    .addParameter("id", id)
                    .executeUpdate();

        }
    }

    public static int getCount(){
        return Stylist.all().size();
    }

    public static Stylist search(String name){
        try(Connection con = DB.sql2o.open()){
            String sql = "SELECT * FROM stylists WHERE name LIKE :name";
          return  con.createQuery(sql)
                    .addParameter("name", name + "%")
                  .executeAndFetchFirst(Stylist.class);

        }
    }
}