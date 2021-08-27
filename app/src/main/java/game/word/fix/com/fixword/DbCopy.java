package game.word.fix.com.fixword;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DbCopy extends SQLiteOpenHelper {

    private Context context;
    private static String DB_NAME = "Database.db";
    private static String DB_PATH = "/data/data/" + BuildConfig.APPLICATION_ID + "/databases/";

    DbCopy(Context context) {
        super(context, DB_NAME, null, 1);
        this.context = context;
        if (!new File(DB_PATH + DB_NAME).exists())
            copydatabase();

    }

    private void copydatabase() {
        InputStream myinput = null;
        try {
            myinput = context.getAssets().open(DB_NAME);
            String outfilename = DB_PATH + DB_NAME;
            File f = new File(DB_PATH);
            if (!f.exists()) {
                f.mkdirs();
            }
            OutputStream myoutput = new FileOutputStream(outfilename);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myinput.read(buffer)) > 0) {
                myoutput.write(buffer, 0, length);
            }
            myoutput.flush();
            myoutput.close();
            myinput.close();
        } catch (IOException e) {
            Log.d("TAH11", "copydatabase: " + e.getMessage());
        }
    }

    public SQLiteDatabase getDatabase() {
        String mypath = DB_PATH + DB_NAME;
        return SQLiteDatabase.openDatabase(mypath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}