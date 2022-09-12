package com.jarofhappiness.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName="Memories")
public class MemoryEntity {
    @ForeignKey(entity=UserEntity.class,
        parentColumns="userID",
        childColumns="userID")
    private int userID;
    @PrimaryKey(autoGenerate=true)
    private int memID;
    private String title;
    private String when;
    private String location;
    @NonNull
    private String memory;
    private String moods;
    private int isLocked;

    @Ignore
    public MemoryEntity(int userID, String title, String when, String location,@NonNull String memory,
                        String moods, int isLocked)  {
        this.userID=userID;
        this.title=title;
        this.when=when;
        this.location=location;
        this.memory=memory;
        this.moods=moods;
        this.isLocked=isLocked;
    }

    public MemoryEntity(int userID, int memID, String title, String when, String location,
                        @NonNull String memory, String moods, int isLocked)  {
        this.userID=userID;
        this.memID=memID;
        this.title=title;
        this.when=when;
        this.location=location;
        this.memory=memory;
        this.moods=moods;
        this.isLocked=isLocked;
    }

    public int getUserID() {
        return userID;
    }

    public int getMemID() {
        return memID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title=title;
    }

    public String getWhen() {
        return when;
    }

    public void setWhen(String when) {
        this.when=when;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location=location;
    }

    @NonNull
    public String getMemory() {
        return memory;
    }

    public void setMemory(@NonNull String memory) {
        this.memory=memory;
    }

    public String getMoods() {
        return moods;
    }

    public void setMoods(String moods) {
        this.moods=moods;
    }

    public int getIsLocked() {
        return isLocked;
    }

    public void setIsLocked(int isLocked) {
        this.isLocked=isLocked;
    }

    @Ignore
    public static final String LAUGH="1";
    @Ignore
    public static final String LOL="2";
    @Ignore
    public static final String LOVE="3";
    @Ignore
    public static final String TONGUE="4";
    @Ignore
    public static final String WINK="5";
    @Ignore
    public static final int YES=1;
    @Ignore
    public static final int NO=0;

    @Override
    public String toString() {
        return "MemoryEntity{" +
                "userID=" + userID +
                ", memID=" + memID +
                ", title='" + title + '\'' +
                ", when='" + when + '\'' +
                ", location='" + location + '\'' +
                ", memory='" + memory + '\'' +
                ", moods='" + moods + '\'' +
                ", isLocked=" + isLocked +
                '}';
    }
}
