package LINE.MEMO.KIMJUNSEONG;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
            showdial();
          //  startActivity(new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA));

        }
        return super.onOptionsItemSelected(item);
    }

    private void onSaveNote() {

        String text = inputNote.getText().toString();
        String textbody=inputbody.getText().toString();
        if (!text.isEmpty()) {
            long date = new Date().getTime();
            //Note note = new Note(text, date);
             //dao.insertNote(note);
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
           // Note note = new Note(text, date);
          //  dao.insertNote(note);
            finish();
        }

    }
    void showdial()
    {
        final List<String> ListItems = new ArrayList<>();
        ListItems.add("갤러리에서 가져오기");
        ListItems.add("카메라로 촬영하기");
        ListItems.add("URL로 가져오기");
        final CharSequence[] items =  ListItems.toArray(new String[ ListItems.size()]);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("이미지를 추가하려고해요 방식을 알려주세요~");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int pos) {
                if(pos==0)
                {
                    Intent intent = new Intent();
                    intent.setAction(android.content.Intent.ACTION_VIEW);
                    intent.setType("image/*");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                else if(pos==1)
                    startActivity(new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA));

            }
        });
        builder.show();
    }
}