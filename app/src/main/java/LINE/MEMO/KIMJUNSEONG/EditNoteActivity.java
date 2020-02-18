package LINE.MEMO.KIMJUNSEONG;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;

public class EditNoteActivity extends AppCompatActivity {
    private EditText inputNote;
    private NotesDao dao;
    private EditText inputbody;
    private Note tmp;
    public static final String NOTE_EXTRA_KEY="note_id";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edite);
        inputNote = findViewById(R.id.input_note);
        inputbody = findViewById(R.id.input_note_body);
        dao = NotesDB.getInstance(this).notesDao();

        if (getIntent().getExtras() != null) {
            int id = getIntent().getExtras().getInt(NOTE_EXTRA_KEY, 0);
            tmp = dao.getNoteById(id);
            inputNote.setText(tmp.getText());
            inputbody.setText(tmp.getBody());
        }else inputNote.setFocusable(true);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.note_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.save_note)
        {
            if(inputNote.getText()==null||inputNote.getText().toString().equals(""))
                Toast.makeText(this, "제목을 입력해주세요~", Toast.LENGTH_SHORT).show();
            else
            onSaveNote();
        }

        else if (id==R.id.access_gallery)
        {
            startActivity(new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA));
        }
        return super.onOptionsItemSelected(item);
    }

    private void onSaveNote() {

        String text = inputNote.getText().toString();
        String textbody=inputbody.getText().toString();
        if (!text.isEmpty()) {
            long date = new Date().getTime(); // get Courent  system time
            //Note note = new Note(text, date); // Create new Note
             //dao.insertNote(note); // insert and save note to database
            if(tmp==null)
            {
                tmp=new Note(text,date,textbody);
                dao.insertNote(tmp);
            }else{
                tmp.setText(text);
                tmp.setDate(date);
                tmp.setBody(textbody);
                dao.updateNote(tmp);
            }
           // Note note = new Note(text, date); // Create new Note
          //  dao.insertNote(note); // insert and save note to database




            finish(); // return to the MainActivity
        }

    }
}