package com.ii.testautomation.helper;

import com.ii.testautomation.dto.request.ProjectRequest;
import com.ii.testautomation.entities.Project;
import com.opencsv.CSVParser;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CsvHelper {
    public static String TYPE = "text/csv";
    static String[] HEADERs = { "Id", "Title", "Description", "Published" };

    public static boolean hasCSVFormat(MultipartFile file) {

        if (!TYPE.equals(file.getContentType())) {
            return false;
        }

        return true;
    }

//    public static List<ProjectRequest> csvToProject(InputStream is) {
//        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//             CSVParser csvParser = new CSVParser(fileReader,
//                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {
//
//            List<ProjectRequest> projectRequestList= new ArrayList<>();
//
//            Iterable<CSVRecord> csvRecords = csvParser.getRecords();
//
//            for (CSVRecord csvRecord : csvRecords) {
//                ProjectRequest projectRequest=new ProjectRequest();
//                projectRequest.setName(csvRecord.get("name"));
//                projectRequest.setName(csvRecord.get("description"));
//                projectRequest.setCode(csvRecord.get("code"));
//
//               projectRequestList.add(projectRequest);
//            }
//
//            return projectRequestList;
//        } catch (IOException e) {
//            throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
//        }
//    }
}
