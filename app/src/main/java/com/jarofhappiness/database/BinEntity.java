package com.jarofhappiness.database;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName="RecycleBin")
public class BinEntity {
    @ForeignKey(entity=MemoryEntity.class,
    parentColumns="memID",
    childColumns="memID")
    @PrimaryKey
    private int memID;

    public BinEntity(int memID) {
        this.memID=memID;
    }

    public int getMemID() {
        return memID;
    }
}
