package com.jarofhappiness.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface LinkDao {
    @Insert
    void insert(LinkEntity link);

    @Query("SELECT link FROM ExternalLinks WHERE memID=:memID")
    LiveData<List<String>> getLinks(int memID);

    @Query("DELETE FROM ExternalLinks WHERE memID=:memID")
    void deleteAllLinks(int memID);

    @Query("SELECT COUNT(DISTINCT Link) FROM ExternalLinks WHERE memID NOT IN (SELECT memID FROM "+
            "RecycleBin) AND memID IN (SELECT memID FROM Memories WHERE userID=:userID)")
    LiveData<Integer> countOfLinks(int userID);

    @Query("DELETE FROM ExternalLinks WHERE memID IN (:memIDs)")
    void deleteSelectedMemIDs(List<Integer> memIDs);

    @Query("SELECT link AS text, count(*) AS count FROM ExternalLinks WHERE link NOT LIKE '' AND "
            +"memID NOT IN (SELECT memID FROM RecycleBin) AND memID IN (SELECT memID FROM Memories"
            +" WHERE userID=:userID) GROUP BY link")
    LiveData<List<OthersTuple>> getAllLinks(int userID);

    @Query("DELETE FROM ExternalLinks WHERE memID IN (SELECT memID FROM Memories WHERE "+
            "userID=:userID)")
    void emptyLinks(int userID);
}
