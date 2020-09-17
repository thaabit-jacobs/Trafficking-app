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

            
            get("/search", (req, res) -> {
                Map<String, Object> map = new HashMap<>();
                int longCount = jdbi.withHandle(h -> h.createQuery("select count(street) from persons where street='Loop Street';").mapTo(int.class).findOnly());
               
                int BreeCount = jdbi.withHandle(h -> h.createQuery("select count(street) from persons where street='Bree Street';").mapTo(int.class).findOnly());
                
                int BreeCount = jdbi.withHandle(h -> h.createQuery("select count(street) from persons where street='Bree Street';").mapTo(int.class).findOnly());
                
                map.put("longCount", longCount);
                map.put("BreeCount", BreeCount);
                
                return new ModelAndView(map, "searchDemo.handlebars");

            }, new HandlebarsTemplateEngine());

            post("/search", (req, res) -> {
                Map<String, Object> map = new HashMap<>();
                


                //System.out.println(location);

                return new ModelAndView(map, "searchDemo.handlebars");

            }, new HandlebarsTemplateEngine());
            
            post("/success", (req, res) -> {
                Map<String, Object> map = new HashMap<>();
                
                
                String witnessFirstName = req.queryParams("witness_firstname");
                String witnessLastName = req.queryParams("witness_lastname");
                String witnessContact = req.queryParams("witness_phone");
                
                String victimFirstName = req.queryParams("victim_firstname");
                String victimLastName = req.queryParams("victim_lastname");
                String victimAge = req.queryParams("age");
                
                
                String gender = req.queryParams("gender");
                
                String street = req.queryParams("street");
                
                String date = req.queryParams("bdate");
                
                System.out.println(witnessFirstName);
                System.out.println(witnessLastName);
                System.out.println(witnessContact);
                System.out.println(victimFirstName);
                System.out.println(victimLastName);
                System.out.println(victimAge);
                System.out.println(gender);
                System.out.println(street);
                System.out.println(date);
                

                jdbi.useHandle(h -> {
                    h.execute("insert into persons (first_name, last_name, age, gender, street, date_incident) values (?, ?, ?, ?, ?, ?)",
                    		victimFirstName,
                    		victimLastName,
                    		Integer.parseInt(victimAge),
                    		gender,
                    		street,
                    		date);
                });
                
                return new ModelAndView(map, "success.handlebars");

            }, new HandlebarsTemplateEngine());

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }}
        
//class GetStreets{
//
//}