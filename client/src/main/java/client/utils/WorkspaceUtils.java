package client.utils;

import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
@Service
public class WorkspaceUtils {
    public List<Long> getBoardIds(Scanner host) {
        ArrayList<Long> result = new ArrayList<Long>();

        while(host.hasNextLine()) {
            result.add(Long.valueOf(host.nextLine()));
        }
        return result;
    }
    public void addBoardId(FileWriter hostWriter, long boardId) {
        PrintWriter writer = new PrintWriter(hostWriter);
        writer.println(boardId);
        writer.close();
    }
    public void removeBoardId(List<String> oldFile, FileWriter writeHost, long boardId) {
        PrintWriter printHost = new PrintWriter(writeHost);
        ArrayList<Long> temp = new ArrayList<>();
        for(String line: oldFile) {
            Long nextLine = Long.parseLong(line);
            if(nextLine != boardId) temp.add(nextLine);
        }
        System.out.println(temp.size());
        for(Long id : temp) {
            printHost.println(id);
        }
        printHost.close();
    }
    public List<Long> getFromFile(String host) throws FileNotFoundException {
        File file = new File("client/src/main/resources/workspaces/" + host);
        if(!file.exists()) return new ArrayList<>();
        List<Long> boardIds = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(file);
            boardIds = getBoardIds(scanner);
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        }
        return boardIds;
    }
    public void deleteFromFile(String host, long boardId) {
        File file = new File("client/src/main/resources/workspaces/" + host);
        try {
            List<String> oldFile = Files.readAllLines(file.toPath());
            FileWriter fileWriter = new FileWriter(file, false);
            removeBoardId(oldFile, fileWriter, boardId);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
