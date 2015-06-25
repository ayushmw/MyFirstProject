package com.example.ayush.customerapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ayush on 02-06-2015.
 */
public class SavedCardsDB {
    private static final String KEY_ROW_ID = "_id";
    private static final String KEY_CARD_NUMBER = "cardNumber";
    private static final String KEY_NAME = "name";
    private static final String KEY_CVV = "cvv";
    private static final String KEY_CARD_LABEL = "cardLabel";
    private static final String KEY_MONTH = "month";
    private static final String KEY_YEAR = "year";
    private static final String KEY_DEFAULT_CARD = "defaultCard";
    private static final String KEY_BACKGROUND_COLOR = "backgroundColor";
    private static final String KEY_ACTIVE_EMAIL = "activeEmail";

    private static final String KEY_ROW_ID_GP = "gp_id";
    private static final String KEY_EMAIL_ADDRESS = "emailAddress";
    private static final String KEY_GPLUS_NAME = "gPlusName";
    private static final String KEY_PROFILE_PIC = "profilePic";
    private static final String KEY_MOBILE_NUMBER = "mobileNumber";
    private static final String KEY_ACTIVE = "active";

    private static final String DATABASE_NAME = "SavedCardsDataBase";
    private static final String DATABASE_TABLE = "CardsTable";
    private static final String GPLUS_TABLE = "GPlusTable";
    private static final int DATABASE_VERSION = 7;

    private DBHelper dbHelper;
    private final Context context;
    private SQLiteDatabase sqLiteDatabase;

    public SavedCardsDB(Context c) {
        context = c;
    }

    public long createEntry(String cardNo, String name, String cvv, String cardLabel, String month, String year, String defaultCard, String bgColor, String activeEmail) throws SQLException {
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_CARD_NUMBER, cardNo);
        contentValues.put(KEY_NAME, name);
        contentValues.put(KEY_CVV, cvv);
        contentValues.put(KEY_CARD_LABEL, cardLabel);
        contentValues.put(KEY_MONTH, month);
        contentValues.put(KEY_YEAR, year);
        contentValues.put(KEY_DEFAULT_CARD, defaultCard);
        contentValues.put(KEY_BACKGROUND_COLOR, bgColor);
        contentValues.put(KEY_ACTIVE_EMAIL, activeEmail);
        return sqLiteDatabase.insert(DATABASE_TABLE, KEY_BACKGROUND_COLOR, contentValues);
    }

    public long createEntryGP(String emailAddress, String gPlusName, int profilePic, String mobileNumber, String active) throws SQLException {
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_EMAIL_ADDRESS, emailAddress);
        contentValues.put(KEY_GPLUS_NAME, gPlusName);
        contentValues.put(KEY_PROFILE_PIC, profilePic);
        contentValues.put(KEY_MOBILE_NUMBER, mobileNumber);
        contentValues.put(KEY_ACTIVE, active);
        return sqLiteDatabase.insert(GPLUS_TABLE, KEY_PROFILE_PIC, contentValues);
    }

    public String getData() throws SQLException {
        String[] columns = new String[]{KEY_ROW_ID, KEY_CARD_NUMBER, KEY_NAME, KEY_CVV, KEY_CARD_LABEL, KEY_MONTH, KEY_YEAR, KEY_DEFAULT_CARD, KEY_BACKGROUND_COLOR, KEY_ACTIVE_EMAIL};
        Cursor cursor = sqLiteDatabase.query(DATABASE_TABLE, columns, null, null, null, null, null);
        int iRow = cursor.getColumnIndex(KEY_ROW_ID);
        int iCardNumber = cursor.getColumnIndex(KEY_CARD_NUMBER);
        int iName = cursor.getColumnIndex(KEY_NAME);
        int iCvv = cursor.getColumnIndex(KEY_CVV);
        int iCardLabel = cursor.getColumnIndex(KEY_CARD_LABEL);
        int iMonth = cursor.getColumnIndex(KEY_MONTH);
        int iYear = cursor.getColumnIndex(KEY_YEAR);
        int iDefaultCard = cursor.getColumnIndex(KEY_DEFAULT_CARD);
        int iBackgroundColor = cursor.getColumnIndex(KEY_BACKGROUND_COLOR);
        int iActiveEmail = cursor.getColumnIndex(KEY_ACTIVE_EMAIL);
        String result = "";
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            result += cursor.getString(iRow) + '\t' + cursor.getString(iCardNumber)
                    + '\t' + cursor.getString(iName) + '\t' + cursor.getString(iCvv) + '\t' + cursor.getString(iCardLabel)
                    + '\t' + cursor.getString(iMonth) + '\t' + cursor.getString(iYear) + '\t' + cursor.getString(iDefaultCard)
                    + '\t' + cursor.getString(iBackgroundColor) + '\t' + cursor.getString(iActiveEmail) +  '\n';
        }
        return result;
    }

    public List<String> getGPName() throws SQLException {
        String[] columns = new String[]{KEY_GPLUS_NAME};
        Cursor cursor = sqLiteDatabase.query(GPLUS_TABLE, columns, null, null, null, null, null);
        List<String> result = new ArrayList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            result.add(cursor.getString(cursor.getColumnIndex(KEY_GPLUS_NAME)));
        }
        return result;
    }

    public List<String> getGPEmail() throws SQLException {
        String[] columns = new String[]{KEY_EMAIL_ADDRESS};
        Cursor cursor = sqLiteDatabase.query(GPLUS_TABLE, columns, null, null, null, null, null);
        List<String> result = new ArrayList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            result.add(cursor.getString(cursor.getColumnIndex(KEY_EMAIL_ADDRESS)));
        }
        return result;
    }

    public String getDataGP() throws SQLException {
        String[] columns = new String[]{KEY_ROW_ID_GP, KEY_EMAIL_ADDRESS, KEY_GPLUS_NAME, KEY_PROFILE_PIC, KEY_ACTIVE};
        Cursor cursor = sqLiteDatabase.query(GPLUS_TABLE, columns, null, null, null, null, null);
        int iRow = cursor.getColumnIndex(KEY_ROW_ID_GP);
        int iEmailAddress = cursor.getColumnIndex(KEY_EMAIL_ADDRESS);
        int iGPlusName = cursor.getColumnIndex(KEY_GPLUS_NAME);
        int iProfilePic = cursor.getColumnIndex(KEY_PROFILE_PIC);
        int iMobileNumber = cursor.getColumnIndex(KEY_MOBILE_NUMBER);
        int iActive = cursor.getColumnIndex(KEY_ACTIVE);
        String result = "";
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            result += cursor.getInt(iRow) + "\t" + cursor.getString(iEmailAddress) + "\t" + cursor.getString(iGPlusName)
                    + "\t" + cursor.getInt(iProfilePic) + "\t" + cursor.getString(iMobileNumber) + "\t" + cursor.getString(iActive) +  "\n";
        }
        return result;
    }

    public String getRowOfDefaultCard() {
        String[] columns = new String[]{KEY_ROW_ID};
        Cursor cursor = sqLiteDatabase.query(DATABASE_TABLE, columns, KEY_DEFAULT_CARD + " =? AND " + KEY_ACTIVE_EMAIL + " =?", new String[]{"true", getActiveEmail()}, null, null, null, null);
        String result = "";
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            result += cursor.getString(cursor.getColumnIndex(KEY_ROW_ID));
        }
        return result;
    }

    public String getRowOfEmail(String email) {
        String[] columns = new String[]{KEY_ROW_ID_GP};
        Cursor cursor = sqLiteDatabase.query(GPLUS_TABLE, columns, KEY_EMAIL_ADDRESS + "=?", new String[]{email}, null, null, null, null);
        String result = "";
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            result += cursor.getString(cursor.getColumnIndex(KEY_ROW_ID_GP));
        }
        return result;
    }

    public String getActiveEmail() {
        String[] columns = new String[]{KEY_EMAIL_ADDRESS};
        Cursor cursor = sqLiteDatabase.query(GPLUS_TABLE, columns, KEY_ACTIVE + "=?", new String[]{"true"}, null, null, null, null);
        String result = "";
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            result += cursor.getString(cursor.getColumnIndex(KEY_EMAIL_ADDRESS));
        }
        return result;
    }

    public List<String> getRowIds() {
        String[] columns = new String[]{KEY_ROW_ID};
        Cursor cursor = sqLiteDatabase.query(DATABASE_TABLE, columns, KEY_ACTIVE_EMAIL + "=?", new String[]{getActiveEmail()}, null, null, null, null);
        List<String> result = new ArrayList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            result.add(cursor.getString(cursor.getColumnIndex(KEY_ROW_ID)));
        }
        return result;
    }

    public List<String> getCardNumbers() {
        String[] columns = new String[]{KEY_CARD_NUMBER};
        Cursor cursor = sqLiteDatabase.query(DATABASE_TABLE, columns, KEY_ACTIVE_EMAIL + "=?", new String[]{getActiveEmail()}, null, null, null, null);
        List<String> result = new ArrayList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            result.add(cursor.getString(cursor.getColumnIndex(KEY_CARD_NUMBER)));
        }
        return result;
    }

    public List<String> getActive() {
        String[] columns = new String[]{KEY_ACTIVE};
        Cursor cursor = sqLiteDatabase.query(GPLUS_TABLE, columns, null, null, null, null, null);
        List<String> result = new ArrayList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            result.add(cursor.getString(cursor.getColumnIndex(KEY_ACTIVE)));
        }
        return result;
    }

    public void setDefaultCard(String rowIDToBeModified, boolean isChecked) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_DEFAULT_CARD, "false");
        sqLiteDatabase.update(DATABASE_TABLE, cv, KEY_DEFAULT_CARD + " =? AND " + KEY_ACTIVE_EMAIL + " =?", new String[]{"true", getActiveEmail()});
        if (isChecked) {
            ContentValues cv2 = new ContentValues();
            cv2.put(KEY_DEFAULT_CARD, "true");
            sqLiteDatabase.update(DATABASE_TABLE, cv2, KEY_ROW_ID + "=" + rowIDToBeModified, null);
        }
    }

    public void setAccountActive(String rowIDToBeModified, boolean isChecked) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_ACTIVE, "false");
        sqLiteDatabase.update(GPLUS_TABLE, cv, KEY_ACTIVE + "=?", new String[]{"true"});
        if (isChecked) {
            ContentValues cv2 = new ContentValues();
            cv2.put(KEY_ACTIVE, "true");
            sqLiteDatabase.update(GPLUS_TABLE, cv2, KEY_ROW_ID_GP + "=" + rowIDToBeModified, null);
        }
    }

    public void logout() {
        ContentValues cv = new ContentValues();
        cv.put(KEY_ACTIVE, "false");
        sqLiteDatabase.update(GPLUS_TABLE, cv, KEY_ACTIVE + "=?", new String[]{"true"});
    }

    public void setCardBackgroundColor(String rowIDToBeModified, String backgroundColor) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_BACKGROUND_COLOR, backgroundColor);
        sqLiteDatabase.update(DATABASE_TABLE, cv, KEY_ROW_ID + "=" + rowIDToBeModified, null);
    }

    public void deleteEntry(String rowIDToDelete) throws SQLException {
        sqLiteDatabase.delete(DATABASE_TABLE, KEY_ROW_ID + "=?", new String[]{rowIDToDelete});
    }

    private static class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + DATABASE_TABLE + " (" + KEY_ROW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + KEY_CARD_NUMBER + " TEXT NOT NULL, " + KEY_NAME + " TEXT NOT NULL, " + KEY_CVV + " TEXT NOT NULL, "
                    + KEY_CARD_LABEL + " TEXT NOT NULL, " + KEY_MONTH + " TEXT NOT NULL, " + KEY_YEAR + " TEXT NOT NULL, "
                    + KEY_DEFAULT_CARD + " TEXT NOT NULL, " + KEY_BACKGROUND_COLOR + " VARCHAR(255), " + KEY_ACTIVE_EMAIL + " TEXT NOT NULL);");

            db.execSQL("CREATE TABLE " + GPLUS_TABLE + " (" + KEY_ROW_ID_GP + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + KEY_EMAIL_ADDRESS + " VARCHAR(255), " + KEY_GPLUS_NAME + " VARCHAR(255), " + KEY_PROFILE_PIC + " INTEGER, " + KEY_MOBILE_NUMBER + " TEXT NOT NULL, " + KEY_ACTIVE + " TEXT NOT NULL);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + GPLUS_TABLE);
            onCreate(db);
        }
    }

    public SavedCardsDB open() throws SQLException {
        dbHelper = new DBHelper(context);
        sqLiteDatabase = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }
}
