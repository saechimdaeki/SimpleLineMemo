package LINE.MEMO.KIMJUNSEONG;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NoteDate {
    public static String format(long longtype){
        DateFormat dateFormat=new SimpleDateFormat("마지막 수정: yyyy년 MM월 dd일", Locale.KOREA);
        return dateFormat.format(new Date(longtype));
    }
}
