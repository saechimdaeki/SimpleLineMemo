package LINE.MEMO.KIMJUNSEONG;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class note_adapter extends RecyclerView.Adapter<note_adapter.noteholder> {
    public Context context;
    public ArrayList<Note> notelist;
    private NoteEventListener listener;
    private boolean multiCheck = false;

    public note_adapter(Context context, ArrayList<Note> notelist) {
        this.notelist = notelist;
        this.context = context;
    }

    @Override
    public noteholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note, parent, false);

        return new noteholder(view);
    }

    @Override
    public void onBindViewHolder(noteholder holder, int position) {
        final Note note = getnotes(position);

        if (note != null) {
            holder.notetext.setText(note.getText());
            holder.notedate.setText(NoteDate.format(note.getDate()));
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
            if ("cmp".equals(holder.thumbnail.getTag())) {
                /*
                holder.thumbnail.setVisibility(View.INVISIBLE);///첫번째 사진이 없을시 썸네일 보이지않음 .
                holder.thumbnail.setTag("cmpppp");
                Bundle extras=((Activity) context).getIntent().getExtras();

                int imageRes= 0;
                if (extras != null) {
                    imageRes = extras.getInt("IMAGE_RES");
                    Log.v("되고있긴하냐 ", "어?"+imageRes);
                }else{
                    Log.v("안대네 ", "어어?"+imageRes);
                }
                holder.thumbnail.setImageResource(imageRes);
                holder.thumbnail.setVisibility(View.VISIBLE);

                 */
            } else {
                //Intent intent=((Activity) context).getIntent();//   어댑터에서 getintent
                /*
                holder.thumbnail.setTag("cmpppp");
                Bundle extras=((Activity) context).getIntent().getExtras();

                int imageRes= 0;
                if (extras != null) {
                    imageRes = extras.getInt("IMAGE_RES");
                }
                holder.thumbnail.setImageResource(imageRes);
                holder.thumbnail.setVisibility(View.VISIBLE);

                 */
                ;
            }

            if (multiCheck) {
                holder.checkBox.setVisibility(View.VISIBLE);
                holder.checkBox.setChecked(note.isChecked());
            } else holder.checkBox.setVisibility(View.GONE);

        }
    }

    @Override
    public int getItemCount() {
        return notelist.size();
    }

    private Note getnotes(int i) {
        return notelist.get(i);
    }

    public List<Note> getCheckedNotes() {   //// 더이상 사용하지 않습니다
        List<Note> chckedNotes = new ArrayList<>();
        for (Note n : this.notelist) {
            if (n.isChecked())
                chckedNotes.add(n);
        }
        return chckedNotes;
    }

    public void setListener(NoteEventListener listener) {
        this.listener = listener;
    }

    public void setMultiCheck(boolean multiCheck) { ///더이상 사용하지 않습니다
        this.multiCheck = multiCheck;
        if (!multiCheck)
            for (Note note : this.notelist) {
                note.setChecked(false);
            }
        notifyDataSetChanged();
    }

    class noteholder extends RecyclerView.ViewHolder {
        TextView notetext, notedate, notebody;
        ImageView thumbnail;
        CheckBox checkBox;

        public noteholder(View itemView) {
            super(itemView);
            notedate = itemView.findViewById(R.id.note_Date);
            notetext = itemView.findViewById(R.id.note);
            checkBox = itemView.findViewById(R.id.checkbox);
            notebody = itemView.findViewById(R.id.note_body);
            thumbnail = itemView.findViewById(R.id.thumbnail_note);


        }
    }
}
