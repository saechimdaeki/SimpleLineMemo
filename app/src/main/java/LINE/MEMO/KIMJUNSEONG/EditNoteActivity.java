package LINE.MEMO.KIMJUNSEONG;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;

public class EditNoteActivity extends AppCompatActivity {
    private EditText inputNote;
    private NotesDao dao;
    private EditText inputbody;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edite);
        inputNote = findViewById(R.id.input_note);
        inputbody= findViewById(R.id.input_note_body);
        dao = NotesDB.getInstance(this).notesDao();
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
            onSaveNote();
        if (id==R.id.access_gallery)
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
            Note note = new Note(text, date); // Create new Note
            dao.insertNote(note); // insert and save note to database

            finish(); // return to the MainActivity
        }

    }
}