package com.jcstudio.com.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DBHelper extends SQLiteOpenHelper {


    String path;
    //Course Schema Design
    public static final String TABLE_NAME_CUM = "TABLE_NOTA";
    public static final String UID_MATERIA = "_id";
    public static final String COLUMN_CUM_MATERIA_NOMBRE = "MATERIA_NOMBRE";
    public static final String COLUMN_CUM_MATERIA_UV = "MATERIA_UV";
    public static final String COLUMN_CUM_MATERIA_NOTA = "MATERIA_NOTA";
    public static final String COLUMN_CUM_CICLO_ID = "CICLO_ID";
    private static final String TABLE_CREATE_CUM = "CREATE TABLE "+TABLE_NAME_CUM+" ("
            +UID_MATERIA +" INTEGER PRIMARY KEY AUTOINCREMENT, "
            +COLUMN_CUM_MATERIA_NOMBRE+" VARCHAR(255), "
            +COLUMN_CUM_MATERIA_UV+" REAL NOT NULL, "
            +COLUMN_CUM_MATERIA_NOTA+" REAL NOT NULL, "
            +COLUMN_CUM_CICLO_ID+" INTEGER NOT NULL "
            +");";
    private static final String DROP_TABLE_CUM = "DROP TABLE IF EXISTS "+TABLE_NAME_CUM;

    //Semester Schema Design
    public static final String TABLE_NAME_CICLO = "TABLE_CICLO";
    public static final String UID_CICLO = "_id";
    public static final String COLUMN_CICLO_NOMBRE = "CICLO_NOMBRE";
    public static final String COLUMN_CICLO_NOTA = "CICLO_NOTA";
    public static final String COLUMN_CICLO_TOTAL_MATERIA = "CICLO_TOTAL_MATERIA";
    public static final String COLUMN_CICLO_TOTAL_UV = "CICLO_TOTAL_UV";
    private static final String TABLE_CREATE_CICLO = "CREATE TABLE "+TABLE_NAME_CICLO+" ("
            +UID_CICLO+" INTEGER PRIMARY KEY AUTOINCREMENT, "
            +COLUMN_CICLO_NOMBRE+" VARCHAR(255), "
            +COLUMN_CICLO_NOTA+" REAL NOT NULL, "
            +COLUMN_CICLO_TOTAL_MATERIA+" INTEGER NOT NULL, "
            +COLUMN_CICLO_TOTAL_UV+" REAL NOT NULL "
            +");";
    private static final String DROP_TABLE_CICLO = "DROP TABLE IF EXISTS "+TABLE_NAME_CICLO;


    private static final String DATABASE_NAME = "myCgpaCalculation.db";
    private static final int DATA_BASE_VERSION = 1;


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATA_BASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        try {
            sqLiteDatabase.execSQL(TABLE_CREATE_CICLO);
            sqLiteDatabase.execSQL(TABLE_CREATE_CUM);
            Log.d("Anik", "Database Created");
        } catch (SQLException e) {
            Log.d("Anik", ""+e);
            e.printStackTrace();
        }
        path = sqLiteDatabase.getPath();
        Log.d("Anik", path);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DROP_TABLE_CICLO);
        sqLiteDatabase.execSQL(DROP_TABLE_CUM);

        sqLiteDatabase.execSQL(TABLE_CREATE_CICLO);
        sqLiteDatabase.execSQL(TABLE_CREATE_CUM);
    }

}
