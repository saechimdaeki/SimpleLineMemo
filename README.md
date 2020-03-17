# SimpleLineMemo
 Line플러스 Android app과제입니다. 

![LinecodingTest](https://user-images.githubusercontent.com/40031858/74128902-a55a1f00-4c21-11ea-9ccf-b11d3ae5625f.JPG)


## 기술 스택
- 사용 언어 : JAVA
- 이미지 및 스플래시 처리 라이브러리 : [Glide](https://github.com/bumptech/glide)
- DB: [Room](https://developer.android.com/topic/libraries/architecture/room)

DB설계 
<code>
 
     @Dao
    public interface NotesDao {
    
     
     * @param note
     
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNote(Note note);
     * @param note that will be delete
    @Delete
    void deleteNote(Note... note);
     * @param note the note that will be update
    @Update
    void updateNote(Note note);
     * @return list of Notes
    @Query("SELECT * FROM notes")
    List<Note> getNotes();
     * @param noteId note id
     * @return Note
    @Query("SELECT * FROM notes WHERE id = :noteId")
    Note getNoteById(int noteId);
     * Delete Note by Id from DataBase
     * @param noteId
    @Query("DELETE FROM notes WHERE id = :noteId")
    void deleteNoteById(int noteId);
    }
 </code>
<code>
    
      @Database(entities = Note.class, version = 1, exportSchema = false)
     public abstract class NotesDB extends RoomDatabase {
     public static final String DATABSE_NAME = "notesDb";
    private static NotesDB instance;

    public static NotesDB getInstance(Context context) {
        if (instance == null)
            instance = Room.databaseBuilder(context, NotesDB.class, DATABSE_NAME)
                    .allowMainThreadQueries()
                    .build();
        return instance;
    }

    public abstract NotesDao notesDao();
    }
 
</code>



------------------

## Android Memo App Attaches Photos and Supports Thumbnails (If you don't have a thumbnail, you won't see it)

<details>
<summary>See Features for yourself</summary>
<img src=https://user-images.githubusercontent.com/40031858/75107105-d8f66980-5663-11ea-85d1-7c080234f014.png width=400px>

</details>


## Click and hold each note to share or delete the contents of that note.
<details>
<summary>See Features for yourself</summary>
<img src=https://user-images.githubusercontent.com/40031858/75108902-57eba200-5664-11ea-807a-6da71c0965c6.png width=400px>

</details>

## Each note can be swapped to the left and right to delete.

<details>
<summary>See Features for yourself</summary>
<img src=https://user-images.githubusercontent.com/40031858/75109248-0abc0000-5665-11ea-9158-13f03bd6e3ca.png width=400px>

</details>



## You can take a url or gallery or picture yourself and put it in a note

<details>
<summary>See Features for yourself</summary>
<img src=https://user-images.githubusercontent.com/40031858/75109261-3b9c3500-5665-11ea-8413-be34999afa5b.png width=400px>

</details>

-------------------------------


## ※ 첫번째가 아닌 다른 이미지를 클릭했을때 첫번째의 이미지가 없을경우 그것이 썸네일을 대처해야한다는 오류를범해서(조건누락) 과제에서 탈락하였습니다. 이점은 바로 수정하였습니다※ 

수정 내용: 

![image](https://user-images.githubusercontent.com/40031858/75515355-3a8c4e80-5a3d-11ea-9413-71ae5f832be2.png)



#### 첫 기업과제 소감: room persistence library를 처음써보아서 어려움이 있었지만 배운점이 있어서 재미 있었고 기업에서 요구하는 조건을 자세히좀 읽어보고 다음에는 실수를 범하지 말아야겠습니다. 

