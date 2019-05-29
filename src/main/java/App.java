import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.sun.org.apache.xpath.internal.operations.Mod;

import spark.ModelAndView;

import static spark.Spark.*;

public class App{
    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567; //return default port if heroku-port isn't set (i.e. on localhost)
    }



    public static void main(String[] args){

        port(getHerokuAssignedPort());

        staticFileLocation("/public");
        String layout = "templates/layout.vtl";

        get("/", (request, response) -> {
            Map<String, Object> model = new HashMap<String, Object>();
            return new ModelAndView(model, "templates/index.vtl");
        }, new VelocityTemplateEngine());

        post("/stylists", (request, response) -> {
            Map<String, Object> model = new HashMap<String, Object>();
            String name = request.queryParams("name");
            int number = Integer.parseInt(request.queryParams("number"));
            int age = Integer.parseInt(request.queryParams("age"));
            String gender = request.queryParams("gender");
            Stylist stylist = new Stylist(name,number,age,gender);
            stylist.save();
            model.put("stylist", stylist);
            model.put("stylists", Stylist.all());
            model.put("template", "templates/stylists.vtl");
            return new ModelAndView(model, layout);
        }, new VelocityTemplateEngine());

        post("/clients", (request, response) -> {
            Map<String, Object> model = new HashMap<String, Object>();
            Stylist stylist = Stylist.find(Integer.parseInt(request.queryParams("stylistId")));

            String name = request.queryParams("clientname");
            int number = Integer.parseInt(request.queryParams("clientnumber"));
            String gender = request.queryParams("clientgender");

            String email = request.queryParams("email");
            client client = new client(name, number, email, stylist.getId(),gender);
            client.save();
            model.put("stylists", Stylist.all());
            model.put("stylist", stylist);
            model.put("client", client);
            model.put("clients", client.all());
            model.put("template", "templates/clients.vtl");
            return new ModelAndView(model,layout);
        }, new VelocityTemplateEngine());

        get("/form", (request, response) -> {
            Map<String, Object> model = new HashMap<String, Object>();
//            Stylist stylist = Stylist.find(Integer.parseInt(request.queryParams("stylistId")));
            model.put("stylists", Stylist.all());
//            model.put("stylist", stylist);
            model.put("template", "templates/form.vtl");
            return new ModelAndView(model, layout);
        }, new VelocityTemplateEngine());

        get("/stylists", (request, response) -> {
            Map<String, Object> model = new HashMap<String, Object>();
            model.put("stylists", Stylist.all());
            model.put("template", "templates/stylists.vtl");
            return new ModelAndView(model, layout);
        }, new VelocityTemplateEngine());

        get("/clients", (request, response) -> {
            Map<String, Object> model = new HashMap<String, Object>();
            model.put("clients", client.all());
            model.put("template", "templates/clients.vtl");
            return new ModelAndView(model, layout);
        }, new VelocityTemplateEngine());

        get("/stylists/:id",(request, response) -> {
            Map<String, Object> model = new HashMap<String, Object>();
            Stylist stylist = Stylist.find(Integer.parseInt(request.params(":id")));
            model.put("stylist", stylist);
            model.put("clients", client.all());
            model.put("template", "templates/stylist.vtl");
            return new ModelAndView(model, layout);
        }, new VelocityTemplateEngine());

        get("/clients/:id", (request, response) -> {
            Map<String, Object> model = new HashMap<String, Object>();
            client client = client.find(Integer.parseInt(request.params("id")));
            model.put("stylists", Stylist.all());
            model.put("client", client);
            model.put("template", "templates/client.vtl");
            return new ModelAndView(model,layout);
        }, new VelocityTemplateEngine());

        get("/stylists/:stylistId/clients/:id", (request, response) -> {
            Map<String, Object> model = new HashMap<String, Object>();
            Stylist stylist = Stylist.find(Integer.parseInt(request.params(":stylistId")));
            client client = client.find(Integer.parseInt(request.params(":id")));
            model.put("stylist", stylist);
            model.put("client", client);
            model.put("template", "templates/client.vtl");
            return new ModelAndView(model,layout);
        }, new VelocityTemplateEngine());

        post("/clients/:id/delete", (request, response) -> {
            Map<String, Object> model = new HashMap<String, Object>();
            client client = client.find(Integer.parseInt(request.params(":id")));
            client.deleteClient();
            model.put("clients", client.all());
            model.put("template", "templates/clients.vtl");
            return new ModelAndView(model,layout);
        }, new VelocityTemplateEngine());

        post("/clients/:id", (request, response) -> {
            Map<String, Object> model = new HashMap<String, Object>();
            Stylist stylist = Stylist.find(Integer.parseInt(request.queryParams("stylistId")));
            client client = client.find(Integer.parseInt(request.params(":id")));
            String name = request.queryParams("clientname");
            int number = Integer.parseInt(request.queryParams("clientnumber"));
            String gender = request.queryParams("clientgender");
            String email = request.queryParams("email");
            client.update(name,number,email,stylist.getId(),gender);
            String url = String.format("/clients/%d", client.getId());
            response.redirect(url);

            return new ModelAndView(model, layout);
        }, new VelocityTemplateEngine());

        post("/stylists/:id", (request, response) -> {
            Map<String,Object> model = new HashMap<String, Object>();
            Stylist stylist = Stylist.find(Integer.parseInt(request.params(":id")));
            String name = request.queryParams("name");
            int number = Integer.parseInt(request.queryParams("number"));
            int age = Integer.parseInt(request.queryParams("age"));
            String gender = request.queryParams("gender");
            stylist.update(name,number,age,gender);
            String url = String.format("/stylists/%d", stylist.getId());
            response.redirect(url);
            return new ModelAndView(model, layout);
        }, new VelocityTemplateEngine());

        post("/stylists/:id/delete", (request, response) -> {
            Map<String, Object> model = new HashMap<String, Object>();
            Stylist stylist = Stylist.find(Integer.parseInt(request.params(":id")));
            stylist.deleteStylist();
            model.put("stylists", Stylist.all());
            model.put("template", "templates/stylists.vtl");
            return new ModelAndView(model, layout);
        }, new VelocityTemplateEngine());
    }
}
