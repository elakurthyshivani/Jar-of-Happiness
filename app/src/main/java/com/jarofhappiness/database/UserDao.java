package com.jarofhappiness.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface UserDao {
    @Insert
    void insert(UserEntity user);

    @Query("SELECT COUNT(*) FROM Users WHERE gmail LIKE :gmail")
    LiveData<Integer> isUserPresent(String gmail);

    @Query("SELECT * FROM Users WHERE gmail LIKE :gmail")
    UserEntity getUser(String gmail);

    @Query("SELECT gmail FROM Users WHERE userID=:userID")
    LiveData<String> getGmail(int userID);

    @Query("SELECT themeID FROM Users WHERE userID=:userID")
    LiveData<Integer> getThemeID(int userID);

    @Query("UPDATE Users SET themeID=:themeID WHERE userID=:userID")
    void updateTheme(int userID, int themeID);

    @Query("SELECT lockType FROM Users WHERE userID=:userID")
    LiveData<Integer> getLockType(int userID);

    @Query("DELETE FROM Users WHERE userID=:userID")
    void deleteAccount(int userID);
}
