package co.projectcodex.hackathon;

import java.util.List;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.Query;

import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
        }
            

        return Jdbi.create(defualtJdbcUrl);

    }

    public static void main(String[] args) {
    	List<String> streets = new ArrayList<>();
    	streets.add("Long street");
    	streets.add("Bree street");
    	streets.add("Loop street");
    	streets.add("Kloof street");
    	streets.add("Adderley street");
    	streets.add("Lower main road, Observatory");
   
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
                
                String location = req.queryParams("location").toLowerCase();
                
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
                
                jdbi.useHandle(h -> {
                    h.execute("insert into incident (incident_time, descrip) values (?, ?)",
                    		LocalDate.parse(date),
                    		victimLastName);
                });
                
                List<String> areaCount = jdbi.withHandle(handle ->
                handle.createQuery("select * from area where name = ?")
                .bind(0, location)
                .mapTo(String.class)
                .list());
                
                if(areaCount.size() == 0)
                {
                    jdbi.useHandle(h -> {
                        h.execute("insert into area (name, incident_count) values (?, ?)",
                        		location,
                        		1);
                    });
                } else
                {
                    jdbi.useHandle(h -> {
                        h.execute("update area set incident_count = incident_count+1 where name = ?",
                        		location);
                    });
                }
                
                List<String> areaId = jdbi.withHandle(handle ->
                handle.createQuery("select id from area where name = ?")
                .bind(0, location)
                .mapTo(String.class)
                .list());
                
                int areaIdValue = Integer.parseInt(areaId.get(0));
                
                
                return new ModelAndView(map, "success.handlebars");

            }, new HandlebarsTemplateEngine());

            
            get("/search", (req, res) -> {
                Map<String, Object> map = new HashMap<>();
                
                return new ModelAndView(map, "searchDemo.handlebars");

            }, new HandlebarsTemplateEngine());

            post("/search", (req, res) -> {
                Map<String, Object> map = new HashMap<>();
                
                String location = req.queryParams("location");

                //System.out.println(location);

                return new ModelAndView(map, "searchDemo.handlebars");

            }, new HandlebarsTemplateEngine());
            
            get("/success", (req, res) -> {
                Map<String, Object> map = new HashMap<>();
                
                return new ModelAndView(map, "success.handlebars");

            }, new HandlebarsTemplateEngine());

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }}
        
