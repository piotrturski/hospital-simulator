package net.piotrturski.edgelab.view;

import org.junit.Test;

import static net.piotrturski.edgelab.hospital.TestHospitalFactory.buildHospital;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

public class ConsoleControllerTest {

    ConsoleController consoleController = new ConsoleController(buildHospital());

    @Test
    public void should_process_input() {
        String output = consoleController.processInput("F", "P");

        assertThat(output).isEqualTo("F:0,H:1,D:0,T:0,X:0");
    }

    @Test
    public void should_handle_missing_2nd_parameter() {
        String output = consoleController.processInput("D,D");

        assertThat(output).isEqualTo("F:0,H:0,D:0,T:0,X:2");
    }

    @Test
    public void should_reject_wrong_input() {

        assertIllegalParameters();
        assertIllegalParameters("ERR");
        assertIllegalParameters("F", "ERR");
        assertIllegalParameters("F,");
        assertIllegalParameters("F", "P,");
        assertIllegalParameters("F", "P","3rd argument");
    }

    private void assertIllegalParameters(String... args) {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> consoleController.processInput(args));
    }
}