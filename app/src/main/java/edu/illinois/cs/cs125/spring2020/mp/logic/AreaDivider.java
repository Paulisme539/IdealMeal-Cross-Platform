package edu.illinois.cs.cs125.spring2020.mp.logic;

import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.GoogleMap;
import android.graphics.Color;
import com.google.android.gms.maps.model.PolylineOptions;

/**
 * Holds method for area mode gameplay.
 */
public class AreaDivider extends java.lang.Object {

    /** The latitude of the north border. */
    private double north;
    /** The longitude of the east border. */
    private double east;
    /** The latitude of the south border. */
    private double south;
    /** The longitude of the west border. */
    private double west;
    /** The expected size of each cell. */
    private int cellSize;

    /**
     * Creates an AreaDivider for an area.
     * @param setNorth Sets north variable.
     * @param setEast Sets east variable.
     * @param setSouth Sets south variable.
     * @param setWest Sets west variable.
     * @param setCellSize Sets cell size variable.
     */
    public AreaDivider(final double setNorth, final double setEast, final double setSouth,
                       final double setWest, final int setCellSize) {
        north = setNorth;
        east = setEast;
        south = setSouth;
        west = setWest;
        cellSize = setCellSize;
    }

    /**
     * Returns the index of a cell as a LatLngBounds object.
     * @param x the cell's x coordinate
     * @param y the cell's y coordinate
     * @return the LatLngBounds boundaries of the cell
     */
    public LatLngBounds getCellBounds(final int x, final int y) {
        double newSizex = (east - west) / getXCells();
        double newSizey = (north - south) / getYCells();
        LatLng northeast = new LatLng((newSizey * (y + 1) + south), (newSizex * (x + 1)) + west);
        LatLng southwest = new LatLng((newSizey * y) + south, (newSizex * x) + west);
        return new LatLngBounds(southwest, northeast);
    }

    /**
     * Returns the number of cells in the x direction based on cell size.
     * @return the number of cells in the x direction
     */
    public int getXCells() {
        return (int) (Math.ceil(LatLngUtils.distance(south, east, south, west) / cellSize));
    };

    /**
     * Returns the x index of a location from a LatLng object.
     * @param location the LatLng object with the cell location
     * @return the x coordinate of the cell containing the LatLng point
     */
    public int getXIndex(final LatLng location) {
        double mapDistance = LatLngUtils.distance(north, east, north, west);
        double playerLocation = LatLngUtils.distance(north, west, north, location.longitude);
        double newSize = mapDistance / getXCells();
        return (int) (playerLocation / newSize);
    }

    /**
     * Returns the number of cells in the y direction based on cell size.
     * @return the number of cells in the y direction
     */
    public int getYCells() {
        return (int) (Math.ceil(LatLngUtils.distance(south, east, north, east) / cellSize));
    }

    /**
     * Returns the y index of a location from a LatLng object.
     * @param location the LatLng object with the cell location
     * @return the y coordinate of the cell containing the LatLng point
     */
    public int getYIndex(final LatLng location) {
        double mapDistance = LatLngUtils.distance(north, east, south, east);
        double playerLocation = LatLngUtils.distance(south, east, location.latitude, east);
        double newSize = mapDistance / getYCells();
        return (int) (playerLocation / newSize);
    }

    /**
     * Returns whether the configuration provided to the constructor is valid.
     * @return whether the Area Divider can divide a valid area
     */
    public boolean isValid() {
        if (cellSize > 0 && east > west && north > south) {
            return true;
        }
        return false;
    }

    /**
     * Draws the grid to a map using solid black polylines.
     * @param map the Google map to draw on
     */
    public void renderGrid(final GoogleMap map) {
        double[] xList = new double[getXCells() + 1];
        double[] yList = new double[getYCells() + 1];
        final int lineThickness = 12;
        for (int i = 0; i < xList.length; i++) {
            double newSizeX = ((east - west) / getXCells());
            xList[i] = ((newSizeX * i) + west);
        }
        for (int i = 0; i < yList.length; i++) {
            double newSizeY = ((north - south) / getYCells());
            yList[i] = ((newSizeY * i) + south);
        }
        for (int i = 0; i < yList.length; i++) {
            PolylineOptions fill = new PolylineOptions().add(new LatLng(yList[i], xList[0]),
                    new LatLng(yList[i], xList[getXCells()])).color(Color.BLACK).width(lineThickness).zIndex(1);
            map.addPolyline(fill);
        }
        for (int i = 0; i < xList.length; i++) {
            PolylineOptions fill = new PolylineOptions().add(new LatLng(yList[0], xList[i]),
                    new LatLng(yList[getYCells()], xList[i])).color(Color.BLACK).width(lineThickness).zIndex(1);
            map.addPolyline(fill);
        }
    }
}
