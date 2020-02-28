package LINE.MEMO.KIMJUNSEONG;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import static LINE.MEMO.KIMJUNSEONG.BitmapManager.byteToBitmap;

public class note_adapter extends RecyclerView.Adapter<note_adapter.noteholder> {
    public Context context;
    public ArrayList<Note> notelist;
    private NoteEventListener listener;
    boolean  check=EditNoteActivity.thumbnailclick;;
    boolean check2=EditNoteActivity.imageclickcheck2;
    boolean check3=EditNoteActivity.imageclickcheck3;
    boolean check4=EditNoteActivity.imageclickcheck4;
    boolean check5=EditNoteActivity.imageclickcheck5;
    public note_adapter(Context context, ArrayList<Note> notelist) {
        this.notelist = notelist;
        this.context = context;
    }
    @Override
    public noteholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note, parent, false);
        check=true;
        return new noteholder(view);
    }
    @Override
    public void onBindViewHolder(noteholder holder, int position) {
        final Note note = getnotes(position);
       String tmp ="";
        if (note != null) {
            holder.notetext.setText(note.getText());
            holder.notedate.setText(NoteDate.format(note.getDate()));
            if(note.getBody().length()>=80) /* 글자수 80글자이상일시 본문의 일부만 나오게 */
            {
                for(int i=0; i<80; i++)
                {
                    tmp+=note.getBody().charAt(i);
                }
                tmp+=".....";
                holder.notebody.setText(tmp);
            }else
            holder.notebody.setText(note.getBody());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onNoteClick(note);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    listener.onNoteLongClick(note);
                    return false;
                }
            });
            if(check)
            {
                Bitmap bitmap= byteToBitmap(note.getImage());
               // Log.v("bitmap","잘받긴햇니?"+byteToBitmap(note.getImage()));
                holder.thumbnail.setImageBitmap(Bitmap.createScaledBitmap(bitmap,100,100,false));
                holder.thumbnail.setVisibility(View.VISIBLE);
            }
        }
    }
    @Override
    public int getItemCount() {
        return notelist.size();
    }

    private Note getnotes(int i) {
        return notelist.get(i);
    }

    public void setListener(NoteEventListener listener) {
        this.listener = listener;
    }

    class noteholder extends RecyclerView.ViewHolder {
        TextView notetext, notedate, notebody;
        ImageView thumbnail;
        int height;
        public noteholder(View itemView) {
            super(itemView);
            notedate = itemView.findViewById(R.id.note_Date);
            notetext = itemView.findViewById(R.id.note);
            notebody = itemView.findViewById(R.id.note_body);
            thumbnail = itemView.findViewById(R.id.thumbnail_note);
        }
    }
}
