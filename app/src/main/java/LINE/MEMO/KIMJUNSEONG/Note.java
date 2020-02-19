package LINE.MEMO.KIMJUNSEONG;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "notes")
public class Note {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "text")
    private String text;
    @ColumnInfo(name = "date")
    private long date;
    @ColumnInfo(name = "body")
    private String body;


    @Ignore
    private boolean checked = false;


    /* 수정완료 2020 02 18  김준성  */
    public Note(String text, long date, String body) {
        this.text = text;
        this.date = date;
        this.body = body;

    }

    public String getText() {
        return text;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean b) {
        this.checked = checked;
    }
}
