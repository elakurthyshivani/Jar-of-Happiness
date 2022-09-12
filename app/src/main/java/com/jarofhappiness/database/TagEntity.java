package com.jarofhappiness.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(tableName="Tags", primaryKeys={"memID", "tag"})
public class TagEntity {
    @ForeignKey(entity=MemoryEntity.class,
    parentColumns="memID",
    childColumns="memID")

    private int memID;
    @NonNull
    private String tag;

    public TagEntity(int memID, @NonNull String tag) {
        this.memID=memID;
        this.tag=tag;
    }

    public int getMemID() {
        return memID;
    }

    public void setMemID(int memID) {
        this.memID = memID;
    }

    @NonNull
    public String getTag() {
        return tag;
    }

    public void setTag(@NonNull String tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "TagEntity{" +
                "memID=" + memID +
                ", tag='" + tag + '\'' +
                '}';
    }
}
