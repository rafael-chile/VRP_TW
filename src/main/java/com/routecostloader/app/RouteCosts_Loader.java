package com.routecostloader.app;

import com.vrptw.dao.ClientDao;
import com.vrptw.entities.Client;
import com.vrptw.entities.RouteCost;

import java.io.*;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class RouteCosts_Loader {


    private static Logger logger = Logger.getLogger( RouteCosts_Loader.class.getName() );


    static String MAPQUEST_KEY = "jf5LPPtU2cMyidCPhPv9wVOMpjuKbPIX";
    static String HTTP_REST_POST_URL =  "http://www.mapquestapi.com/directions/v2/routematrix?key=" + MAPQUEST_KEY;
    static boolean PRINT_IN_TERMINAL = true;
    static boolean IS_TEST = true;

    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws IOException {
        RESTServicesConsumer restServicesConsumer = new RESTServicesConsumer();

        String loggerFile = System.getProperty("user.dir")+"/src/main/java/com/routecostloader/app/logs/rc-log.%u.%g.log";
        logger.addHandler(new FileHandler(loggerFile , 1024 * 1024, 10, true));

        // LOAD DATA TO PROCESS
        List<JsonToPost> locJsonLst = null;
        String fileName = System.getProperty("user.dir") + "/src/main/dbResources/json_mapquest/jsonLocReqList";
        File file = new File(fileName);

        if( !file.exists() ) {
            /* Generate a list of json request to send to mapquest
                (from all to all: base-depot/loc-pnt1, base-depot/loc-pnt2,...,base-pntN-1/loc-pntN) */
            List<Client> clientIdLst = RouteCosts_Loader.loadClientIdToProcess();
            System.out.println("Total Clients to process (including depot): " + clientIdLst.size());

            locJsonLst = RouteCosts_Loader.generateJsonToPost(clientIdLst);
            locJsonLst.stream().forEach(msg -> logger.info(msg.toString()));
            saveList(locJsonLst, fileName);
            System.out.println("Total Clients saved infile: " + locJsonLst.size());

        }else{
            try {
                locJsonLst = RouteCosts_Loader.readList(fileName);
                System.out.println("Total Clients found in filed list: " + locJsonLst.size());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        //TODO: process calls to mapquest for each element in the list (json), TODO2: save json responses into files
        String jsonRespFileName = System.getProperty("user.dir") + "/src/main/dbResources/json_mapquest/jsonLocRespList";
        if( !IS_TEST ){
            String errorOn = "";
            try {
                for(JsonToPost jsonReq : locJsonLst) {
                    errorOn = jsonReq.toString();
                    String resp = restServicesConsumer.POST(HTTP_REST_POST_URL, jsonReq.getJsonRequest());
                    jsonReq.setJsonResponse(resp);
                }
                saveList(locJsonLst, jsonRespFileName);
            } catch (IOException e1) {
                e1.printStackTrace();
                logger.info("ERROR while processing :> " + errorOn);
                saveList(locJsonLst, jsonRespFileName); // the post returned an error, so it saves the responses list
            }
        }

        // get distance and time to insert into db
        List<RouteCost> routeCostsList = new ArrayList<>();
        locJsonLst.stream().forEach(jsonToPost->{
            try {
                double[][] distanceEtTime =  IS_TEST ? JSONProcessing.loadJSONResponse_Stub()
                        : JSONProcessing.loadDistanceEtTimeFromJSON(jsonToPost.getJsonResponse());

                String comments = "Processed at (" + new Date() + ") with: " + jsonToPost.getFromClient() + " & " + jsonToPost.getToClient();

                RouteCost rc_base_to_loc = new RouteCost(jsonToPost.getFromClient(), jsonToPost.getToClient(),
                        distanceEtTime[1][0], distanceEtTime[0][0], comments);
                routeCostsList.add(rc_base_to_loc);

                RouteCost rc_loc_to_base = new RouteCost(jsonToPost.getFromClient(), jsonToPost.getToClient(),
                        distanceEtTime[1][1], distanceEtTime[0][1], comments);
                routeCostsList.add(rc_loc_to_base);
            } catch (IOException e) {
                e.printStackTrace();
            }

        });

        //TODO: run insert queries


    }

    static List<Client> loadClientIdToProcess(){
        List<Client> clientIdLst = new ArrayList<>();
        try {
            clientIdLst.add( (new ClientDao()).getList("0").get(0) ); // get the depot

            // get clients with orders from date A to date B
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            clientIdLst.addAll( (new ClientDao()).getListIDsBetweenDates(sdf.parse("2015-06-22"),sdf.parse("2015-06-27")));
        } catch (SQLException | ParseException e) {
            e.printStackTrace();
        }
        return clientIdLst;
    }

    static List<JsonToPost> generateJsonToPost(List<Client> clientIdList){
        List<JsonToPost> locJsonLst = new ArrayList<>();

        for(int i = 0; i < clientIdList.size(); i++){ // i==0 is depot, so it match depot to all locations
            Client from_client = clientIdList.get(i);
            Location from_location = new Location(from_client.getLocation_lat(), from_client.getLocation_lng());
            for(int j = i; j < clientIdList.size(); j++){
                Client to_client = clientIdList.get(j);
                Location to_location = new Location(to_client.getLocation_lat(), to_client.getLocation_lng());
                String strJsonToPost = JSONProcessing.toJSON(from_location, to_location);

                JsonToPost jsonToPost = new JsonToPost(from_client.getIdClient(), to_client.getIdClient(), strJsonToPost);
                locJsonLst.add(jsonToPost);

                //System.out.println("From client: " + from_client.getIdClient() + ", to client: " + to_client.getIdClient());
            }
        }

        return locJsonLst;
    }

    static void saveList(List jsonToPostList, String fileName) throws IOException {
        FileOutputStream fos = new FileOutputStream(fileName);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(jsonToPostList);
        oos.close();
    }

    static List readList(String fileName) throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(fileName);
        ObjectInputStream ois = new ObjectInputStream(fis);
        List list = (List) ois.readObject();
        ois.close();
        return list;
    }
}
