package edu.illinois.cs.cs125.spring2020.mp.logic;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import edu.illinois.cs.cs125.spring2020.mp.R;

public class GameSummary extends java.lang.Object {

    /** JsonObject that contains relevant game information. */
    private com.google.gson.JsonObject infoFromServer;

    public GameSummary(final com.google.gson.JsonObject setInfoFromServer) {
        infoFromServer = setInfoFromServer;
    }

    /**
     * Gets the unique, server-assigned ID of this game.
     * @return the game ID
     */
    public java.lang.String getId() {
        String id = infoFromServer.get("id").getAsString();
        return id;
    }

    /**
     * Gets the mode of this game, either area or target.
     * @return the game mode
     */
    public java.lang.String getMode() {
        String mode = infoFromServer.get("mode").getAsString();
        return mode;
    }

    /**
     * Gets the owner/creator of this game.
     * @return the email of the game's owner
     */
    public java.lang.String getOwner() {
        String owner = infoFromServer.get("owner").getAsString();
        return owner;
    }

    /**
     * Gets the name of the user's team/role.
     * @param userEmail the logged-in user's email
     * @param context android data that stores game information
     * @return the human-readable team/role name of the user in this game
     */
    public java.lang.String getPlayerRole(final java.lang.String userEmail,
                                          final android.content.Context context) {
        String[] teamNames = context.getResources().getStringArray(R.array.team_choices);
        JsonArray players = infoFromServer.get("players").getAsJsonArray();
        for (JsonElement i : players) {
            JsonObject j = (JsonObject) i;
            if (userEmail.equals(j.get("email").getAsString())) {
                return teamNames[j.get("team").getAsInt()];
            }
        }
        return null;
    }

    /**
     * Determines whether this game is an invitation to the user.
     * @param userEmail the logged-in user's email
     * @return whether the user is invited to this game
     */
    public boolean isInvitation(final java.lang.String userEmail) {
        JsonArray players = infoFromServer.get("players").getAsJsonArray();
        for (JsonElement i : players) {
            JsonObject j = (JsonObject) i;
            int state = infoFromServer.get("state").getAsInt();
            if (userEmail.equals(j.get("email").getAsString())) {
                if (state == 0 || state == 1) {
                    if (j.get("state").getAsInt() == 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines whether the user is currently involved in this game.
     * @param userEmail the logged-in user's email
     * @return whether this game is ongoing for the user
     */
    public boolean isOngoing(final java.lang.String userEmail) {
        JsonArray players = infoFromServer.get("players").getAsJsonArray();
        for (JsonElement i : players) {
            JsonObject j = (JsonObject) i;
            if (userEmail.equals(j.get("email").getAsString())) {
                if (j.get("state").getAsInt() == 2 || j.get("state").getAsInt() == PlayerStateID.PLAYING) {
                    int state = infoFromServer.get("state").getAsInt();
                    if (state == 0 || state == 1) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
