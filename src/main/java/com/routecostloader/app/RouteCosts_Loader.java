package com.routecostloader.app;

import com.vrptw.dao.ClientDao;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class RouteCosts_Loader {

    static String MAPQUEST_KEY = "jf5LPPtU2cMyidCPhPv9wVOMpjuKbPIX";
    static String HTTP_REST_POST_URL =  "http://www.mapquestapi.com/directions/v2/routematrix?key=" + MAPQUEST_KEY;
    static boolean PRINT_IN_TERMINAL = true;

    public static void main(String[] args) throws IOException {
        RESTServicesConsommer restServicesConsommer = new RESTServicesConsommer();

        // Load Datato processs
        List<String> clientIdLst = RouteCosts_Loader.loadClientIdToProcess();
        System.out.println("Total Clients to process (including depot): " + clientIdLst.size());

        //TODO: generate a list json to send to mapquest (from all to all: base-depot/loc-pnt1, base-depot/loc-pnt2,...,base-pntN-1/loc-pntN)
        //TODO: process calls to mapquest for each element in the list (json), TODO2: save json responses into files
        //TODO: process responses and generate (in file) insert queries
        //TODO: run insert queries

        // get json
/*        String baseLocLat = "38.75831", baseLoclong = "-8.30988";
        String locLat = "39.02721", locLong = "-8.77501";
        String jsonString = JSONProcessing.toJSON(new Location(baseLocLat,baseLoclong), new Location(locLat,locLong));
        System.out.println("JSON to send:" + jsonString);
*/
       // String resp = restServicesConsommer.POST(HTTP_REST_POST_URL, json);
        //System.out.println("Response:" +resp);


        JSONProcessing.loadJSON_Sample();


    }

    static List<String> loadClientIdToProcess(){
        List<String> clientIdLst = new ArrayList<>();
        try {
            clientIdLst.add( (new ClientDao()).getList("0").get(0).getIdClient() ); // get the depot

            // get clients with orderds from date A to date B
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            clientIdLst.addAll( (new ClientDao()).getListIDsBetweenDates(sdf.parse("2015-06-22"),sdf.parse("2015-06-27")));
        } catch (SQLException | ParseException e) {
            e.printStackTrace();
        }
        return clientIdLst;
    }

}
