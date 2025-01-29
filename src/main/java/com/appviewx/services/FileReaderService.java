package com.appviewx.services;

import com.appviewx.model.primarydb.FileLine;
import com.appviewx.repos.FileLineJDBCRepository;
import com.appviewx.repos.primarydb.FileLineJPARepository;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class FileReaderService {
    private static final Logger LOGGER = LogManager.getLogger(FileReaderService.class);

    @Autowired
    private FileLineJDBCRepository fileLineJDBCRepo;

    @Autowired
    private FileLineJPARepository fileLineJPARepository;

    public void readDataFromFile(String filePath)  {
        InputStream fis = null;
        BufferedReader br = null;
        List<FileLine> fileLinesList = null;
        int batchSize = 1000;
        int numberOfBatches = 0;
        try {
            br = openFileStream(fis, br, filePath);
            String line = br.readLine();
            fileLinesList = new ArrayList<>();
            while(line != null) {
                fileLinesList.add(convertLineToFileLine(line));
                if(fileLinesList.size() % batchSize == 0) {
                    int[] saveResult = saveBatch(fileLinesList);
                    LOGGER.info("Current Batch Number : " + ++numberOfBatches + " and saveResult : {}" + saveResult);
                    fileLinesList.clear();
                }
                line = br.readLine();
            }

            if(fileLinesList.size() > 0) {
                ++numberOfBatches;
                int[] saveResult = saveBatch(fileLinesList);
                LOGGER.info("Last Batch Number " + numberOfBatches + " with size " + fileLinesList.size() + "Saved");
                LOGGER.info("Save Result : " + saveResult);
            }
        } catch(IOException ex) {
            LOGGER.error("Error encountered while opening the file or reading a line",ex);
        } finally {
            if(fis != null && br != null) {
                closeFileStream(fis, br);
            }
        }
    }

    private int[] saveBatch(List<FileLine> fileLinesList) {
        return fileLineJDBCRepo.batchInsert(fileLinesList);
    }

    private BufferedReader openFileStream(InputStream fis, BufferedReader br, String filePath) {
        try {
            fis = new FileInputStream(filePath);
            br = new BufferedReader(new InputStreamReader(fis));
        } catch (FileNotFoundException fnfe) {
            LOGGER.error("Error encountered while opening file",fnfe);
        }
        return br;
    }

    private void closeFileStream(InputStream fis, BufferedReader br) {
        try {
            br.close();
            fis.close();
        } catch (IOException ex) {
            LOGGER.error("Error while closing the buffered reader or input stream",ex);
        }

    }

    private FileLine convertLineToFileLine(String line) {
        List<String> fields = Arrays.asList(line.split(","));
        FileLine fileLine = new FileLine();
        fileLine.setColumn1(fields.get(0));
        fileLine.setColumn2(fields.get(1));
        fileLine.setColumn3(fields.get(2));

        return fileLine;
    }

    /*public List<FileLine> getLines() {
        //PageRequest.
        //fileLineJPARepository.
    }*/

}
