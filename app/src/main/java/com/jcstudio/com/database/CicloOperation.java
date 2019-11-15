package com.jcstudio.com.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.jcstudio.com.model.Ciclo;
import com.jcstudio.com.model.Materia;

import java.util.ArrayList;
import java.util.List;

public class CicloOperation {

    private Context mContext;
    private DBHelper dbHelper;
    private SQLiteDatabase sqLiteDatabase;
    private String[] mAllColumns = {DBHelper.UID_CICLO,
            DBHelper.COLUMN_CICLO_NOMBRE,
            DBHelper.COLUMN_CICLO_NOTA,
            DBHelper.COLUMN_CICLO_TOTAL_MATERIA,
            DBHelper.COLUMN_CICLO_TOTAL_UV };

    public CicloOperation(Context context) {
        mContext = context;
        dbHelper = new DBHelper(context);
        try {
            open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void open() throws SQLException {
        sqLiteDatabase = dbHelper.getWritableDatabase();
    }
    public void close() {
        dbHelper.close();
        Log.d("Anik", "ciclo db close");
    }
    public Ciclo createCiclo(String ciclo_nombre, double ciclo, int total_materia, double total_uv) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_CICLO_NOMBRE, ciclo_nombre);
        values.put(DBHelper.COLUMN_CICLO_NOTA, ciclo);
        values.put(DBHelper.COLUMN_CICLO_TOTAL_MATERIA, total_materia);
        values.put(DBHelper.COLUMN_CICLO_TOTAL_UV, total_uv);
        long insertId = sqLiteDatabase.insert(DBHelper.TABLE_NAME_CICLO,null,values);
        Log.d("Anik", "invoked"+insertId);
        Cursor cursor = sqLiteDatabase.query(DBHelper.TABLE_NAME_CICLO, mAllColumns,
                DBHelper.UID_CICLO + " = " + insertId, null, null,
                null, null);
        cursor.moveToFirst();
        Ciclo newCiclo = cursorToSmester(cursor);
        cursor.close();
        return newCiclo;
    }

    public List<Ciclo> getAllCiclo(){
        List<Ciclo> allCiclo = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.query(DBHelper.TABLE_NAME_CICLO, mAllColumns,
                null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Ciclo ciclo = cursorToSmester(cursor);
                allCiclo.add(ciclo);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return allCiclo;
    }

    public void deleteCiclo(Ciclo ciclo) {
        long id = ciclo.getmId();
        MateriaOperation ccicloOperation = new MateriaOperation(mContext);
        List<Materia> listMaterias = ccicloOperation.getMateriasPorCicloId(id);
        if (listMaterias != null && !listMaterias.isEmpty()) {
            for (Materia e : listMaterias) {
                ccicloOperation.deleteMateria(e);
            }
        }
        String[] args={String.valueOf(id)};
        sqLiteDatabase.delete(DBHelper.TABLE_NAME_CICLO, DBHelper.UID_CICLO
                + " = ?", args);
    }

    public void updateCiclo(long cicloId, double ciclo, double total_uv, int total_materia){
        long id = cicloId;
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.COLUMN_CICLO_NOTA,ciclo);
        contentValues.put(DBHelper.COLUMN_CICLO_TOTAL_UV,total_uv);
        contentValues.put(DBHelper.COLUMN_CICLO_TOTAL_MATERIA,total_materia);
        String[] whereArgs = {String.valueOf(id)};
        sqLiteDatabase.update(DBHelper.TABLE_NAME_CICLO,contentValues, DBHelper.UID_CICLO+" =? ",whereArgs);
    }

    private Ciclo cursorToSmester(Cursor cursor) {
        Ciclo ciclo = new Ciclo();
        ciclo.setmId(cursor.getLong(0));
        ciclo.setCiclo_nombre(cursor.getString(1));
        ciclo.setTotal_nota(cursor.getDouble(2));
        ciclo.setTotal_materia(cursor.getInt(3));
        ciclo.setTotal_uv(cursor.getDouble(4));
        return ciclo;
    }
}
