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
    public static Bitmap drawString(Bitmap bitmap, String text, int x, int y, int lineHeight){
        Canvas canvas=new Canvas(bitmap);
        Paint paint=new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(12);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
        canvas.drawBitmap(bitmap,0,0,paint);
        String[] texts=text.split("\n");
        for (String line:texts){
            canvas.drawText(line,x,y,paint);
            y+=lineHeight;
        }
        return bitmap;
    }

    public static Bitmap base64ToBitmap(String base64){
        byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return bitmap;
    }

    public static String bitmapToBase64(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        String base64 = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return base64;
    }

    public static Bitmap byteToBitmap(byte[] bytes){
        /*
        int width=50; int height=50;
        Bitmap bitmap=Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
        ByteBuffer buffer=ByteBuffer.wrap(bytes);
        buffer.rewind();
        bitmap.copyPixelsFromBuffer(buffer);
        return bitmap;
        */


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
