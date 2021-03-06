import spark.ModelAndView;
 import java.util.ArrayList;
 import java.util.HashMap;
 import java.util.Map;


 
import static spark.Spark.*;


public class App {
   static int getHerokuAssignPort() {

        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567; //return default port if heroku-port isn't set (i.e. on localhost)
      }
    
        public static void main(String[] args) {
            port(getHerokuAssignPort());
            staticFileLocation("/public");
            
    
        String layout = "/templates/layout.vtl";
// get requests  to retrieve info 
        get("/", (request, response) -> {
                    Map<String, Object> model = new HashMap<String, Object>();
                    model.put("template", "templates/index.vtl");
                    model.put("salons", request.session().attribute("salons"));
                    return new ModelAndView(model, layout);

                },
                new VelocityTemplateEngine());


        get("/Stylist", (request, response) -> {
            Map<String, Object> model = new HashMap<String, Object>();
            model.put("template", "templates/Stylist.vtl");
            model.put("salons", request.session().attribute("salons"));
            return new ModelAndView(model, layout);
        }, new VelocityTemplateEngine());

        get("/Squadsuccess", (request, response) -> {
            Map<String, Object> model = new HashMap<String, Object>();
            model.put("salons", Stylist.all());
            model.put("template", "templates/display.vtl");
            return new ModelAndView(model, layout);
        }, new VelocityTemplateEngine());


        get("/display", (request, response) -> {
            Map<String, Object> model = new HashMap<String, Object>();
            model.put("salons", Stylist.all());
            model.put("template", "templates/display.vtl");
            return new ModelAndView(model, layout);
        }, new VelocityTemplateEngine());

        get("/index", (request, response) -> {
            Map<String, Object> model = new HashMap<String, Object>();
            model.put("salons", request.session().attribute("salons"));
            model.put("template", "templates/index.vtl");
            return new ModelAndView(model, layout);
        }, new VelocityTemplateEngine());


        get("/Client", (request, response) -> {
            Map<String, Object> model = new HashMap<String, Object>();
            model.put("salons", request.session().attribute("salons"));
            model.put("template", "templates/Client.vtl");
            return new ModelAndView(model, layout);
        }, new VelocityTemplateEngine());

        get("/Clientsdisplay", (request, response) -> {
            Map<String, Object> model = new HashMap<String, Object>();
            model.put("salons", Client.all());
            model.put("template", "templates/Clientsdisplay.vtl");
            return new ModelAndView(model, layout);
        }, new VelocityTemplateEngine());


        get("/deletesuccess", (request, response) -> {
            Map<String, Object> model = new HashMap<String, Object>();
            model.put("salons", request.session().attribute("salons"));
            model.put("template", "templates/deletesuccess.vtl");
            return new ModelAndView(model, layout);
        }, new VelocityTemplateEngine());


//Post to request change info

        
        post("/Success", (request, response) -> {
            Map<String, Object> model = new HashMap<String, Object>();
            String name = request.queryParams("name");
            String gender = request.queryParams("gender");
            String cname = request.queryParams("cname");
            Stylist newPerson = new Stylist(name, gender);
            newPerson.save();
            model.put("template", "templates/Success.vtl");
            return new ModelAndView(model, layout);
        }, new VelocityTemplateEngine());
        
        post("/clientsuccess", (request, response) -> {
            Map<String, Object> model = new HashMap<String, Object>();

            String name = request.queryParams("name");
            String gender = request.queryParams("gender");
            String cname = request.queryParams("name");
            Client newClient = new Client(name, gender, cname);
            newClient.save();
            model.put("template", "templates/clientsuccess.vtl");
            return new ModelAndView(model, layout);
        }, new VelocityTemplateEngine());

        post("/salons1", (request, response) -> {
            Map<String, Object> model = new HashMap<String, Object>();
            String sname = request.queryParams("sname");
            String sgender = request.queryParams("sgender");
            String cname = request.queryParams("sname");
            Client newClient = new Client(sname, sgender, sname);
            newClient.save();
            model.put("template", "templates/Clientsdisplay.vtl");
            return new ModelAndView(model, layout);
        }, new VelocityTemplateEngine());

        post("/delete-stylist", (request, response) -> {
            HashMap<String, Object> model = new HashMap<String, Object>();
            Stylist stylist = Stylist.find(Integer.parseInt(request.queryParams("id")));
            stylist.delete();
            model.put("salons", stylist);
            model.put("template", "templates/deletesuccess.vtl");
            return new ModelAndView(model, layout);
        }, new VelocityTemplateEngine());


        post("/delete-client", (request, response) -> {
            HashMap<String, Object> model = new HashMap<String, Object>();
            Client  client = Client.find(Integer.parseInt(request.queryParams("delete")));
            client.delete();
            model.put("salons", client);
            model.put("template", "templates/deleteclientsuccess.vtl");
            return new ModelAndView(model, layout);
        }, new VelocityTemplateEngine());

    }
}

