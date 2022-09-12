package com.jarofhappiness.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities={UserEntity.class, MemoryEntity.class, TagEntity.class, LinkEntity.class,
        BinEntity.class}, version=1, exportSchema=false)
public abstract class JOHDatabase extends RoomDatabase {
    private static final Object LOCK=new Object();
    private static final String DATABASE_NAME="JOHDatabase";
    private static JOHDatabase instance;

    public static JOHDatabase getInstance(Context context)  {
        if(instance==null)  {
            synchronized(LOCK)  {
                instance=Room.databaseBuilder(context.getApplicationContext(), JOHDatabase.class,
                        JOHDatabase.DATABASE_NAME).build();
            }
        }
        return instance;
    }

    public abstract UserDao userDao();

    public abstract MemoryDao memoryDao();

    public abstract TagDao tagDao();

    public abstract LinkDao linkDao();

    public abstract BinDao binDao();
}
