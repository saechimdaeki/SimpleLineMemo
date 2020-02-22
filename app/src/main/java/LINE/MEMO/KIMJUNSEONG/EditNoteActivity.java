package LINE.MEMO.KIMJUNSEONG;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
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
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static LINE.MEMO.KIMJUNSEONG.BitmapManager.byteToBitmap;

public class EditNoteActivity extends AppCompatActivity {
    public static final String NOTE_EXTRA_KEY = "note_id";
    private static final int gallery = 1;
    private static final int CAMERA = 2;
    private static final int imgURL = 3;
    ImageView imageView;
    public static boolean thumbnailclick =false;
    GridView gridView;
    private EditText inputNote;
    private NotesDao dao;
    private EditText inputbody;
    private Note tmp;
    private Boolean permission = MainActivity.permission;
    private String imageFilePath;
    private Uri photoUri;
    private File file;
    ImageView imgthumb;   ////썸네일 이미지
    ImageView img2,img3,img4,img5;
    Bitmap bitmap;
    private boolean deletethumbnail=false;
    private boolean delete2=false,delete3=false,delete4=false,delete5=false;
    private boolean glidecheck=true;
    private boolean otherimg=true; //썸네일이아님
    private int tmpposition;//gridview이미지 위함.
    Bitmap bitmapthumb; ///db에서 가져온첫번째 그리드뷰 이미지 
    Bitmap bitmapgrid2,bitmapgrid3,bitmapgrid4,bitmapgrid5; //db에서가져온 그리드뷰 나머지들
    byte[]image;    ///바이트
    byte[] imagegrid2,imagegrid3,imagegrid4,imagegrid5,imagegrid6,imagegrid7,imagegrid8,imagegrid9,imagegrid10;
    //TODO : 이미지가 변경되고 유지되게끔 해야함
    private Integer[] mThumblds = {R.drawable.test1, R.drawable.test2, R.drawable.test3,
            R.drawable.test4, R.drawable.test5
    };
    /*
    private boolean[] imageclickcheck={   /////그리드뷰 이미지가 클릭되었는지
      false, false,false,false,false,false,false,false,false,false
    };*/

    Boolean imageclickcheck2=false,imageclickcheck3=false,imageclickcheck4=false,imageclickcheck5=false;/////그리드뷰 이미지가 클릭되었는지

    boolean dbcheck=true; //썸네일db와 그리드 첫번째 db비교
    boolean dbcheckgrid2=true,dbcheckgrid3=true,dbcheckgrid4=true,dbcheckgrid5=true; //나머지 비교
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edite);
        gridView = findViewById(R.id.gridView01);
        inputNote = findViewById(R.id.input_note);
        inputbody = findViewById(R.id.input_note_body);
        thumbnailclick =false;
        dao = NotesDB.getInstance(this).notesDao();
        if (getIntent().getExtras() != null) {
            int id = getIntent().getExtras().getInt(NOTE_EXTRA_KEY, 0);
            tmp = dao.getNoteById(id);
            inputNote.setText(tmp.getText());
            inputbody.setText(tmp.getBody());
            bitmapthumb= byteToBitmap(tmp.getImage());
            bitmapgrid2=byteToBitmap(tmp.getGridimage2());
            bitmapgrid3=byteToBitmap(tmp.getGridimage3());
            bitmapgrid4=byteToBitmap(tmp.getGridimage4());
            bitmapgrid5=byteToBitmap(tmp.getGridimage5());
            Drawable drawable=getResources().getDrawable(R.drawable.ic_check_box_outline_blank_black_24dp);
            Bitmap bitmapcmp=getBitmap((VectorDrawable) drawable);
            Drawable drawableother=getResources().getDrawable(R.drawable.glideerror);
            Bitmap bitmapcmpother=getBitmap((VectorDrawable) drawableother);
            ///다르면 썸네일 이미지를 그대로 가져옴
            dbcheck= sameAs(bitmapthumb, bitmapcmp);  //같아도 + gridview가 나오게끔 표시
            dbcheckgrid2=sameAs(bitmapgrid2,bitmapcmpother);
            dbcheckgrid3=sameAs(bitmapgrid3,bitmapcmpother);
            dbcheckgrid4=sameAs(bitmapgrid4,bitmapcmpother);
            dbcheckgrid5=sameAs(bitmapgrid5,bitmapcmpother);
        } else inputNote.setFocusable(true);
        gridView.setAdapter(new ImageAdapterGridView(this));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                imageView = (ImageView) view;
                tmpposition=position;
                if(position==0)  ////첫번째 사진클릭할시에만 썸네일 올라가게끔
                {
                    thumbnailclick =true;
                    imgthumb=(ImageView)view;
                    otherimg=false;
                }else{
                    otherimg=true;
                    if(position==1)
                        imageclickcheck2=true;
                    if(position==2)
                        imageclickcheck3=true;
                    if(position==3)
                        imageclickcheck4=true;
                    if(position==4)
                        imageclickcheck5=true;

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
        if(id==R.id.delete_note)
        {
            onDeleteNote();
        }
        return super.onOptionsItemSelected(item);
    }
    private void onSaveNote() {
        String text = inputNote.getText().toString();
        String textbody = inputbody.getText().toString();
        if(thumbnailclick && glidecheck)///여기선 otherimg가 필요가없습니다.
        {
            image=BitmapManager.bitmapToByte(bitmap);
           // Log.v("체크지점1","체크지점1");
        }
        else if(!dbcheck)     ////,예외적으로 db체크가 false일시에 다시 editnote들어온다음에 저장하여도 사진이남게끔
        {
          //  Drawable drawable=bitmapthumb;
            if(deletethumbnail){
                Drawable drawable=getResources().getDrawable(R.drawable.ic_check_box_outline_blank_black_24dp);
                bitmap=getBitmap((VectorDrawable) drawable);
                image=BitmapManager.bitmapToByte(bitmap);    //썸네일 삭제하였을때 리사이클러뷰에서 썸네일이 삭제되게끔
            }else
            image=BitmapManager.bitmapToByte(bitmapthumb);
          // image= BitmapManager.bitmapToByte(getBitmap((BitmapDrawable) drawable));
          //  Log.v("체크지점2","체크지점2");
        }
        else
        {
            Drawable drawable=getResources().getDrawable(R.drawable.ic_check_box_outline_blank_black_24dp);
            bitmap=getBitmap((VectorDrawable) drawable);
            image=BitmapManager.bitmapToByte(bitmap);
           // Log.v("체크지점3","체크지점3");
        }
        if(!imageclickcheck2)   //두번째 이미지 클릭안했을때
        {
            Drawable drawable=getResources().getDrawable(R.drawable.glideerror);
            bitmap=getBitmap((VectorDrawable) drawable);
            imagegrid2=BitmapManager.bitmapToByte(bitmap);
            if(!dbcheckgrid2){  ///이미지 클릭안했고 기존에 이미지 가지고 있었을때
                imagegrid2=BitmapManager.bitmapToByte(bitmapgrid2);
            }
        }else{
            imagegrid2=BitmapManager.bitmapToByte(bitmapgrid2);
            if(delete2)
            {
                Drawable drawable=getResources().getDrawable(R.drawable.glideerror);
                bitmap=getBitmap((VectorDrawable) drawable);
                imagegrid2=BitmapManager.bitmapToByte(bitmap);
            }else
                imagegrid2=BitmapManager.bitmapToByte(bitmapgrid2);
        }
        if(!imageclickcheck3){
            if(!dbcheckgrid3){
                imagegrid3=BitmapManager.bitmapToByte(bitmapgrid3);
               // Log.v("호출이쪽","호출이쪽");
            }else{
                Drawable drawable=getResources().getDrawable(R.drawable.glideerror);
                bitmap=getBitmap((VectorDrawable) drawable);
                imagegrid3=BitmapManager.bitmapToByte(bitmap);
              //  Log.v("호출여기","호출여기");
            }
        }else{
            if(delete3){
                Drawable drawable=getResources().getDrawable(R.drawable.glideerror);
                bitmap=getBitmap((VectorDrawable) drawable);
                imagegrid3=BitmapManager.bitmapToByte(bitmap);
            }
            else
            imagegrid3=BitmapManager.bitmapToByte(bitmapgrid3);
         //   Log.v("호출저짝","호출저짝");
        }

        if(!imageclickcheck4){
            Drawable drawable=getResources().getDrawable(R.drawable.glideerror);
            bitmap=getBitmap((VectorDrawable) drawable);
            imagegrid4=BitmapManager.bitmapToByte(bitmap);
            if(!dbcheckgrid4){
                imagegrid4=BitmapManager.bitmapToByte(bitmapgrid4);
            }
        }else{
            if(delete4){
                Drawable drawable=getResources().getDrawable(R.drawable.glideerror);
                bitmap=getBitmap((VectorDrawable) drawable);
                imagegrid4=BitmapManager.bitmapToByte(bitmap);
            }
            else
                imagegrid4=BitmapManager.bitmapToByte(bitmapgrid4);
        }

        if(!imageclickcheck5){
            Drawable drawable=getResources().getDrawable(R.drawable.glideerror);
            bitmap=getBitmap((VectorDrawable) drawable);
            imagegrid5=BitmapManager.bitmapToByte(bitmap);
            if(!dbcheckgrid5){
                imagegrid5=BitmapManager.bitmapToByte(bitmapgrid5);
            }
        }else
        {
            if(delete5){
                Drawable drawable=getResources().getDrawable(R.drawable.glideerror);
                bitmap=getBitmap((VectorDrawable) drawable);
                imagegrid5=BitmapManager.bitmapToByte(bitmap);
            }
            else
                imagegrid5=BitmapManager.bitmapToByte(bitmapgrid5);
        }
        if (!text.isEmpty()) {
            long date = new Date().getTime();
            if (tmp == null) {
                tmp = new Note(text, date, textbody,image,imagegrid2,imagegrid3,imagegrid4,imagegrid5);
           //     Log.v("에디트노트","잘갔는가?"+image);
                dao.insertNote(tmp);
            } else {
                tmp.setText(text);
                tmp.setDate(date);
                tmp.setBody(textbody);
                tmp.setImage(image);
                tmp.setGridimage2(imagegrid2);
                tmp.setGridimage3(imagegrid3);
                tmp.setGridimage4(imagegrid4);
                tmp.setGridimage5(imagegrid5);
             //   Log.v("에디트노트2","잘갔는가?2"+image);
                dao.updateNote(tmp);
            }
            finish();
        }
    }
    private void onDeleteNote(){
        dao.deleteNote(tmp);
        finish();
    }

    void showdial() {
        final List<String> ListItems = new ArrayList<>();
        ListItems.add("갤러리에서 가져오기");
        ListItems.add("카메라로 촬영하기");
        ListItems.add("URL로 가져오기");
        ListItems.add("해당 이미지 삭제");
        final CharSequence[] items = ListItems.toArray(new String[ListItems.size()]);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("이미지를 추가하거나 삭제할 수 있습니다");
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
                }else if(pos==3){
                    deletephoto();
                }
            }
        });
        builder.show();
    }
    private void deletephoto(){
        if(thumbnailclick  &&!otherimg){
            imgthumb.setImageResource(R.drawable.glideerror);
            thumbnailclick=false;
            deletethumbnail=true;
          //  Log.v("이거1","이거?");
        } else {
            imageView.setImageResource(R.drawable.glideerror);
            glidecheck=false;
            otherimg=true;
            if(imageclickcheck2){
                delete2=true;
            }if(imageclickcheck3){
                delete3=true;
            }if(imageclickcheck4)
                delete4=true;
            if(imageclickcheck5)
                delete5=true;

           // Log.v("저거?","저거?");
        }
    }
    private void inserturl() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText et = new EditText(getApplicationContext());
        builder.setTitle("URL을 입력해주세요").setMessage("URL을 입력해주세요").setView(et);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                String bts = et.getText().toString();
                Glide.with(getApplicationContext())
                        .asBitmap()
                        .load(bts)
                        .error(R.drawable.cancel)
                        .into(new CustomTarget<Bitmap>(60, 60) {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                if(thumbnailclick &&glidecheck &&!otherimg){
                                    imgthumb.setImageBitmap(resource);
                                    bitmap=((BitmapDrawable)imgthumb.getDrawable()).getBitmap();
                                    glidecheck=true;
                                }
                                else{
                                    otherimg=true;
                                    imageView.setImageBitmap(resource);
                                    if(imageclickcheck2)
                                        bitmapgrid2=((BitmapDrawable)imageView.getDrawable()).getBitmap();
                                    if(imageclickcheck3)
                                        bitmapgrid3=((BitmapDrawable)imageView.getDrawable()).getBitmap();
                                    if(imageclickcheck4)
                                        bitmapgrid4=((BitmapDrawable)imageView.getDrawable()).getBitmap();
                                    if(imageclickcheck5)
                                        bitmapgrid5=((BitmapDrawable)imageView.getDrawable()).getBitmap();
                                }
                            }
                            @Override
                            public void onLoadStarted(@Nullable Drawable placeholder) {
                               Toast.makeText(EditNoteActivity.this, "잠시만 이미지를 받아올때까지 기다려주세요", Toast.LENGTH_SHORT).show();
                            }
                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {
                                glidecheck=true;
                            }
                            @Override
                            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                glidecheck=false;
                                Toast.makeText(EditNoteActivity.this, "존재하지 않는 URL이거나 맞지않는 확장자입니다.", Toast.LENGTH_SHORT).show();
                                if(thumbnailclick){
                                    imgthumb.setImageResource(R.drawable.glideerror);
                                    thumbnailclick =false;
                                    otherimg=false;
                                }else{
                                    otherimg=true;
                                    imageView.setImageResource(R.drawable.glideerror);

                                }
                            }
                        });

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    /*  사용할 수 있으므로 제거해서는 안되는 메소드     */
    private Bitmap getBitmapFromVector(VectorDrawable vectorDrawable) {
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        return bitmap;
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
            glidecheck=false;
            otherimg=true;
            if (file != null) {
                if (file.exists()) {
                    if (file.delete()) {
                        file = null;
                    }
                }
            }
            return;
        }else if(requestCode==Activity.RESULT_OK)
            glidecheck=true;
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
            Bitmap bitmap2 = BitmapFactory.decodeFile(imageFilePath);
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
                glidecheck=true;
            } else {
                exifDegree = 0;
                glidecheck=false;
            }
            if (thumbnailclick && !otherimg ){
                imgthumb.setImageBitmap(rotate(bitmap2,exifDegree));
                bitmap=((BitmapDrawable)imgthumb.getDrawable()).getBitmap();
                otherimg=false;
               //bitmap= Bitmap.createScaledBitmap(bitmap,400,400,true);
            }
            else{
                imageView.setImageBitmap(rotate(bitmap2, exifDegree));
                otherimg=true;
            }
        }
    }
    private void setImage() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap originalBm = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
        if(thumbnailclick && !otherimg){
            imgthumb.setImageBitmap(originalBm);
            bitmap=((BitmapDrawable)imgthumb.getDrawable()).getBitmap();
            bitmap= Bitmap.createScaledBitmap(bitmap,400,400,true);
            glidecheck=true;
            otherimg=false;
        }
        else{
            imageView.setImageBitmap(originalBm);
            otherimg=true;
            if(imageclickcheck2){
                bitmapgrid2=((BitmapDrawable)imageView.getDrawable()).getBitmap();
                bitmapgrid2=Bitmap.createScaledBitmap(bitmapgrid2,400,400,true);
            }
            if(imageclickcheck3){
                bitmapgrid3=((BitmapDrawable)imageView.getDrawable()).getBitmap();
                bitmapgrid3=Bitmap.createScaledBitmap(bitmapgrid3,400,400,true);
            }if(imageclickcheck4){
                bitmapgrid4=((BitmapDrawable)imageView.getDrawable()).getBitmap();
                bitmapgrid4=Bitmap.createScaledBitmap(bitmapgrid4,400,400,true);
            }if(imageclickcheck5){
                bitmapgrid5=((BitmapDrawable)imageView.getDrawable()).getBitmap();
                bitmapgrid5=Bitmap.createScaledBitmap(bitmapgrid5,400,400,true);
            }
        }
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
        return Bitmap.createScaledBitmap(bitmap,400,400,true);
    }
    @Override   /////상태저장
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

    }
    private static Bitmap getBitmap(VectorDrawableCompat vectorDrawable) {
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        return bitmap;
    }
    private static Bitmap getBitmap(VectorDrawable vectorDrawable) {
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        return bitmap;
    }
    @Override
    public void onDestroy(){
        super.onDestroy();

    }
    /* 비트맵 비교하려고 썻던메소드  */
    private boolean sameAs(Bitmap bitmap1, Bitmap bitmap2) {
        ByteBuffer buffer1 = ByteBuffer.allocate(bitmap1.getHeight() * bitmap1.getRowBytes());
        bitmap1.copyPixelsToBuffer(buffer1);

        ByteBuffer buffer2 = ByteBuffer.allocate(bitmap2.getHeight() * bitmap2.getRowBytes());
        bitmap2.copyPixelsToBuffer(buffer2);
        return Arrays.equals(buffer1.array(), buffer2.array());
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

            if(position==0 &&!dbcheck)
            {
                Drawable drawable=new BitmapDrawable(bitmapthumb);
                mImageView.setImageDrawable(drawable);
                Log.v("호출","호출1");
            }
            if(!dbcheckgrid2 && position==1)
            {
                Drawable drawable=new BitmapDrawable(bitmapgrid2);
                mImageView.setImageDrawable(drawable);
                Log.v("호출","호출2");
            }
            if(!dbcheckgrid3 && position==2){
                Drawable drawable=new BitmapDrawable(bitmapgrid3);
                mImageView.setImageDrawable(drawable);
                Log.v("호출","호출3");
            }
            if(!dbcheckgrid4 && position==3){
                Drawable drawable=new BitmapDrawable(bitmapgrid4);
                mImageView.setImageDrawable(drawable);
                Log.v("호출","호출4");
            }
            if(!dbcheckgrid5 && position==4){
                Drawable drawable=new BitmapDrawable(bitmapgrid5);
                mImageView.setImageDrawable(drawable);
                Log.v("호출","호출5");
            }


            return mImageView;
        }
    }









}