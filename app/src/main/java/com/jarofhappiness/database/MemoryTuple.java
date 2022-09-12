package com.jarofhappiness.database;

import androidx.room.Ignore;

public class MemoryTuple {
    public int memID;
    public int isLocked;
    public String title;
    public String memory;

    @Ignore
    public boolean isChecked;
}
