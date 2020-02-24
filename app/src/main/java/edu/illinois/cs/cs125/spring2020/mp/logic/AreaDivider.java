package edu.illinois.cs.cs125.spring2020.mp.logic;

import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.GoogleMap;

@SuppressWarnings("checkstyle:LeftCurly")
public class AreaDivider extends java.lang.Object {

    private final double north;
    private final double east;
    private final double south;
    private final double west;
    private final int cellSize;

    public AreaDivider(final double setNorth, final double setEast, final double setSouth, final double setWest, final int setCellSize) {
        north = setNorth;
        east = setEast;
        south = setSouth;
        west = setWest;
        cellSize = setCellSize;
    }


    public LatLngBounds getCellBounds(final int x, final int y) {
        double newSizex = (east - west) / getXCells();
        double newSizey = (north - south) / getYCells();
        LatLng northeast = new LatLng((newSizey * (y + 1) + south), (newSizex * (x + 1)) + west);
        LatLng southwest = new LatLng((newSizey * y) + south, (newSizex * x) + west);
        return new LatLngBounds(southwest, northeast);
    };

    public int getXCells() {
        return (int) (Math.ceil(LatLngUtils.distance(south, east, south, west) / cellSize));
    };

    public int getXIndex(final LatLng location) {
        double mapDistance = LatLngUtils.distance(north, east, north, west);
        double playerLocation = LatLngUtils.distance(north, west, north, location.longitude);
        double newSize = mapDistance / getXCells();
        return (int) (playerLocation / newSize);
    }

    public int getYCells() {
        return (int) (Math.ceil(LatLngUtils.distance(south, east, north, east) / cellSize));
    }

    public int getYIndex(final LatLng location) {
        double mapDistance = LatLngUtils.distance(north, east, south, east);
        double playerLocation = LatLngUtils.distance(south, east, location.latitude, east);
        double newSize = mapDistance / getYCells();
        return (int) (playerLocation / newSize);
    }

    public boolean isValid() {
        if (cellSize > 0 && east > west && north > south) {
            return true;
        }
        return false;
    }
    public void renderGrid(final GoogleMap map) {

    }
}
