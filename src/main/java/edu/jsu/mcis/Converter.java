package edu.jsu.mcis;

import au.com.bytecode.opencsv.*;
import org.json.simple.*;
import org.json.simple.parser.*;
import java.io.*;
import java.util.*;

public class Converter {

    /*
        Consider a CSV file like the following:
        
        ID,Total,Assignment 1,Assignment 2,Exam 1
        111278,611,146,128,337
        111352,867,227,228,412
        111373,461,96,90,275
        111305,835,220,217,398
        111399,898,226,229,443
        111160,454,77,125,252
        111276,579,130,111,338
        111241,973,236,237,500
        
        The corresponding JSON file would be as follows (note the curly braces):
        
        {
            "colHeaders":["ID","Total","Assignment 1","Assignment 2","Exam 1"],
            "rowHeaders":["111278","111352","111373","111305","111399","111160","111276","111241"],
            "data":[[611,146,128,337],
                    [867,227,228,412],
                    [461,96,90,275],
                    [835,220,217,398],
                    [898,226,229,443],
                    [454,77,125,252],
                    [579,130,111,338],
                    [973,236,237,500]
            ]
        }  
    */

    @SuppressWarnings("unchecked")
    //method for converting csv data to json format
    public static String csvToJson(String csvString) {
        
        //initialize list of string arrays
        List<String[]> csvList = null;
        
        try {
            //creates CSVReader and parses all the data to a list of string arrays
            CSVReader reader = new CSVReader(new StringReader(csvString));
            csvList = reader.readAll();
        }    
            catch (Exception e) {}
        
        //creates the string to be converted and adds the column headers to first line in json format
        String jsonResult = "{\n    \"colHeaders\":[";
        String[] colHeaders = csvList.get(0);
        for (int col = 0; col < colHeaders.length; col++) {
            jsonResult += "\"" + colHeaders[col] + "\"";
            if (col < colHeaders.length - 1) {
                jsonResult += ",";
            }
            else {
                jsonResult += "],\n";
            }
        }
        //creates string array for row headers and adds the first element from each array (ID) after the first
        //to this array
        String[] rowHeaders = new String[csvList.size()-1];
        for (int i = 0; i < csvList.size()-1; i++) {
            rowHeaders[i] = csvList.get(i+1)[0];
        }

        //appends the row headers to the result string in json format
        jsonResult += "    \"rowHeaders\":[";
        for (int row = 0; row < rowHeaders.length; row++) {
            jsonResult += "\"" + rowHeaders[row] + "\"";
            if (row < rowHeaders.length - 1) {
                jsonResult += ",";
            }
            else {
                jsonResult = jsonResult + "],\n";
            }
        }
        
        //appends the data to the result string in json format
        jsonResult = jsonResult + "    \"data\":[";
        for (int j = 1; j < rowHeaders.length+1; j++) {
            if (j == 1) {
                jsonResult += "[";
                for (int k = 1; k < colHeaders.length; k++) {
                    jsonResult += csvList.get(j)[k];
                    if (k == colHeaders.length-1) {
                    jsonResult += "],\n";
                    }
                    else {
                        jsonResult += ",";
                    }
                }
            }
            else if (j < rowHeaders.length) {
                jsonResult += "            [";
                for (int k = 1; k < colHeaders.length; k++) {
                    jsonResult += csvList.get(j)[k];
                    if (k == colHeaders.length-1) {
                        jsonResult += "],\n";
                    }
                    else  {
                        jsonResult += ",";
                    }
                }
            }
            else {
                jsonResult += "            [";
                for (int k = 1; k < colHeaders.length; k++) {
                    jsonResult += csvList.get(j)[k];
                    if (k == colHeaders.length-1) {
                        jsonResult += "]\n";
                        jsonResult += "    ]\n}";
                    }
                    else {
                        jsonResult += ",";
                    }
                }
            }
        }
        return jsonResult;    
    }
        
    @SuppressWarnings("unchecked")
    //method for converting json data to csv format
    public static String jsonToCsv(String jsonString) {
        //initializes a new JSONObject
        JSONObject jsonData = null;  

        //creates parser and parses the string passed through
        try { 
            JSONParser parser = new JSONParser();  
            jsonData = (JSONObject)parser.parse(jsonString);  
        }
            catch(Exception e) {}

        //initializes arrays of JSONObject data pertaining to their type (column, row, data)
        JSONArray colHeaders = (JSONArray) jsonData.get("colHeaders");  
        JSONArray rowHeaders = (JSONArray)jsonData.get("rowHeaders");
        JSONArray data = (JSONArray)jsonData.get("data");
        
        //creates the string to be converted and adds the column headers to the top line in csv format
        String csvResult = Converter.<String>joinArray((JSONArray) colHeaders) + "\n";  

        //adds the row headers and their corresponding line of data to the converted string in csv format
        for(int i = 0; i < rowHeaders.size(); i++) { 
            csvResult  = (csvResult + "\"" + (String)rowHeaders.get(i) + "\"," + Converter.<Long>joinArray((JSONArray) data.get(i)) + "\n");  
        }

        return csvResult;

    }
    
    @SuppressWarnings("unchecked")
    //method for joining JSONObject data from an array into a string in csv format
    private static <T> String joinArray(JSONArray array) {
        String line = "";
        for(int i = 0; i < array.size(); i++) {
            line = (line + "\"" + ((T) array.get(i)) + "\"");
            if(i < array.size() - 1) {
                line = line + ",";            
            }
        }
        return line;
    }
    
}
