package net.piotrturski.edgelab;

import net.piotrturski.edgelab.view.ConsoleController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class SmokeTest {

    @Autowired ConsoleController consoleController;

    @Test
    public void should_autowire_and_start_application() throws Exception {
        assertThat(consoleController).isNotNull();
        new Application().commandLineRunner(consoleController).run("D,D");
    }
}