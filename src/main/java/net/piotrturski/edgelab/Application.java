package net.piotrturski.edgelab;

import net.piotrturski.edgelab.view.ConsoleController;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Profile("!test") // spring still can't mock input for a CommandLineRunner
    @Bean
    CommandLineRunner commandLineRunner(ConsoleController consoleController) {
        return args -> System.out.println(consoleController.processInput(args));
    }

}
