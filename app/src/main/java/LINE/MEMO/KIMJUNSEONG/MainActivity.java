package LINE.MEMO.KIMJUNSEONG;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import static LINE.MEMO.KIMJUNSEONG.EditNoteActivity.NOTE_EXTRA_KEY;

//import android.widget.Toolbar;

public class MainActivity extends AppCompatActivity implements NoteEventListener{
    private RecyclerView recyclerView;
    private ArrayList<Note> notes;
    private note_adapter adapter;
    private NotesDao dao;
    private Toolbar supportActionBar;
    private callback actioncallback;
    private int chackedCount = 0;
    private FloatingActionButton fab;
    private  BackButtonPressHandler backButtonPressHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(savedInstanceState==null){
            Intent intent = new Intent(this,LoadingActivity.class);
            startActivity(intent);
        }
        backButtonPressHandler=new BackButtonPressHandler(this);
        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView=findViewById(R.id.notes_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

         fab=(FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAddnoewnote();
            }
        });
        dao=NotesDB.getInstance(this).notesDao();

    }
    private void onAddnoewnote(){

        startActivity(new Intent(this, EditNoteActivity.class));
    }
    private void loadnote(){
        this.notes=new ArrayList<>();

        List<Note> list=dao.getNotes();
        this.notes.addAll(list);

        this.adapter=new note_adapter(this,this.notes);
        this.adapter.setListener(this);

        this.recyclerView.setAdapter(adapter);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();
        loadnote();
    }
    @Override
    public void onBackPressed(){
        backButtonPressHandler.onBackPressed();
    }

    @Override
    public void onNoteClick(Note note) {
        Intent intent=new Intent(this,EditNoteActivity.class);
        intent.putExtra(NOTE_EXTRA_KEY,note.getId());
        startActivity(intent);
    }

    @Override
    public void onNoteLongClick(Note note) {

        note.setChecked(true);
        chackedCount=1;
        adapter.setMultiCheck(true);
        adapter.setListener(new NoteEventListener() {
            @Override
            public void onNoteClick(Note note) {
                note.setChecked(!note.isChecked());
                if(note.isChecked())
                    chackedCount++;
                else chackedCount--;
                if(chackedCount>1){
                    actioncallback.changeShareItemVisible(false);
                }else actioncallback.changeShareItemVisible(true);
                if(chackedCount==0){
                    actioncallback.getAction().finish();
                }
                actioncallback.setCount(chackedCount+"/"+notes.size());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onNoteLongClick(Note note) {

            }
        });
        actioncallback=new callback() {
            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                if(menuItem.getItemId()==R.id.action_delete_notes)
                    onDeleteMultiNotes();
                else if(menuItem.getItemId()==R.id.action_share_note)
                    onSharedNote();
                actionMode.finish();
                return false;
            }
        };
        startActionMode(actioncallback);
        fab.setVisibility(View.GONE);
        actioncallback.setCount(chackedCount+"/"+notes.size());


    }

    @Override
    public void onActionModeFinished(ActionMode mode){
        super.onActionModeFinished(mode);

        adapter.setMultiCheck(false);
        adapter.setListener(this);
        fab.setVisibility(View.VISIBLE);
    }
    private void onDeleteMultiNotes(){
        List<Note> chackedNotes = adapter.getCheckedNotes();
        if (chackedNotes.size() != 0) {
            for (Note note : chackedNotes) {
                dao.deleteNote(note);
            }

            loadnote();
            Toast.makeText(this, chackedNotes.size() + "메모 삭제완료", Toast.LENGTH_SHORT).show();
        } else Toast.makeText(this, "메모가 선택되지 않았네요 ", Toast.LENGTH_SHORT).show();
    }
    private void onSharedNote(){
        Note note = adapter.getCheckedNotes().get(0);
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        String notetext = note.getText() + "\n\n Create on : " +
                NoteDate.format(note.getDate()) + "\n  By :" +
                getString(R.string.app_name);
        share.putExtra(Intent.EXTRA_TEXT, notetext);
        startActivity(share);
    }

}