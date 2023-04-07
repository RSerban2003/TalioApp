package server.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import server.database.BoardRepository;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


public class AdminControllerTest {

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    private AdminController adminController;

    @BeforeEach
    public void setUp() {
        adminController = new AdminController(boardRepository, messagingTemplate, "password");
    }

    @Test
    public void testSetPassword() {
        adminController.setPassword();

        assertNotNull(adminController.getPassword());
    }

    @Test
    public void testCheckPassWithCorrectPassword() {
        String password = "password";
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("pass", password);

        ResponseEntity<?> responseEntity = adminController.checkPass(requestBody);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue((Boolean) responseEntity.getBody());
    }

    @Test
    public void testCheckPassWithIncorrectPassword() {
        String password = "password";
        String incorrectPassword = "incorrect";
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("pass", incorrectPassword);

        ResponseEntity<?> responseEntity = adminController.checkPass(requestBody);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertFalse((Boolean) responseEntity.getBody());
    }

    @Test
    public void testCheckPassWithMissingPassword() {
        Map<String, String> requestBody = new HashMap<>();

        ResponseEntity<?> responseEntity = adminController.checkPass(requestBody);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testPassNoArgs(){
        new AdminController();
        boolean t = true;
        assertTrue(t);
    }
}
