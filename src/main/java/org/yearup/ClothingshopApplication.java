package org.yearup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ClothingshopApplication
{



    public static void main(String[] args) {

        if(args.length !=2) {
            System.out.println("2 arguments are needed: username and password");
        }

        System.setProperty("username", args[0]);
        System.setProperty("password", args[1]);

        SpringApplication.run(ClothingshopApplication.class, args);
    }

}
