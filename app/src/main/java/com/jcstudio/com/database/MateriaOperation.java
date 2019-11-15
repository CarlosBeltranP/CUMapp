package com.jcstudio.com.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.jcstudio.com.model.Materia;

import java.util.ArrayList;
import java.util.List;

public class MateriaOperation {

    private DBHelper dbHelper;
    private SQLiteDatabase sqLiteDatabase;
    private String[] mAllColumns = {DBHelper.UID_MATERIA,
            DBHelper.COLUMN_CUM_MATERIA_NOMBRE,
            DBHelper.COLUMN_CUM_MATERIA_UV,
            DBHelper.COLUMN_CUM_MATERIA_NOTA,
            DBHelper.COLUMN_CUM_CICLO_ID };
    public MateriaOperation(Context context) {
        dbHelper = new DBHelper(context);
        try {
            open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void open() throws SQLException {
        sqLiteDatabase = dbHelper.getWritableDatabase();
    }
    /*public void close() {
        dbHelper.close();
        Log.d("Anik", "cgpa db close");
    }*/
    public Materia createMateria(String materia_nombre, double materia_uv, double nota, Long cicloId) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_CUM_MATERIA_NOMBRE, materia_nombre);
        values.put(DBHelper.COLUMN_CUM_MATERIA_UV, materia_uv);
        values.put(DBHelper.COLUMN_CUM_MATERIA_NOTA, nota);
        values.put(DBHelper.COLUMN_CUM_CICLO_ID, cicloId);
        long insertId = sqLiteDatabase.insert(DBHelper.TABLE_NAME_CUM,null,values);
        Cursor cursor = sqLiteDatabase.query(DBHelper.TABLE_NAME_CUM, mAllColumns,
                DBHelper.UID_MATERIA + " = " + insertId, null, null,
                null, null);
        if(cursor == null){
            Log.d("Anik", "null "+cursor);
        }
        else{
            Log.d("Anik", "not null");
        }
        cursor.moveToFirst();
        Materia newMateria = cursorToMateria(cursor);
        cursor.close();
        return newMateria;
    }
    public List<Materia> getMateriasPorCicloId(Long cicloId){
        List<Materia> materiasPorCicloId = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.query(DBHelper.TABLE_NAME_CUM, mAllColumns,
                DBHelper.COLUMN_CUM_CICLO_ID + " = ?",
                new String[] { String.valueOf(cicloId) }, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Materia materias = cursorToMateria(cursor);
                materiasPorCicloId.add(materias);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return materiasPorCicloId;
    }
    public List<Materia> getAllMaterias(){
        List<Materia> allMateria = new ArrayList<>();
        Cursor cursor =
                sqLiteDatabase.query(DBHelper.TABLE_NAME_CUM,mAllColumns,null,null,null,null,null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Materia materias = cursorToMateria(cursor);
                allMateria.add(materias);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return allMateria;
    }
    public void deleteMateria(Materia course) {
        long id = course.getmId();
        sqLiteDatabase.delete(DBHelper.TABLE_NAME_CUM, DBHelper.UID_MATERIA
                + " = " + id, null);
    }

    public void updateMateria(Materia course, String mMateriaNombre, double mUv, double mGpa){
        long id = course.getmId();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.COLUMN_CUM_MATERIA_NOMBRE,mMateriaNombre);
        contentValues.put(DBHelper.COLUMN_CUM_MATERIA_UV,mUv);
        contentValues.put(DBHelper.COLUMN_CUM_MATERIA_NOTA,mGpa);
        String[] whereArgs = {String.valueOf(id)};
        sqLiteDatabase.update(DBHelper.TABLE_NAME_CUM,contentValues, DBHelper.UID_MATERIA+" =? ",whereArgs);
    }
    private Materia cursorToMateria(Cursor cursor) {
        Materia course = new Materia();
        course.setmId(cursor.getLong(0));
        course.setMateria_nombre(cursor.getString(1));
        course.setMateria_uv(cursor.getDouble(2));
        course.setNota_obtenida(cursor.getDouble(3));
        course.setCiclo_id(cursor.getInt(4));
        return course;
    }
}
