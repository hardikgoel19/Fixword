package game.word.fix.com.fixword;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.Random;

public class DbHelperWord{

    private static final String DATABASE_NAME = "Database.db";
    private static final String COLUMN_1 = "word";
    private static final String COLUMN_2 = "level";
    private static final String COLUMN_3 = "status";
    private static final String TABLE_NAME = "words";
    private DbCopy dbCopy;

    DbHelperWord(Context context) {
        dbCopy = new DbCopy(context);
    }

    public void putStatus(String word, int level, int status) {
        SQLiteDatabase db = dbCopy.getDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_1, word);
        contentValues.put(COLUMN_2, level);
        contentValues.put(COLUMN_3, status);
        String where = COLUMN_1 + " = '"+word+"' and "+COLUMN_2+"="+level+" and "+COLUMN_3 + "="+0;
        db.update(TABLE_NAME,contentValues,where,new String[]{});
        db.close();
    }

    public String getRandomWord(int level) {
        SQLiteDatabase db = dbCopy.getDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where level = "+level + " and status = 0", null);
        ArrayList<String> list = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                list.add(cursor.getString(cursor.getColumnIndex(COLUMN_1)));
            }
            cursor.close();
            int random = new Random().nextInt(list.size());
            db.close();
            return list.get(random);
        }
        db.close();
        return "";
    }

}
