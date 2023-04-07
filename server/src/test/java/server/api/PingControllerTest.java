package server.api;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class PingControllerTest {

    @Test
    public void testPong() {
        PingController controller = new PingController();
        String response = controller.pong();
        assertEquals("pong", response);
    }
}