package com.example.ayush.customerapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.SQLException;

/**
 * Created by Ayush on 02-06-2015.
 */
public class SavedCardsDB {
    public static final String KEY_ROW_ID = "_id";
    public static final String KEY_CARD_NUMBER = "cardNumber";
    public static final String KEY_NAME = "name";
    public static final String KEY_CVV = "cvv";
    public static final String KEY_CARD_LABEL = "cardLabel";
    public static final String KEY_MONTH = "month";
    public static final String KEY_YEAR = "year";

    private static final String DATABASE_NAME = "SavedCardsDataBase";
    private static final String DATABASE_TABLE = "CardsTable";
    private static final int DATABASE_VERSION = 1;

    private DBHelper dbHelper;
    private final Context context;
    private SQLiteDatabase sqLiteDatabase;

    public SavedCardsDB(Context c) {
        context = c;
    }

    public long createEntry(String cardNo, String name, String cvv, String cardLabel, String month, String year) throws SQLException {
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_CARD_NUMBER, cardNo);
        contentValues.put(KEY_NAME, name);
        contentValues.put(KEY_CVV, cvv);
        contentValues.put(KEY_CARD_LABEL, cardLabel);
        contentValues.put(KEY_MONTH, month);
        contentValues.put(KEY_YEAR, year);
        return sqLiteDatabase.insert(DATABASE_TABLE, null, contentValues);
    }

    public String getData() throws SQLException{
        String[] columns = new String[] {KEY_ROW_ID, KEY_CARD_NUMBER, KEY_NAME, KEY_CVV, KEY_CARD_LABEL, KEY_MONTH, KEY_YEAR};
        Cursor cursor = sqLiteDatabase.query(DATABASE_TABLE, columns, null, null, null, null, null);
        int iRow = cursor.getColumnIndex(KEY_ROW_ID);
        int iCardNumber = cursor.getColumnIndex(KEY_CARD_NUMBER);
        int iName = cursor.getColumnIndex(KEY_NAME);
        int iCvv = cursor.getColumnIndex(KEY_CVV);
        int iCardLabel = cursor.getColumnIndex(KEY_CARD_LABEL);
        int iMonth = cursor.getColumnIndex(KEY_MONTH);
        int iYear = cursor.getColumnIndex(KEY_YEAR);
        String result = "";
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            result += cursor.getString(iRow) + '\t' + cursor.getString(iCardNumber)
                    + '\t' + cursor.getString(iName) + '\t' + cursor.getString(iCvv) + '\t' + cursor.getString(iCardLabel)
                    + '\t' + cursor.getString(iMonth) + '\t' + cursor.getString(iYear)+ '\n';
        }
        return result;
    }

    public void updateEntry(long lRowIDToBeModified, String modifiedName, String modifiedHotness) throws SQLException{
        ContentValues updateContentValues = new ContentValues();
        updateContentValues.put(KEY_NAME, modifiedName);
        //updateContentValues.put(KEY_HOTNESS, modifiedHotness);
        sqLiteDatabase.update(DATABASE_TABLE, updateContentValues, KEY_ROW_ID + "=" + lRowIDToBeModified, null);
    }

    public void deleteEntry(long lRowIDToDelete) throws SQLException{
        sqLiteDatabase.delete(DATABASE_TABLE, KEY_ROW_ID + "=" + lRowIDToDelete, null);
    }


    private static class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + DATABASE_TABLE + " (" + KEY_ROW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + KEY_CARD_NUMBER + " TEXT NOT NULL, " + KEY_NAME + " TEXT NOT NULL, " + KEY_CVV + " TEXT NOT NULL, "
                    + KEY_CARD_LABEL + " TEXT NOT NULL, " + KEY_MONTH + " TEXT NOT NULL, " + KEY_YEAR + " TEXT NOT NULL);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
            onCreate(db);
        }
    }

    public SavedCardsDB open() throws SQLException {
        dbHelper = new DBHelper(context);
        sqLiteDatabase = dbHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        dbHelper.close();
    }
}
