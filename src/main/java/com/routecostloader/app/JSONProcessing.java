package com.routecostloader.app;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JSONProcessing {
    private static String directory = System.getProperty("user.dir");

    private JSONProcessing(){}

    public static boolean JSON2File(String clientIdFrom, String clientIdTo, String namef, String jsonString){

        try {
            String fileName = clientIdFrom + "_"+ clientIdTo + "_" + namef/*(new Date()).getTime()*/;
            File file = new File(directory+"/src/main/dbResources/json_mapquest/"+fileName+".json");
            FileUtils.writeStringToFile(file, jsonString);
            return true;
        } catch (IOException e) { // Error writing file
            e.printStackTrace();
        }
        return false;
    }

    public static String toJSON(Location baseLoc, Location loc){
        String message;
        JSONObject json = new JSONObject();

        JSONArray locationsArray = new JSONArray();

        // create latLng
        JSONObject location = new JSONObject();
        JSONObject item = new JSONObject();

        // BASE_LOC
        item.put("lng", baseLoc.getLocation_lng());
        item.put("lat", baseLoc.getLocation_lat());
        location.put("latLng", item);
        locationsArray .put(location);

        // LOC
        location = new JSONObject();
        item = new JSONObject();

        item.put("lng", loc.getLocation_lng());
        item.put("lat", loc.getLocation_lat());
        location.put("latLng", item);
        locationsArray .put(location);

        // insert all locations to the json
        json.put("locations", locationsArray);

        // Add options
        JSONObject option = new JSONObject();
        option.put("allToAll", true);
        json.put("options", option);

        message = json.toString();
        return message;
        /*return "{locations: [" +
                baseLoc.toString() + ", " +
                loc.toString() + ", " +
                "],   options: { allToAll: true}}".replace("\\","");
        */
    }


    public static double[][] loadJSONResponse_Stub() throws IOException {
        String filePath = directory + "/src/main/dbResources/json_mapquest/jsonSampleResp.json";
        String jsonString = readFile(filePath, StandardCharsets.UTF_8);
        return JSONProcessing.loadDistanceEtTimeFromJSON(jsonString);
    }

    /**
     * Parse a jsonString to get the distance and time arrays
     * @param jsonString A String containing data in json format
     * @return return A 2x2 matrix containing in the first array of the matrix the distance, and the time in the second.
     * @throws IOException
     */
    public static double[][] loadDistanceEtTimeFromJSON(String jsonString) throws IOException {
        double[][] parsedResp = new double[2][2];
        JSONObject input = new JSONObject(jsonString);
        System.out.println("JSON found:" + input);

        // GET DISTANCES
        JSONArray distance = input.getJSONArray("distance");

        JSONArray base_to_loc_arr = distance.getJSONArray(0);
        JSONArray loc_to_base_arr = distance.getJSONArray(1);

        double base_to_loc = base_to_loc_arr.getDouble(1);
        double loc_to_base = loc_to_base_arr.getDouble(0);

        if(RouteCosts_Loader.PRINT_IN_TERMINAL) {
            System.out.println("DISTANCE=>Base to base: " + base_to_loc_arr.getDouble(0) + ", Base to loc: " + base_to_loc);
            System.out.println("DISTANCE=>Loc to base: " + loc_to_base + ", Loc to loc: " + loc_to_base_arr.getDouble(1));
        }
        parsedResp[0][0] = base_to_loc;
        parsedResp[0][1] = loc_to_base;

        // GET TIMES
        JSONArray time  = input.getJSONArray("time");

        JSONArray t_base_to_loc_arr = time.getJSONArray(0);
        JSONArray t_loc_to_base_arr = time.getJSONArray(1);

        double t_base_to_loc = t_base_to_loc_arr.getDouble(1);
        double t_loc_to_base = t_loc_to_base_arr.getDouble(0);

        if(RouteCosts_Loader.PRINT_IN_TERMINAL) {
            System.out.println("TIME=> Base to base: " + t_base_to_loc_arr.getDouble(0) + ", Base to loc: " + t_base_to_loc);
            System.out.println("TIME=> Loc to base: " + t_loc_to_base + ", Loc to loc: " + t_loc_to_base_arr.getDouble(1));
        }
        parsedResp[1][0] = t_base_to_loc;
        parsedResp[1][1] = t_loc_to_base;

        return parsedResp;
    }

    static String readFile(String path, Charset encoding) throws IOException{
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

}
