package client.utils;

import org.checkerframework.checker.units.qual.A;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.io.*;
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
        for(Long Id : temp) {
            printHost.println(Id);
        }
        printHost.close();
    }
}
