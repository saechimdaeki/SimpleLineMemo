package LINE.MEMO.KIMJUNSEONG;

import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

public abstract class MainCallback implements ActionMode.Callback {
    private ActionMode action;
    public MenuItem countitem;
    @Override
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
        actionMode.getMenuInflater().inflate(R.menu.menu_action_mode,menu);
        this.action=actionMode;
        this.countitem=menu.findItem(R.id.action_checked_count);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
        return false;
    }



    @Override
    public void onDestroyActionMode(ActionMode actionMode) {

    }
    public void setCount(String checkedCount){
        if(countitem!=null)
        this.countitem.setTitle(checkedCount);
    }

    public ActionMode getAction() {
        return action;
    }
}
