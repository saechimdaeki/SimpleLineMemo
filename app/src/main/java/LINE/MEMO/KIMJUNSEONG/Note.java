package LINE.MEMO.KIMJUNSEONG;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
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
    @ColumnInfo(name = "image", typeAffinity = ColumnInfo.BLOB)
    private byte[] image;
    private byte[] gridimage2;
    private byte[] gridimage3;
    private byte[] gridimage4;
    private byte[] gridimage5;
    private byte[] gridimage6;
    private byte[] gridimage7;
    private byte[] gridimage8;
    private byte[] gridimage9;
    private byte[] gridimage10;






    /* 수정완료 2020 02 18  김준성  */
    public Note(String text, long date, String body,byte[] image,byte[] gridimage2,byte[] gridimage3,byte[] gridimage4,byte[] gridimage5) {
        this.text = text;
        this.date = date;
        this.body = body;
        this.image=image;
        this.gridimage2=gridimage2;
        this.gridimage3=gridimage3;
        this.gridimage4=gridimage4;
        this.gridimage5=gridimage5;

    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public String getBody() {
        return body;
    }
    public void setBody(String body) {
        this.body = body;
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
    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }


    public byte[] getGridimage2() {
        return gridimage2;
    }

    public void setGridimage2(byte[] gridimage2) {
        this.gridimage2 = gridimage2;
    }

    public byte[] getGridimage3() {
        return gridimage3;
    }

    public void setGridimage3(byte[] gridimage3) {
        this.gridimage3 = gridimage3;
    }

    public byte[] getGridimage4() {
        return gridimage4;
    }

    public void setGridimage4(byte[] gridimage4) {
        this.gridimage4 = gridimage4;
    }

    public byte[] getGridimage5() {
        return gridimage5;
    }

    public void setGridimage5(byte[] gridimage5) {
        this.gridimage5 = gridimage5;
    }

    public byte[] getGridimage6() {
        return gridimage6;
    }

    public void setGridimage6(byte[] gridimage6) {
        this.gridimage6 = gridimage6;
    }

    public byte[] getGridimage7() {
        return gridimage7;
    }

    public void setGridimage7(byte[] gridimage7) {
        this.gridimage7 = gridimage7;
    }

    public byte[] getGridimage8() {
        return gridimage8;
    }

    public void setGridimage8(byte[] gridimage8) {
        this.gridimage8 = gridimage8;
    }

    public byte[] getGridimage9() {
        return gridimage9;
    }

    public void setGridimage9(byte[] gridimage9) {
        this.gridimage9 = gridimage9;
    }

    public byte[] getGridimage10() {
        return gridimage10;
    }

    public void setGridimage10(byte[] gridimage10) {
        this.gridimage10 = gridimage10;
    }
}
