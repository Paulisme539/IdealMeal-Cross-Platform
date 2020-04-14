package edu.illinois.cs.cs125.spring2020.mp.logic;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.neovisionaries.ws.client.WebSocket;

import edu.illinois.cs.cs125.spring2020.mp.R;

/**
 * Represents an area mode game. Keeps track of cells and the player's most recent capture.
 * <p>
 * All these functions are stubs that you need to implement.
 * Feel free to add any private helper functions that would be useful.
 * See {@link TargetGame} for an example of how multiplayer games are handled.
 */
public final class AreaGame extends Game {

    // You will probably want some instance variables to keep track of the game state
    // (similar to the area mode gameplay logic you previously wrote in GameActivity)
    /** Double to keep track of West Boundary. */
    private double areaWest;

    /** Double to keep track of North Boundary. */
    private double areaNorth;

    /** Double to keep track of East Boundary. */
    private double areaEast;

    /** Double to keep track of South Boundary. */
    private double areaSouth;

    /** Integer to keep track of Area Game's cell size. */
    private int cellSize;

    /** Instance variable to access AreaDivider functions. */
    private AreaDivider instance;

    /** JsonArray used to store list of cells. */
    private JsonArray list;

    /** JsonArray used to store list of players in game. */
    private JsonArray playersList;

    /** JsonObject used to keep track of a player object in playersList. */
    private JsonObject player;

    /**
     * Creates a game in area mode.
     * <p>
     * Loads the current game state from JSON into instance variables and populates the map
     * to show existing cell captures.
     * @param email the user's email
     * @param map the Google Maps control to render to
     * @param webSocket the websocket to send updates to
     * @param fullState the "full" update from the server
     * @param context the Android UI context
     */
    public AreaGame(final String email, final GoogleMap map, final WebSocket webSocket,
                    final JsonObject fullState, final Context context) {
        super(email, map, webSocket, fullState, context);
        areaWest = fullState.get("areaWest").getAsDouble();
        areaNorth = fullState.get("areaNorth").getAsDouble();
        areaEast = fullState.get("areaEast").getAsDouble();
        areaSouth = fullState.get("areaSouth").getAsDouble();
        cellSize = fullState.get("cellSize").getAsInt();
        instance = new AreaDivider(areaNorth, areaEast, areaSouth, areaWest, cellSize);
        instance.renderGrid(map);

        playersList = fullState.get("players").getAsJsonArray();


        list = fullState.get("cells").getAsJsonArray();
        for (int i = 0; i < list.size(); i++) {
            JsonObject cell = list.get(i).getAsJsonObject();
            int x = cell.get("x").getAsInt();
            int y = cell.get("y").getAsInt();
            LatLngBounds inputPolygon = instance.getCellBounds(x, y);
            int[] array = getContext().getResources().getIntArray(R.array.team_colors);
            int teamID = cell.get("team").getAsInt();
            LatLng northwest = new LatLng(inputPolygon.northeast.latitude, inputPolygon.southwest.longitude);
            LatLng southeast = new LatLng(inputPolygon.southwest.latitude, inputPolygon.northeast.longitude);
            PolygonOptions object = new PolygonOptions().add(inputPolygon.northeast, southeast,
                    inputPolygon.southwest, northwest).fillColor(array[teamID]);
            map.addPolygon(object);
        }
    }

    /**
     * Called when the user's location changes.
     * <p>
     * Area mode games detect whether the player is in an uncaptured cell. Capture is possible if
     * the player has no captures yet or if the cell shares a side with the previous cell captured by
     * the player. If capture occurs, a polygon with the team color is added to the cell on the map
     * and a cellCapture update is sent to the server.
     * @param location the player's most recently known location
     */
    @Override
    public void locationUpdated(final LatLng location) {
        super.locationUpdated(location);
        if (location.longitude > areaEast || location.longitude < areaWest && location.latitude > areaNorth
                || location.latitude < areaSouth) {
            return;
        }
        int xLocation = instance.getXIndex(location);
        int yLocation = instance.getYIndex(location);
        for (int i = 0; i < list.size(); i++) {
            JsonObject cell = list.get(i).getAsJsonObject();
            int x = cell.get("x").getAsInt();
            int y = cell.get("y").getAsInt();
            if (x == xLocation && y == yLocation) {
                return;
            }
        }


        for (int i = 0; i < playersList.size(); i++) {
            player = playersList.get(i).getAsJsonObject();
            if (getEmail().equals(player.get("email").getAsString())) {
                JsonArray pathList = player.get("path").getAsJsonArray();
                if (pathList != null) {
                    if (pathList.size() == 0) {
                        //capture target
                        LatLngBounds inputPolygon = instance.getCellBounds(xLocation, yLocation);
                        int[] array = getContext().getResources().getIntArray(R.array.team_colors);
                        int teamID = player.get("team").getAsInt();
                        LatLng northwest = new LatLng(inputPolygon.northeast.latitude,
                                inputPolygon.southwest.longitude);
                        LatLng southeast = new LatLng(inputPolygon.southwest.latitude,
                                inputPolygon.northeast.longitude);
                        PolygonOptions object = new PolygonOptions().add(inputPolygon.northeast,
                                southeast, inputPolygon.southwest, northwest).fillColor(array[teamID]);
                        getMap().addPolygon(object);

                        JsonObject cellCaptureUpdate = new JsonObject();
                        cellCaptureUpdate.addProperty("x", xLocation);
                        cellCaptureUpdate.addProperty("y", yLocation);
                        pathList.add(cellCaptureUpdate);

                        JsonObject cellUpdate = new JsonObject();
                        cellUpdate.addProperty("type", "cellCapture");
                        cellUpdate.addProperty("x", xLocation);
                        cellUpdate.addProperty("y", yLocation);
                        sendMessage(cellUpdate);
                    } else {
                        JsonObject lastVisit = pathList.get(pathList.size() - 1).getAsJsonObject();
                        if (lastVisit != null) {
                            if (((xLocation == lastVisit.get("x").getAsInt() + 1) && (yLocation
                                    == lastVisit.get("y").getAsInt()))
                                    || ((yLocation == lastVisit.get("y").getAsInt() + 1) && (xLocation
                                    == lastVisit.get("x").getAsInt()))) {
                                //capture target
                                LatLngBounds inputPolygon = instance.getCellBounds(xLocation, yLocation);
                                int[] array = getContext().getResources().getIntArray(R.array.team_colors);
                                int teamID = player.get("team").getAsInt();
                                LatLng northwest = new LatLng(inputPolygon.northeast.latitude,
                                        inputPolygon.southwest.longitude);
                                LatLng southeast = new LatLng(inputPolygon.southwest.latitude,
                                        inputPolygon.northeast.longitude);
                                PolygonOptions object = new PolygonOptions().add(inputPolygon.northeast,
                                        southeast, inputPolygon.southwest, northwest).fillColor(array[teamID]);
                                getMap().addPolygon(object);

                                JsonObject cellCaptureUpdate = new JsonObject();
                                cellCaptureUpdate.addProperty("x", xLocation);
                                cellCaptureUpdate.addProperty("y", yLocation);
                                pathList.add(cellCaptureUpdate);

                                JsonObject cellUpdate = new JsonObject();
                                cellUpdate.addProperty("type", "cellCapture");
                                cellUpdate.addProperty("x", xLocation);
                                cellUpdate.addProperty("y", yLocation);
                                sendMessage(cellUpdate);

                            } else if (((xLocation == lastVisit.get("x").getAsInt() - 1) && (yLocation
                                    == lastVisit.get("y").getAsInt()))
                                    || ((yLocation == lastVisit.get("y").getAsInt() - 1) && (xLocation
                                    == lastVisit.get("x").getAsInt()))) {
                                LatLngBounds inputPolygon = instance.getCellBounds(xLocation, yLocation);
                                int[] array = getContext().getResources().getIntArray(R.array.team_colors);
                                int teamID = player.get("team").getAsInt();
                                LatLng northwest = new LatLng(inputPolygon.northeast.latitude,
                                        inputPolygon.southwest.longitude);
                                LatLng southeast = new LatLng(inputPolygon.southwest.latitude,
                                        inputPolygon.northeast.longitude);
                                PolygonOptions object = new PolygonOptions().add(inputPolygon.northeast,
                                        southeast, inputPolygon.southwest, northwest).fillColor(array[teamID]);
                                getMap().addPolygon(object);

                                JsonObject cellCaptureUpdate = new JsonObject();
                                cellCaptureUpdate.addProperty("x", xLocation);
                                cellCaptureUpdate.addProperty("y", yLocation);
                                pathList.add(cellCaptureUpdate);

                                JsonObject cellUpdate = new JsonObject();
                                cellUpdate.addProperty("type", "cellCapture");
                                cellUpdate.addProperty("x", xLocation);
                                cellUpdate.addProperty("y", yLocation);
                                sendMessage(cellUpdate);
                            }
                        }
                    }
                }
            }
        }
    }


    /**
     * Processes an update from the server.
     * <p>
     * Since playerCellCapture events are specific to area mode games, this function handles those
     * by placing a polygon of the capturing player's team color on the newly captured cell and
     * recording the cell's new owning team.
     * All other message types are delegated to the superclass.
     * @param message JSON from the server (the "type" property indicates the update type)
     * @return whether the message type was recognized
     */
    @Override
    public boolean handleMessage(final JsonObject message) {
        if (super.handleMessage(message)) {
            return true;
        }
        if (message.get("type").getAsString().equals("playerCellCapture")) {
            int x = message.get("x").getAsInt();
            int y = message.get("y").getAsInt();
            int teamID = message.get("team").getAsInt();
            String email = message.get("email").getAsString();


            int[] array = getContext().getResources().getIntArray(R.array.team_colors);
            LatLngBounds inputPolygon = instance.getCellBounds(x, y);
            LatLng northwest = new LatLng(inputPolygon.northeast.latitude, inputPolygon.southwest.longitude);
            LatLng southeast = new LatLng(inputPolygon.southwest.latitude, inputPolygon.northeast.longitude);
            PolygonOptions object = new PolygonOptions().add(inputPolygon.northeast,
                    southeast, inputPolygon.southwest, northwest).fillColor(array[teamID]);
            getMap().addPolygon(object);

            JsonObject cellCaptureUpdate = new JsonObject();
            cellCaptureUpdate.addProperty("x", x);
            cellCaptureUpdate.addProperty("y", y);
            cellCaptureUpdate.addProperty("team", teamID);
            cellCaptureUpdate.addProperty("email", email);
            list.add(cellCaptureUpdate);

            for (int i = 0; i < playersList.size(); i++) {
                player = playersList.get(i).getAsJsonObject();
                if (player.get("email").getAsString().equals(email)) {
                    JsonArray pathList = player.get("path").getAsJsonArray();
                    JsonObject cellUpdate = new JsonObject();
                    cellUpdate.addProperty("x", x);
                    cellUpdate.addProperty("y", y);
                    pathList.add(cellUpdate);
                }
            }

            return true;
        } else {
            return false;
        }
    }

    /**
     * Gets a team's score in this area mode game.
     * @param teamId the team ID
     * @return the number of cells owned by the team
     */
    @Override
    public int getTeamScore(final int teamId) {
        int count = 0;
        for (int i = 0; i < playersList.size(); i++) {
            player = playersList.get(i).getAsJsonObject();
            if (teamId == player.get("team").getAsInt()) {
                JsonArray path = player.get("path").getAsJsonArray();
                for (int j = 0; j < path.size(); j++) {
                    count++;
                }
            }
        }
        return count;
    }

}
