package co.projectcodex.hackathon;

import java.util.List;

import org.jdbi.v3.core.Jdbi;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

public class App {

    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567;
    }

    static Jdbi getJdbiDatabaseConnection(String defualtJdbcUrl) throws URISyntaxException, SQLException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        String database_url = processBuilder.environment().get("DATABASE_URL");
        if (database_url != null) {

            URI uri = new URI(database_url);
            String[] hostParts = uri.getUserInfo().split(":");
            String username = hostParts[0];
            String password = hostParts[1];
            String host = uri.getHost();

            int port = uri.getPort();

            String path = uri.getPath();
            String url = String.format("jdbc:postgresql://%s:%s%s", host, port, path);

            return Jdbi.create(url, username, password);
        }

        return Jdbi.create(defualtJdbcUrl);

    }

    public static void main(String[] args) {
        try  {
        	
            staticFiles.location("/public");
            port(getHerokuAssignedPort());

            Jdbi jdbi = getJdbiDatabaseConnection("jdbc:postgresql://localhost/trackerdb?user=thaabit&password=1234");


            get("/", (req, res) -> {
                Map<String, Object> map = new HashMap<>();

                return new ModelAndView(map, "index.handlebars");

            }, new HandlebarsTemplateEngine());
            
            get("/report", (req, res) -> {
                Map<String, Object> map = new HashMap<>();
                
                return new ModelAndView(map, "reportform.handlebars");

            }, new HandlebarsTemplateEngine());
            
            post("/report", (req, res) -> {
                Map<String, Object> map = new HashMap<>();
                
                String witnessFirstName = req.queryParams("witness_firstname");
                String witnessLastName = req.queryParams("witness_lastname");
                String witnessContact = req.queryParams("witness_phone");
                
                String victimFirstName = req.queryParams("victim_firstname");
                String victimLastName = req.queryParams("victim_lastname");
                String victimAge = req.queryParams("age");
                
                String area = req.queryParams("location");
                
                String date = req.queryParams("bdate");
                String time = req.queryParams("btime");
                
                String descrip = "test description";
                

                jdbi.useHandle(h -> {
                    h.execute("insert into witness (first_name, last_name, contact) values (?, ?, ?)",
                    		witnessFirstName,
                    		witnessLastName,
                    		witnessContact);
                });
                
                jdbi.useHandle(h -> {
                    h.execute("insert into victim (first_name, last_name, age) values (?, ?, ?)",
                    		victimFirstName,
                    		victimLastName,
                    		Integer.parseInt(victimAge));
                });
                
                
                
                return new ModelAndView(map, "reportform.handlebars");

            }, new HandlebarsTemplateEngine());
/*
            post("/person", (req, res) -> {

                String firstName = req.queryParams("firstName");
                String lastName = req.queryParams("lastName");
                String email = req.queryParams("email");

                jdbi.useHandle(h -> {
                    h.execute("insert into users (first_name, last_name, email) values (?, ?, ?)",
                            firstName,
                            lastName,
                            email);
                });

                res.redirect("/");
                return "";
            });*/

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }}
        
