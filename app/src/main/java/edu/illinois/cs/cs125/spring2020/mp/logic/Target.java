package edu.illinois.cs.cs125.spring2020.mp.logic;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/** Represents a target in an ongoing target-mode game and manages the marker displaying it. */
public class Target extends java.lang.Object {

    /** The map to render to. */
    private com.google.android.gms.maps.GoogleMap map;

    /** Marker object to designate target to be captured. */
    private Marker marker;

    /** The position of the target. */
    private com.google.android.gms.maps.model.LatLng position;

    /** The TeamID code of the team currently owning the target. */
    private int team;

    /** Creates a target in a target-mode game by placing an appropriately colored marker on the map.
     * @param setMap The map to render to.
     * @param setPosition The position of the target.
     * @param setTeam The TeamID code of the team currently owning the target.
     */
    public Target(final com.google.android.gms.maps.GoogleMap setMap,
                  final com.google.android.gms.maps.model.LatLng setPosition, final int setTeam) {
        map = setMap;
        position = setPosition;
        team = setTeam;

        MarkerOptions options = new MarkerOptions().position(position);
        marker = map.addMarker(options);

        if (TeamID.TEAM_YELLOW == team) {
            BitmapDescriptor icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW);
            marker.setIcon(icon);
        } else if (TeamID.TEAM_RED == team) {
            BitmapDescriptor icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
            marker.setIcon(icon);
        } else if (TeamID.TEAM_BLUE == team) {
            BitmapDescriptor icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE);
            marker.setIcon(icon);
        } else if (TeamID.TEAM_GREEN == team) {
            BitmapDescriptor icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
            marker.setIcon(icon);
        } else {
            BitmapDescriptor icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET);
            marker.setIcon(icon);
        }
    }

    /**
     * Gets the position of the target.
     * @return the coordinates of the target
     */
    public com.google.android.gms.maps.model.LatLng getPosition() {
        return position;
    }

    /**
     * Gets the ID of the team currently owning this target.
     * @return the owning team ID or OBSERVER if unclaimed
     */
    public int getTeam() {
        return team;
    }

    /**
     * Updates the owning team of this target and updates the hue of the marker to match.
     * @param newTeam the ID of the team that captured the target
     */
    public void setTeam(final int newTeam) {
        team = newTeam;
        if (TeamID.TEAM_YELLOW == newTeam) {
            BitmapDescriptor icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW);
            marker.setIcon(icon);
        } else if (TeamID.TEAM_RED == newTeam) {
            BitmapDescriptor icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
            marker.setIcon(icon);
        } else if (TeamID.TEAM_BLUE == newTeam) {
            BitmapDescriptor icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE);
            marker.setIcon(icon);
        } else if (TeamID.TEAM_GREEN == newTeam) {
            BitmapDescriptor icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
            marker.setIcon(icon);
        }
    }

}
