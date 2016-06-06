package com.egfavre;

import spark.ModelAndView;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    static User user;
    //list of everyone who ever logged in
    static ArrayList<User> userList = new ArrayList<>();

    public static void main(String[] args) {
	Spark.init();
   Spark.get(
      "/", //name
            (request, response) -> {     //lambda (req and res)
                HashMap m = new HashMap();
                if (user == null) {
                    return new ModelAndView(m, "login.html");
                }
                else {
                    m.put("name", user.name);
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
                user = new User(username);
                userList.add(user);
                response.redirect("/");
                return "";
            }
    );
        Spark.post(
                "/logout",
                (request, response) -> {
                    user = null;
                    response.redirect("/");
                    return "";
                }
        );
    }
}
