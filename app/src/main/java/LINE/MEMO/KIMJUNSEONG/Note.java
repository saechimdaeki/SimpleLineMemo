package LINE.MEMO.KIMJUNSEONG;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "notes")
public class Note {
    @PrimaryKey(autoGenerate = true)
    private int id=-1;
    @ColumnInfo(name = "text")
    private String text;
    @ColumnInfo(name = "date")
    private long date;
    public Note(){

    }


    public Note(String text,long date){
        this.text=text;
        this.date=date;

    }
    public String getText(){
        return text;
    }

    public void setText(String text){
        this.text=text;
    }

    public long getDate(){
        return date;
    }
    public void setDate(long date){
        this.date=date;
    }
    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id=id;
    }
}
