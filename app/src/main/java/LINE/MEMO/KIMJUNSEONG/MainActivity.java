package LINE.MEMO.KIMJUNSEONG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private WriteFrag writeFrag;
    private listFrag listFg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(savedInstanceState==null){
            Intent intent = new Intent(this,LoadingActivity.class);
            startActivity(intent);
        }

        bottomNavigationView = findViewById(R.id.bottom);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.note:
                        setFrag(0);
                        break;
                    case R.id.pen:
                        setFrag(1);
                        break;
                }
                return true;
            }
        });
        writeFrag = new WriteFrag();
        listFg = new listFrag();


        setFrag(0); // first frag

    }

    // fragment change in main f
    private void setFrag(int n) {
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        switch (n) {
            case 0:
                listFg=new listFrag();
                ft.replace(R.id.main_frame, listFg);
                ft.commit();
                break;
            case 1:
                writeFrag=new WriteFrag();
                ft.replace(R.id.main_frame, writeFrag);
                ft.commit();
                break;

        }


    }

}