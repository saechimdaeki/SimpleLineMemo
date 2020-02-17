package LINE.MEMO.KIMJUNSEONG;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

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
        /*
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        */
        FloatingActionButton fab=(FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //todo
                onAddnoewnote();
            }
        });
        dao=NotesDB.getInstance(this).notesDao();

    }
    private void onAddnoewnote(){
        /*
        if(notes!=null)
            notes.add(new note("this is new note",new Date().getTime()));
        if(n_adapter!=null)
            n_adapter.notifyDataSetChanged();

         */
        startActivity(new Intent(this, EditNoteActivity.class));
    }
    private void loadnote(){
        this.notes=new ArrayList<>();
        /*
        for (int i=0; i<12; i++){
            notes.add(new note("라인플러스 김준성과제",new Date().getTime()));
        }
        n_adapter=new note_adapter(this,notes);
        recyclerView.setAdapter(n_adapter);
    //    n_adapter.notifyDataSetChanged();
    */
        List<Note> list=dao.getNotes();
        this.notes.addAll(list);

        this.adapter=new note_adapter(this,this.notes);
        this.adapter.setListener(this);

        this.recyclerView.setAdapter(adapter);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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
    public void onNoteLongClick(final Note note) {
        new AlertDialog.Builder(this)
                .setTitle("LINE PLUS 김준성")
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dao.deleteNote(note);
                        loadnote();
                    }
                })
                .setNegativeButton("공유", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent share=new Intent(Intent.ACTION_SEND);
                        share.setType("text/plain");
                        share.putExtra(Intent.EXTRA_TEXT,note.getText()+"\n"+"by 김준성");
                        startActivity(share);
                    }
                })
                .create()
                .show();;
    }
}