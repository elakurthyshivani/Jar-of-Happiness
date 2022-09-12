package com.jarofhappiness.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(tableName="ExternalLinks", primaryKeys={"memID", "link"})
public class LinkEntity {
    @ForeignKey(entity=MemoryEntity.class,
            parentColumns="memID",
            childColumns="memID")
    private int memID;
    @NonNull
    private String link;

    public LinkEntity(int memID, @NonNull String link)  {
        this.memID=memID;
        this.link=link;
    }

    public int getMemID() {
        return memID;
    }

    public void setMemID(int memID) {
        this.memID = memID;
    }

    @NonNull
    public String getLink() {
        return link;
    }

    public void setLink(@NonNull String link) {
        this.link = link;
    }

    @Override
    public String toString() {
        return "LinkEntity{" +
                "memID=" + memID +
                ", link='" + link + '\'' +
                '}';
    }
}
