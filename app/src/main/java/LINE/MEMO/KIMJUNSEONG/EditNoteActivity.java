package LINE.MEMO.KIMJUNSEONG;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.exifinterface.media.ExifInterface;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EditNoteActivity extends AppCompatActivity {
    public static final String NOTE_EXTRA_KEY = "note_id";
    private static final int gallery = 1;
    private static final int CAMERA = 2;
    private static final int imgURL = 3;
    ImageView imageView;
    ImageView imagethumbnail;
    GridView gridView;
    private EditText inputNote;
    private NotesDao dao;
    private EditText inputbody;
    private Note tmp;
    private Boolean permission = MainActivity.permission;
    private String imageFilePath;
    private Uri photoUri;
    private File file;
    private Boolean thumbnail = false;
    int imageRes;
    Bitmap bitmap;
    public Integer[] mThumblds = {R.drawable.test1, R.drawable.test2, R.drawable.test3,
            R.drawable.test4, R.drawable.test5, R.drawable.test5,
            R.drawable.test6, R.drawable.test7, R.drawable.test8,
            R.drawable.test9, R.drawable.test10, R.drawable.test11,
            R.drawable.test12, R.drawable.test13, R.drawable.test14,
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edite);
        gridView = findViewById(R.id.gridView01);
        //ImageAdapter imageAdapter = new ImageAdapter(this);
        gridView.setAdapter(new ImageAdapterGridView(this));
        inputNote = findViewById(R.id.input_note);
        inputbody = findViewById(R.id.input_note_body);
        dao = NotesDB.getInstance(this).notesDao();

        if (getIntent().getExtras() != null) {
            int id = getIntent().getExtras().getInt(NOTE_EXTRA_KEY, 0);
            int photo = getIntent().getExtras().getInt(NOTE_EXTRA_KEY, 0);
            tmp = dao.getNoteById(photo);
            tmp = dao.getNoteById(id);
            inputNote.setText(tmp.getText());
            inputbody.setText(tmp.getBody());
        } else inputNote.setFocusable(true);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                imageView = (ImageView) view;
                if (position == 0) {
                   imageRes = mThumblds[0];
                    thumbnail=true;



                }
                showdial();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.note_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.save_note) {
            if (inputNote.getText() == null || inputNote.getText().toString().equals(""))
                Toast.makeText(this, "제목을 입력해주세요~", Toast.LENGTH_SHORT).show();
            else
                onSaveNote();
        }
        return super.onOptionsItemSelected(item);
    }

    private void onSaveNote() {

        String text = inputNote.getText().toString();
        String textbody = inputbody.getText().toString();
        if (!text.isEmpty()) {
            long date = new Date().getTime();
            //Note note = new Note(text, date);
            //dao.insertNote(note);
            if (tmp == null) {
                tmp = new Note(text, date, textbody);
                dao.insertNote(tmp);
            } else {
                tmp.setText(text);
                tmp.setDate(date);
                tmp.setBody(textbody);
                dao.updateNote(tmp);
            }
            // Note note = new Note(text, date);
            //  dao.insertNote(note);
            if (thumbnail) {
                ////Todo  :     이걸 넘겨야 혀
                Intent intent = new Intent(EditNoteActivity.this,note_adapter.class);
                intent.putExtra("IMAGE_RES", imageRes);
//                BitmapDrawable bitbit=(BitmapDrawable) imagethumbnail.getDrawable();
//                bitmap=bitbit.getBitmap();


            }

            finish();
        }
    }

    void showdial() {
        final List<String> ListItems = new ArrayList<>();
        ListItems.add("갤러리에서 가져오기");
        ListItems.add("카메라로 촬영하기");
        ListItems.add("URL로 가져오기");
        final CharSequence[] items = ListItems.toArray(new String[ListItems.size()]);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("이미지를 추가하려고해요 방식을 알려주세요~");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int pos) {
                if (pos == 0) //갤러리
                {
                    if (permission) {
                        ALBUM();
                    } else {
                        Toast.makeText(EditNoteActivity.this, "갤러리 접근 및 사진 촬영을위해 권한이 필요합니다", Toast.LENGTH_SHORT).show();
                    }
                } else if (pos == 1)//사진
                {
                    if (permission) {
                        sendTakePhotoIntent();
                    } else {
                        Toast.makeText(EditNoteActivity.this, "갤러리 접근 및 사진 촬영을위해 권한이 필요합니다", Toast.LENGTH_SHORT).show();
                    }
                } else if (pos == 2)// url
                {
                    if (permission) {
                        inserturl();
                    } else {
                        Toast.makeText(EditNoteActivity.this, "권한을 허용해주세요", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        builder.show();
    }

    private void inserturl() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText et = new EditText(getApplicationContext());
        builder.setTitle("URL을 입력해주세요").setMessage("URL을 입력바랍니다").setView(et);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                String abcd = et.getText().toString();
                Glide.with(getApplicationContext())
                        .asBitmap()
                        .load(abcd)
                        .into(new CustomTarget<Bitmap>(50, 50) {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                imageView.setImageBitmap(resource);
                                // ((ImageView)findViewById(R.id.imageView1)).setImageBitmap(resource);
                                // ((ImageView)findViewById(R.id.imageView1)).setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {
                            }
                        });

            }
        });
        AlertDialog alertDialog = builder.create();

        alertDialog.show();

        /*
        if(findViewById(R.id.imageView1).getResources().equals(R.drawable.ic_add_black_24dp))
        {
            Toast.makeText(this, "존재하지 않는 URL입니다.", Toast.LENGTH_SHORT).show();
        }

         */
        /*
        URL imageurl = new URL("https://user-images.githubusercontent.com/40031858/74790980-90098280-52fc-11ea-9090-e927d209f0d1.png");
        Bitmap bitmap = BitmapFactory.decodeStream(imageurl.openConnection().getInputStream());
        ((ImageView)findViewById(R.id.imageView1)).setImageBitmap(bitmap);
        */
        //startActivityForResult(, gallery);
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "LINEPLUS" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        imageFilePath = image.getAbsolutePath();
        return image;
    }

    private void ALBUM() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, gallery);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();
            if (file != null) {
                if (file.exists()) {
                    if (file.delete()) {
                        file = null;
                    }
                }
            }
            return;
        }
        if (requestCode == gallery) {
            Uri photoUri = data.getData();
            Cursor cursor = null;
            try {
                String[] proj = {MediaStore.Images.Media.DATA};
                assert photoUri != null;
                cursor = getContentResolver().query(photoUri, proj, null, null, null);
                assert cursor != null;
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                file = new File(cursor.getString(column_index));
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
            setImage();
        } else if (requestCode == CAMERA) {
            Bitmap bitmap = BitmapFactory.decodeFile(imageFilePath);
            ExifInterface exif = null;
            try {
                exif = new ExifInterface(imageFilePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            int exifOrientation;
            int exifDegree;
            if (exif != null) {
                exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                exifDegree = exifOrientationToDegrees(exifOrientation);
            } else {
                exifDegree = 0;
            }

            imageView.setImageBitmap(rotate(bitmap, exifDegree));


            //setImage();
        }
        /* url 은 처리안해도됨  */
        //else if(requestCode==)
    }

    private void setImage() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap originalBm = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
        imageView.setImageBitmap(originalBm);
        imageView.setVisibility(View.VISIBLE);
        file = null;
    }

    private void sendTakePhotoIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Toast.makeText(this, "파일 생성에 실패하였습니다 kjs", Toast.LENGTH_SHORT).show();
            }

            if (photoFile != null) {
                photoUri = FileProvider.getUriForFile(this, getPackageName(), photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, CAMERA);
            }
        }
    }

    private int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    private Bitmap rotate(Bitmap bitmap, float degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }
    public class ImageAdapterGridView extends BaseAdapter {
        private Context mContext;

        public ImageAdapterGridView(Context c) {
            mContext = c;
        }

        public int getCount() {
            return mThumblds.length;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView mImageView;

            if (convertView == null) {
                mImageView = new ImageView(mContext);
                mImageView.setLayoutParams(new GridView.LayoutParams(200, 200));
                mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                mImageView.setPadding(16, 16, 16, 16);
            } else {
                mImageView = (ImageView) convertView;
            }
            mImageView.setImageResource(mThumblds[position]);
            return mImageView;
        }
    }
}