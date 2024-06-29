package com.SGACars.CarRentalApplication;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.*;
import java.util.*;

@Controller
public class CarsController {
    @GetMapping("/")
    public String func(){
         return "login";
     }
     
    @PostMapping("/submit")
    public String submit(@RequestParam("button") String button,@RequestParam("uId") String userId, @RequestParam("upwd") String password){
        //System.out.println("Inside submit method");
        if(button.equals("button1")){
            Connection connection =null;
            try {
                System.out.println("Inside submit method and userid is "+userId);
                connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3305/CarRentalApplication", "root", "kousikroot");
                String sql = "select password from Users where userId=?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1,userId);
                ResultSet rs = statement.executeQuery();
                if(rs.next()) {
                    System.out.println("The dbpass is " + rs.getString("password") + " user pass is " + password);
                    if (password.equals(rs.getString("password"))) {
                        return "Dashboard";
                    } else {
                        System.out.println("Wrong credentials");
                    }
                }

            }catch (Exception e){
                System.out.println("Exception is "+e);
            }

        }
        return "signup";

    }
    @PostMapping("/signup")
    public String signup(@RequestParam("button") String button,@RequestParam("sname") String username, @RequestParam("smail") String userId, @RequestParam("spwd") String password, @RequestParam("add") String address){
        System.out.println("Inside signup method");
        System.out.println("The attributes are "+ username+" "+userId+" "+password+" "+address);
        Connection connection = null;
        try{
            connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3305/CarRentalApplication","root","kousikroot");
            String sql= "insert into Users values(?,?,?,?)";
            PreparedStatement pstatement = connection.prepareStatement(sql);
            pstatement.setString(1,username);
            pstatement.setString(2,userId);
            pstatement.setString(3,password);
            pstatement.setString(4,address);
            pstatement.execute();
            System.out.println("Database updated successfully");
            connection.close();
        }catch(Exception e){
            System.out.println("The exception occured is "+e);
        }
        return "login";
    }
}

@GetMapping("/bookCarRental")
public String bookCarRental(@RequestParam(name = "viewType", defaultValue = "table") String viewType,  Model model){
    List<Map<String,Object>> data = fetchFlight();
    model.addAttribute("flightList",data);
    model.addAttribute("viewType", viewType);
    return "viewCars";
}
@ModelAttribute("flightList")
public List<Map<String,Object>> fetchFlight(){
    List<Map<String,Object>> listofCars = new ArrayList<>();
    System.out.println(("The username is "+userId));
    try (Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3305/CarRentalApplication", "root", "kousikroot")) {
        String sql = "select * from Cars";
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            Map<String,Object> mp = new HashMap<>();
            mp.put("Flightname", rs.getString("carmodel"));
            mp.put("Flightnumber", rs.getString("fromcity"));
            mp.put("FromCity", rs.getString("tocity"));
            mp.put("ToCity", rs.getString("startdate"));
            mp.put("Date", rs.getDate("enddate"));
            mp.put("Price", rs.getInt("price"));
            listofCars.add(mp);
        }
        System.out.println("The map data are "+listofCars);
    } catch (SQLException e) {
        System.out.println("The exception occurred: " + e);
    }
    return listofCars;
}
