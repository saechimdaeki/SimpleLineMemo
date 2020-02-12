package LINE.MEMO.KIMJUNSEONG;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class note_adapter extends RecyclerView.Adapter<note_adapter.noteholder> {
    public Context context;
    public ArrayList<Note> notelist;
    public note_adapter(Context context,ArrayList<Note> notelist){
        this.notelist=notelist;
        this.context=context;
    }

    @Override
    public noteholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.note,parent,false);

        return new noteholder(view);
    }

    @Override
    public void onBindViewHolder(noteholder holder, int position) {
        Note n=getnotes(position);
        if(n!=null){
            holder.notetext.setText(n.getText());
            holder.notedate.setText(NoteDate.format(n.getDate()));
        }
    }

    @Override
    public int getItemCount() {
        return notelist.size();
    }
    private Note getnotes(int i){
        return notelist.get(i);
    }
    class noteholder extends RecyclerView.ViewHolder{
        TextView notetext,notedate;
        public noteholder(View itemView){
            super(itemView);
            notedate=itemView.findViewById(R.id.note_Date);
            notetext=itemView.findViewById(R.id.note);
        }
    }
}
