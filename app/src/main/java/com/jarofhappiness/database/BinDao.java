package com.jarofhappiness.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface BinDao {
    @Insert
    void insert(BinEntity bin);

    @Insert
    void insertMany(BinEntity... bins);

    @Query("SELECT COUNT(*) FROM RecycleBin WHERE memID IN (SELECT memID FROM Memories WHERE userID"+
            "=:userID)")
    LiveData<Integer> countOfDeletedMemories(int userID);

    @Query("DELETE FROM RecycleBin WHERE memID IN (:memIDs)")
    void deleteSelectedMemIDs(List<Integer> memIDs);

    @Query("SELECT COUNT(*) FROM RecycleBin WHERE memID=:memID")
    LiveData<Integer> presentInBin(int memID);

    @Query("DELETE FROM RecycleBin WHERE memID IN (SELECT memID FROM Memories WHERE userID=:userID)")
    void emptyBin(int userID);
}
