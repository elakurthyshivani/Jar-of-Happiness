package com.jarofhappiness.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TagDao {
    @Insert
    void insert(TagEntity tag);

    @Query("SELECT tag FROM Tags WHERE memID=:memID")
    LiveData<List<String>> getTags(int memID);

    @Query("DELETE FROM Tags WHERE memID=:memID")
    void deleteAllTags(int memID);

    @Query("SELECT COUNT(DISTINCT Tag) FROM Tags WHERE memID NOT IN (SELECT memID FROM RecycleBin)"+
            " AND memID IN (SELECT memID FROM Memories WHERE userID=:userID)")
    LiveData<Integer> countOfTags(int userID);

    @Query("DELETE FROM Tags WHERE memID IN (:memIDs)")
    void deleteSelectedMemIDs(List<Integer> memIDs);

    @Query("SELECT tag AS text, count(*) AS count FROM Tags WHERE tag NOT LIKE '' AND memID NOT IN"
            +" (SELECT memID FROM RecycleBin) AND memID IN (SELECT memID FROM Memories WHERE "+
            "userID=:userID) GROUP BY tag")
    LiveData<List<OthersTuple>> getAllTags(int userID);

    @Query("DELETE FROM Tags WHERE memID IN (SELECT memID FROM Memories WHERE userID=:userID)")
    void emptyTags(int userID);
}
