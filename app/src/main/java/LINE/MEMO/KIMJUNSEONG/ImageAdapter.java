package LINE.MEMO.KIMJUNSEONG;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
    Context context;

    private Integer[] mThumblds ={R.drawable.test1, R.drawable.test2, R.drawable.test3,
            R.drawable.test4, R.drawable.test5, R.drawable.test5,
            R.drawable.test6, R.drawable.test7, R.drawable.test8,
            R.drawable.test9, R.drawable.test10, R.drawable.test11,
            R.drawable.test12, R.drawable.test13, R.drawable.test14,

    };

    public ImageAdapter(Context context){
        this.context = context;
    }

    @Override
    public int getCount() {
        return mThumblds.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if(convertView == null){
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(200,200));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }else{
            imageView = (ImageView) convertView;
        }
        imageView.setImageResource(mThumblds[position]);
        return imageView;
    }
}


