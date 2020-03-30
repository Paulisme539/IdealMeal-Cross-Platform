package edu.illinois.cs.cs125.spring2020.mp.logic;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/** Provides static methods to convert game information to JSON payloads that can be POSTed to the
 * server's /games/create endpoint to create a multiplayer game. */
public class GameSetup extends java.lang.Object {

    /** Creates a JSON object representing the configuration of a multiplayer area mode game.
     * @param invitees all players involved in the game (never null)
     * @param area the area boundaries
     * @param cellSize the desired cell size in meters
     * @return a JSON object usable by the /games/create endpoint or null if the configuration is invalid.
     */
    public static com.google.gson.JsonObject areaMode(final java.util.List<Invitee> invitees, final
        com.google.android.gms.maps.model.LatLngBounds area, final int cellSize) {
        if (invitees == null || invitees.size() <= 0 || cellSize <= 0) {
            return null;
        }

        JsonObject areaMode = new JsonObject();
        areaMode.addProperty("mode", "area");
        areaMode.addProperty("areaNorth", area.northeast.latitude);
        areaMode.addProperty("areaSouth", area.southwest.latitude);
        areaMode.addProperty("areaEast", area.northeast.longitude);
        areaMode.addProperty("areaWest", area.southwest.longitude);
        areaMode.addProperty("cellSize", cellSize);

        JsonArray inviteesList = new JsonArray();
        for (int i = 0; i < invitees.size(); i++) {
            JsonObject emailTeam = new JsonObject();
            emailTeam.addProperty("email", invitees.get(i).getEmail());
            emailTeam.addProperty("team", invitees.get(i).getTeamId());
            inviteesList.add(emailTeam);
        }
        areaMode.add("invitees", inviteesList);
        return areaMode;
    }

    /** Creates a JSON object representing the configuration of a multiplayer target mode game.
     * @param invitees all players involved in the game (never null)
     * @param targets the positions of all targets (never null)
     * @param proximityThreshold the proximity threshold in meters
     * @return a JSON object usable by the /games/create endpoint or null if the configuration is invalid
     */
    public static com.google.gson.JsonObject targetMode(final java.util.List<Invitee> invitees,
                                                        final java.util.List<com.google.android.gms
                                                                .maps.model.LatLng> targets,
                                                        final int proximityThreshold) {
        if (invitees == null || invitees.size() <= 0 || proximityThreshold <= 0 || targets.size() <= 0) {
            return null;
        }
        JsonObject targetMode = new JsonObject();
        targetMode.addProperty("mode", "target");
        targetMode.addProperty("proximityThreshold", proximityThreshold);
        JsonArray targetList = new JsonArray();
        for (int i = 0; i < targets.size(); i++) {
            JsonObject latlng = new JsonObject();
            latlng.addProperty("latitude", targets.get(i).latitude);
            latlng.addProperty("longitude", targets.get(i).longitude);
            targetList.add(latlng);
        }
        targetMode.add("targets", targetList);
        JsonArray inviteesList = new JsonArray();
        for (int i = 0; i < invitees.size(); i++) {
            JsonObject emailTeam = new JsonObject();
            emailTeam.addProperty("email", invitees.get(i).getEmail());
            emailTeam.addProperty("team", invitees.get(i).getTeamId());
            inviteesList.add(emailTeam);
        }
        targetMode.add("invitees", inviteesList);
        return targetMode;
    }
}
