package com.shevaalex.android.rickmortydatabase.source.database;

import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface LocationDao {
    //shows list of all locations
    @Query("SELECT * FROM Location ORDER BY name")
    DataSource.Factory<Integer, Location> showAllLocations();

    // gets the last location to compare databases
    @Query("SELECT * FROM Location ORDER BY id DESC LIMIT 1")
    Location showLastInLocationList();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertLocation(Location location);

    // gets the entry count to compare databases
    @Query("SELECT COUNT(id) FROM Location")
    int getLocationCount();
}
