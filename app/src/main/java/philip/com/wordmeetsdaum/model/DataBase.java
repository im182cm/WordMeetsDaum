package philip.com.wordmeetsdaum.model;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

/**
 * Created by 1000140 on 2018. 3. 13..
 */

@Database(entities = {Word.class}, version = 1, exportSchema = false)
public abstract class DataBase extends RoomDatabase{
    abstract public WordDao wordDao();
}
