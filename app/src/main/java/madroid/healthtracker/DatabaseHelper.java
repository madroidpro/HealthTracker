package madroid.healthtracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by madroid on 31-07-2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    public static final int DATABASE_VERSION=1;

    // Database name
    public static final String DATABASE_NAME="health_tracker";

    // Database Tables Names
    public static final String DAILY_WEIGHT="daily_weight";

    //Column Names
    public static final String KEY_ID = "id"; // 0
    public static final String KEY_DATE = "date"; // 1
    public static final String KEY_WEIGHT = "weight"; // 2


    //Create Tables
    String CREATE_WEIGHT_TABLE ="CREATE TABLE "+DAILY_WEIGHT+ " ("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , "+
            KEY_DATE + " VARCHAR,"+
            KEY_WEIGHT + " VARCHAR);";

   //Creating DATABASE
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTables(db);
    }

    private void createTables(SQLiteDatabase db){
        db.execSQL(CREATE_WEIGHT_TABLE);
        Log.d("Tables","Successfully Created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + DAILY_WEIGHT);
        onCreate(db);
    }

    public Boolean insertTableData(String table_name, ContentValues table_data){
        SQLiteDatabase sd = this.getWritableDatabase();
        Log.d("sqldata_insertion","success");
        try{
            sd.insertOrThrow(table_name,null,table_data);
            return true;
        }catch (Exception e){
            Log.d("sqldata_exp",e+"");
            return false;
        }
    }

    //Retrieve data from table
    public Cursor getTableData(String Table_name) {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db == null) {
            return null;
        }
        return db.rawQuery("select * from "+Table_name+" ORDER BY date ASC" , null);
    }
    public Cursor getTableData(String Table_name, String pColumn, String key) {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db == null) {
            return null;
        }
        return db.rawQuery("select * from "+Table_name+" where "+pColumn+" = "+key , null);
    }

    public boolean  updateAllTableData(String TableName, String cCoulmn, String value){
        SQLiteDatabase sd=this.getWritableDatabase();
       try{
           String query="UPDATE "+TableName+" SET "+cCoulmn+" = "+value;
           sd.execSQL(query);
           return true;
       }catch (Exception e){
           return false;
       }


    }


    //Delete table data
    public void clearTableData(String table_name){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + table_name);
    }

    public void removeTableData(String table_name, String column_name, String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "delete from "+table_name+" where "+ column_name +" = " + id;
        db.execSQL(query);
        // db.close();
//        refreshTable();
    }

    // Returns the record count
    public int getRecordCount(String table_name){
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            if (db == null) {
                return 0;
            }else{
                return (int)(DatabaseUtils.queryNumEntries(db, table_name));
            }

        }catch (Exception e){
            return 0;
        }

    }

    //Returns Conditioned record count
    public int getRecordCount(String Table_name, String cCoulmn, String key){
        SQLiteDatabase db = this.getReadableDatabase();
        if (db == null) {
            return 0;
        }
       // DatabaseUtils.queryNumEntries()
            Cursor cursor=db.rawQuery("select * from "+Table_name+" where "+cCoulmn+" = "+key , null);
                cursor.moveToNext();
        return cursor.getCount();
    }


    // closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }
}
