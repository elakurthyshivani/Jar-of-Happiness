package com.jarofhappiness.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName="Users")
public class UserEntity {
    @PrimaryKey(autoGenerate=true)
    private int userID;
    @NonNull
    private String gmail;
    private int themeID;
    private int backup=NO;
    private int twoFactorAuth=NO;
    private int lockType=PASSWORD;
    private String lockPassword;

    @Ignore
    public UserEntity(@NonNull String gmail, int themeID) {
        this.gmail=gmail;
        this.themeID=themeID;
    }

    public UserEntity(int userID, @NonNull String gmail, int themeID, int backup, int twoFactorAuth,
                      int lockType, String lockPassword)    {
        this.userID=userID;
        this.gmail=gmail;
        this.themeID=themeID;
        this.backup=backup;
        this.twoFactorAuth=twoFactorAuth;
        this.lockType=lockType;
        this.lockPassword=lockPassword;
    }

    public int getUserID() {
        return userID;
    }

    @NonNull
    public String getGmail()    {
        return gmail;
    }

    public int getThemeID() {
        return themeID;
    }

    public void setThemeID(int themeID) {
        this.themeID=themeID;
    }

    public int getBackup()  {
        return backup;
    }

    public void setBackup(int backup)   {
        this.backup=backup;
    }

    public int getTwoFactorAuth()   {
        return twoFactorAuth;
    }

    public void setTwoFactorAuth(int twoFactorAuth)  {
        this.twoFactorAuth=twoFactorAuth;
    }

    public int getLockType()    {
        return lockType;
    }

    public void setLockType(int lockType)   {
        this.lockType=lockType;
    }

    public String getLockPassword() {
        return lockPassword;
    }

    public void setLockPassword(String lockPassword)    {
        this.lockPassword=lockPassword;
    }

    @Ignore
    public static final int PIN=0;
    @Ignore
    public static final int PASSWORD=1;
    @Ignore
    public static final int YES=1;
    @Ignore
    public static final int NO=0;
}
