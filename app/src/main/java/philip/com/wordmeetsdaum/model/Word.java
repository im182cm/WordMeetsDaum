package philip.com.wordmeetsdaum.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "words", indices = {@Index(value = {"word"}, unique = true)})
public class Word {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int mId;

    @NonNull
    @ColumnInfo(name = "word")
    public final String mContent;

    @NonNull
    @ColumnInfo(name = "checked")
    public boolean mIsChecked;

    public Word(@NonNull String mContent, @NonNull boolean mIsChecked) {
        this.mContent = mContent;
        this.mIsChecked = mIsChecked;
    }

    public boolean isChecked() {
        return mIsChecked;
    }

    public String getContent() {
        return mContent;
    }

    @Override
    public String toString() {
        return mId + ". " + mContent;
    }

    @Override
    public boolean equals(Object obj) {
        if (this.mContent.equals(((Word) obj).mContent))
            return true;
        else
            return false;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + mId;
        result = 31 * result + mContent.hashCode();
        return result;
    }
}
