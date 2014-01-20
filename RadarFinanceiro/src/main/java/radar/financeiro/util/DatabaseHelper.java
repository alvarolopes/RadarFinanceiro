package radar.financeiro.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import android.util.Log;

import radar.financeiro.Model.Debito;


public class DatabaseHelper extends SQLiteOpenHelper {

    // Logcat tag
    private static final String LOG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 4;

    // Database Name
    private static final String DATABASE_NAME = "contactsManager";

    // Table Names
    private static final String TABLE_DEBITOS = "debitos";

    // Common column names
    private static final String KEY_ID = "codigo";
    private static final String KEY_CREATED_AT = "created_at";

    // NOTES Table - column nmaes
    private static final String KEY_DATA = "data";
    private static final String KEY_VALOR = "valor";
    private static final String KEY_ESTABELECIMENTO = "estabelecimento";

    // Table Create Statements
    // Todo table create statement
    private static final String CREATE_TABLE_DEBITOS =
            "CREATE TABLE " + TABLE_DEBITOS + " ( "
                    + KEY_ID + " INTEGER PRIMARY KEY, "
                    + KEY_DATA + " DATETIME, "
                    + KEY_VALOR + " DOUBLE, "
                    + KEY_ESTABELECIMENTO + " TEXT, "
                    + KEY_CREATED_AT + " DATETIME "
                    + ")";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(CREATE_TABLE_DEBITOS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DEBITOS);

        // create new tables
        onCreate(db);
    }

    public long createDebito(Debito debito) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DATA, debito.getDateTime());
        values.put(KEY_VALOR, debito.getValor());
        values.put(KEY_ESTABELECIMENTO, debito.getEstabelecimento());
        values.put(KEY_CREATED_AT, getCreatedAtDateTime());

        // insert row
        long todo_id = db.insert(TABLE_DEBITOS, null, values);

        return todo_id;
    }

    private String getCreatedAtDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    /*
 * get single debito
 */
    public Debito getDebito(long todo_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_DEBITOS + " WHERE "
                + KEY_ID + " = " + todo_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Debito td = new Debito();
        td.setCodigo(c.getInt(c.getColumnIndex(KEY_ID)));
        td.setValor(c.getDouble(c.getColumnIndex(KEY_VALOR)));
        td.setData(c.getString(c.getColumnIndex(KEY_DATA)));
        td.setEstabelecimento((c.getString(c.getColumnIndex(KEY_ESTABELECIMENTO))));


        return td;
    }

    /*
 * getting all debitos
 * */
    public ArrayList<Debito> getAllDebitos() {
        ArrayList<Debito> debitos = new ArrayList<Debito>();
        String selectQuery = "SELECT  * FROM " + TABLE_DEBITOS;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Debito td = new Debito();
                td.setCodigo(c.getInt(c.getColumnIndex(KEY_ID)));
                td.setValor(c.getDouble(c.getColumnIndex(KEY_VALOR)));
                td.setData(c.getString(c.getColumnIndex(KEY_DATA)));
                td.setEstabelecimento((c.getString(c.getColumnIndex(KEY_ESTABELECIMENTO))));

                // adding to todo list
                debitos.add(td);
            } while (c.moveToNext());
        }

        return debitos;
    }


    /*
 * Deleting a Debito
 */
    public void deleteDebito(long tado_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_DEBITOS, KEY_ID + " = ?",
                new String[] { String.valueOf(tado_id) });
    }

    // closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }
}