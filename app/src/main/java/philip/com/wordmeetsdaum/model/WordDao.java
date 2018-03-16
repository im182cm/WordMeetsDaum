package philip.com.wordmeetsdaum.model;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface WordDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertWords(List<Word> words);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertWord(Word word);

    @Update
    void updateWord(Word word);

    @Delete
    void deleteWord(Word word);

    @Query("SELECT * from words")
    Single<List<Word>> loadWords();

    @Query("SELECT * from words WHERE checked = 1")
    Single<List<Word>> loadMyWords();
}
