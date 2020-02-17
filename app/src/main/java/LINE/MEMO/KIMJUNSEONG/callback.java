package LINE.MEMO.KIMJUNSEONG;

import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

public abstract class callback implements ActionMode.Callback {
    private ActionMode action;
    private MenuItem countitem;
    private MenuItem shareItem;
    @Override
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
        actionMode.getMenuInflater().inflate(R.menu.menu_action_mode,menu);
        this.action=actionMode;
        this.countitem=menu.findItem(R.id.action_checked_count);
        this.shareItem=menu.findItem(R.id.action_share_note);
        return false;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
        return false;
    }



    @Override
    public void onDestroyActionMode(ActionMode actionMode) {

    }
    public void setCount(String checkedCount){
        this.countitem.setTitle(checkedCount);
    }
}
