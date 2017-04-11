package com.johnarias.android.crudtest.domain;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.johnarias.android.crudtest.model.Student;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by John on 09/04/2017.
 */
public class TableControllerStudent extends DatabaseHandler{

    //Constructor
    public TableControllerStudent(Context context) {
        super(context);
    }

    //Metodo para crear estudiante del crud devuelve true en el momento en el que se cree el estudiante
    public boolean create(Student student) {

        ContentValues values = new ContentValues();

        values.put("firstname", student.getFirstname());
        values.put("email", student.getEmail());

        SQLiteDatabase db = this.getWritableDatabase();

        boolean createSuccessful = db.insert("students", null, values) > 0;
        db.close();

        return createSuccessful;
    }

    //Metodo que obtiene el numero de estudiantes
    public int count() {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT * FROM students";
        int recordCount = db.rawQuery(sql, null).getCount();

        db.close();
        return recordCount;
    }

    //Metodo que lista todos los estudiantes
    public List<Student> read() {

        List<Student> recordsList = new ArrayList<Student>();

        String sql = "SELECT * FROM students ORDER BY id DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
                String studentFirstname = cursor.getString(cursor.getColumnIndex("firstname"));
                String studentEmail = cursor.getString(cursor.getColumnIndex("email"));

                Student student = new Student(id, studentFirstname, studentEmail);

                recordsList.add(student);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return recordsList;
    }

    public Student readSingleRecord(int studentId) {

        Student student = null;

        String sql = "SELECT * FROM students WHERE id = " + studentId;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
            String firstname = cursor.getString(cursor.getColumnIndex("firstname"));
            String email = cursor.getString(cursor.getColumnIndex("email"));

            student = new Student(id, firstname, email);
        }
        cursor.close();
        db.close();
        return student;
    }

    public boolean update(Student student) {

        ContentValues values = new ContentValues();

        values.put("firstname", student.getFirstname());
        values.put("email", student.getEmail());

        String where = "id = ?";

        String[] whereArgs = { Integer.toString(student.getId()) };

        SQLiteDatabase db = this.getWritableDatabase();

        boolean updateSuccessful = db.update("students", values, where, whereArgs) > 0;
        db.close();

        return updateSuccessful;
    }

    public boolean delete(int id) {
        boolean deleteSuccessful = false;

        SQLiteDatabase db = this.getWritableDatabase();
        deleteSuccessful = db.delete("students", "id ='" + id + "'", null) > 0;
        db.close();

        return deleteSuccessful;
    }

}
