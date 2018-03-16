package philip.com.wordmeetsdaum;

import android.app.Application;
import android.arch.persistence.room.Room;

import philip.com.wordmeetsdaum.model.DataBase;

/**
 * Created by 1000140 on 2018. 3. 13..
 */

public class MyApplication extends Application {
    public static DataBase MyDb;

    @Override
    public void onCreate() {
        super.onCreate();

        MyDb = Room.databaseBuilder(this, DataBase.class, "WordMeetsDaum.db").build();
    }
}
