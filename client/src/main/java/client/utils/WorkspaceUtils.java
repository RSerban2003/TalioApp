package client.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class WorkspaceUtils {
    public List<Long> getBoardIds(String hostName) {
        ArrayList<Long> result = new ArrayList<Long>();
        File host = new File("@/workspaces/" + hostName);
        if(!host.exists()) return result;
        
        try {
            Scanner scanner = new Scanner(host);
            while(scanner.hasNextLine()) {
                result.add(Long.valueOf(scanner.nextLine()));
            }
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        }
        return result;
        
    }
    public void addBoardId(String hostName, long boardId) {
        try {
            FileWriter fileWriter = new FileWriter("@/workspaces/" + hostName);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.println(boardId);
            printWriter.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
    public void removeBoardId(String hostName, long boardId) {
        File inputFile = new File("@/workspaces/" + hostName);
        File temp = new File("@/workspaces/temp");
        if(!inputFile.exists()) return;

        try {
            PrintWriter writer = new PrintWriter(new FileWriter(temp));
            Scanner scanner = new Scanner(inputFile);
            while(scanner.hasNextLine()) {
                Long nextLine = Long.valueOf(scanner.nextLine());
                if(nextLine != boardId) writer.println(nextLine);
            }
            writer.close();
            temp.renameTo(inputFile);
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
