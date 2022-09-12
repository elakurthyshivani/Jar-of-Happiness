package com.jarofhappiness.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MemoryDao {
    @Insert
    long insert(MemoryEntity memory);

    @Update(onConflict= OnConflictStrategy.REPLACE)
    void update(MemoryEntity memory);

    @Query("SELECT memID FROM Memories WHERE rowid=:rowID")
    int getMemIDAtRowID(long rowID);

    @Query("SELECT * FROM Memories WHERE memID=:memID")
    LiveData<MemoryEntity> getMemoryDetails(int memID);

    @Query("SELECT memID FROM Memories WHERE userID=:userID AND memID NOT IN (SELECT memID FROM "+
            "RecycleBin) ORDER BY RANDOM() LIMIT 1")
    LiveData<Integer> getRandomMemID(int userID);

    @Query("SELECT memID, title, memory, isLocked FROM Memories WHERE userID=:userID AND " +
            "memID NOT IN (SELECT memID FROM RecycleBin)")
    LiveData<List<MemoryTuple>> getAllMemories(int userID);

    @Query("SELECT memID, title, memory, isLocked FROM Memories WHERE userID=:userID AND "+
            "memID NOT IN (SELECT memID FROM RecycleBin) AND isLocked=:isLocked")
    LiveData<List<MemoryTuple>> getAllMemories(int userID, int isLocked);

    @Query("SELECT memID, title, memory, isLocked FROM Memories WHERE userID=:userID AND "+
            "memID IN (SELECT memID FROM RecycleBin)")
    LiveData<List<MemoryTuple>> getAllMemoriesInBin(int userID);

    @Query("SELECT `when` AS text, count(*) AS count FROM Memories WHERE userID=:userID AND `when` "
            +"NOT LIKE '' AND memID NOT IN (SELECT memID FROM RecycleBin) GROUP BY `when`")
    LiveData<List<OthersTuple>> getAllWhens(int userID);

    @Query("SELECT location AS text, count(*) AS count FROM Memories WHERE userID=:userID AND location "
            +"NOT LIKE '' AND memID NOT IN (SELECT memID FROM RecycleBin) GROUP BY location")
    LiveData<List<OthersTuple>> getAllLocations(int userID);

    @Query("SELECT memID, title, memory, isLocked FROM Memories WHERE userID=:userID AND "+
            "memID NOT IN (SELECT memID FROM RecycleBin) AND `when` LIKE :text")
    LiveData<List<MemoryTuple>> getAllMemoriesWhen(int userID, String text);

    @Query("SELECT memID, title, memory, isLocked FROM Memories WHERE userID=:userID AND "+
            "memID NOT IN (SELECT memID FROM RecycleBin) AND location LIKE :text")
    LiveData<List<MemoryTuple>> getAllMemoriesLocation(int userID, String text);

    @Query("SELECT memID, title, memory, isLocked FROM Memories WHERE userID=:userID AND "+
            "memID NOT IN (SELECT memID FROM RecycleBin) AND memID IN (SELECT memID"+
            " FROM Tags WHERE tag LIKE :text)")
    LiveData<List<MemoryTuple>> getAllMemoriesTag(int userID, String text);

    @Query("SELECT memID, title, memory, isLocked FROM Memories WHERE userID=:userID AND "+
            "memID NOT IN (SELECT memID FROM RecycleBin) AND memID IN (SELECT memID"+
            " FROM ExternalLinks WHERE link LIKE :text)")
    LiveData<List<MemoryTuple>> getAllMemoriesLink(int userID, String text);

    @Query("SELECT COUNT(*) FROM Memories WHERE userID=:userID AND memID NOT IN (SELECT memID FROM "+
            "RecycleBin)")
    LiveData<Integer> countOfMemories(int userID);

    @Query("SELECT COUNT(DISTINCT title) FROM Memories WHERE userID=:userID AND title NOT LIKE ''"+
            " AND memID NOT IN (SELECT memID FROM RecycleBin)")
    LiveData<Integer> countOfTitles(int userID);

    @Query("SELECT COUNT(DISTINCT `when`) FROM Memories WHERE userID=:userID AND `when` NOT LIKE ''"+
            " AND memID NOT IN (SELECT memID FROM RecycleBin)")
    LiveData<Integer> countOfWhens(int userID);

    @Query("SELECT COUNT(DISTINCT location) FROM Memories WHERE userID=:userID AND location NOT LIKE"+
            " '' AND memID NOT IN (SELECT memID FROM RecycleBin)")
    LiveData<Integer> countOfLocations(int userID);

    @Query("SELECT COUNT(isLocked) FROM Memories WHERE userID=:userID AND isLocked=="+
        MemoryEntity.YES+" AND memID NOT IN (SELECT memID FROM RecycleBin)")
    LiveData<Integer> countOfLockedMemories(int userID);

    @Query("SELECT COUNT(*) FROM Memories WHERE userID=:userID AND moods LIKE :mood AND "+
            " memID NOT IN (SELECT memID FROM RecycleBin)")
    LiveData<Integer> countOfMemoriesWithMood(int userID, String mood);

    @Query("DELETE FROM Memories WHERE memID IN (:memIDs)")
    void deleteSelectedMemories(List<Integer> memIDs);

    @Query("DELETE FROM Memories WHERE userID=:userID")
    void emptyMemories(int userID);
}
