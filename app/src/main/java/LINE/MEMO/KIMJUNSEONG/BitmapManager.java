package LINE.MEMO.KIMJUNSEONG;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class BitmapManager {
    public static Bitmap byteToBitmap(byte[] bytes){
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        //        Log.v("비트맵매니저","너는잘되고있는거 맞긴혀?"+ bytes+ " "+bytes.length);
        return bitmap;
    }
    public static byte[] bitmapToByte(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
}
