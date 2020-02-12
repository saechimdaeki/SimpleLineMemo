package LINE.MEMO.KIMJUNSEONG;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NoteDate {
    public static String format(long longtype){
        DateFormat dateFormat=new SimpleDateFormat("EEE, dd MMM yyyy 'at' hh:mm aaa", Locale.KOREA);
        return dateFormat.format(new Date(longtype));
    }
}
