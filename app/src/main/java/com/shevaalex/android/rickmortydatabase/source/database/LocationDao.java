package com.shevaalex.android.rickmortydatabase.source.database;

import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface LocationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertLocation(Location location);

    //shows list of all locations
    @Query("SELECT * FROM Location ORDER BY name")
    DataSource.Factory<Integer, Location> showAllLocations();

    //gets list of all locations
    @Query("SELECT * FROM Location ORDER BY name")
    List<Location> getAllLocations();

    // gets the last location to compare databases
    @Query("SELECT * FROM Location ORDER BY id DESC LIMIT 1")
    Location showLastInLocationList();

    // gets the entry count to compare databases
    @Query("SELECT COUNT(id) FROM Location")
    int getLocationCount();

    // gets the location by id
    @Query("SELECT * FROM Location WHERE id=:id")
    Location getLocationById(int id);
}
