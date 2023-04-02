package client.scenes;

import client.utils.WorkspaceUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.inject.Inject;
import java.io.*;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class WorkspaceUtilsTest {
    @Autowired
    private WorkspaceUtils workspaceUtils;
    @BeforeEach
    public void setup() {
        System.out.println(workspaceUtils);
    }
    @Test
    public void testWriteNewHost() {
        try {
            File file = new File("src/main/resources/workspaces/localhost");
            file.createNewFile();
            FileWriter writer = new FileWriter(file);
            workspaceUtils.addBoardId(writer, 1);
            Scanner scanner = new Scanner(file);
            assertEquals(Long.valueOf(scanner.nextLine()), 1);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
    @Test
    public void testWriteToExistingHost() {
        File file = new File("src/main/resources/workspaces/localhost");
        try{
            PrintWriter writer = new PrintWriter(new FileWriter(file));
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
