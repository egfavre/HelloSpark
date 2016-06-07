package com.egfavre;

import spark.ModelAndView;
import spark.Session;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    static HashMap<String, User> users = new HashMap();
    //list of everyone who ever logged in
    static ArrayList<User> userList = new ArrayList<>();

    public static void main(String[] args) {
	Spark.init();
   Spark.get(
      "/", //name
            (request, response) -> {     //lambda (req and res)
                Session session = request.session();
                String username = session.attribute("username");
                HashMap m = new HashMap();
                if (username == null) {
                    return new ModelAndView(m, "login.html");
                }
                else {
                    m.put("name", username);
                    m.put("users", userList);
                    return new ModelAndView(m, "home.html");
                }
                },
           new MustacheTemplateEngine()
    );
    Spark.post(
            "/login",
            (request, response) -> {
                //redirect back to "/"
                //look into request and pull out object
                String username = request.queryParams("username");
                User user = users.get(username);
                if (user == null){
                    user = new User(username);
                    users.put(username, user);
                    userList.add(user);
                }

                //create session- generate cookie
                Session session = request.session();
                session.attribute("username", username);

                response.redirect("/");
                return "";
            }
    );
        Spark.post(
                "/logout",
                (request, response) -> {
                    Session session = request.session();
                    session.invalidate();
                    response.redirect("/");
                    return "";
                }
        );
    }
}
