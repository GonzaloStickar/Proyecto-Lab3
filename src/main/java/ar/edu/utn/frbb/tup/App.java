package ar.edu.utn.frbb.tup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@Controller
@RestController
public class App
{
    public static void main( String[] args )
    {
        SpringApplication.run(App.class);
    }
}
