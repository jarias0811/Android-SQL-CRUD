package com.johnarias.android.crudtest;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.johnarias.android.crudtest.domain.TableControllerStudent;
import com.johnarias.android.crudtest.model.Student;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonCreateStudent = (Button) findViewById(R.id.buttonCreateStudent);
        buttonCreateStudent.setOnClickListener(new OnClickListenerCreateStudent());
        //ButterKnife.bind(this);//Inyeccion de vistas mediante anotaciones
        countRecords();
        readRecords();
    }

    //Metodo que llama al metodo que cuenta la cantidad de estudiantes del dal
    public void countRecords() {
        int recordCount = new TableControllerStudent(this).count();

        TextView textViewRecordCount = (TextView) findViewById(R.id.textViewRecordCount);
        textViewRecordCount.setText(recordCount + " records found.");
    }

    //Metodo que invoca al metodo de listar estudiantes en el dal
    public void readRecords() {
        LinearLayout linearLayoutRecords = (LinearLayout) findViewById(R.id.linearLayoutRecords);
        linearLayoutRecords.removeAllViews();

        List<Student> students = new TableControllerStudent(this).read();

        if (students.size() > 0) {
            for (Student student : students) {

                int id = student.getId();
                String studentFirstname = student.getFirstname();
                String studentEmail = student.getEmail();

                String textViewContents = studentFirstname + " - " + studentEmail;

                TextView textViewStudentItem= new TextView(this);
                textViewStudentItem.setPadding(0, 10, 0, 10);
                textViewStudentItem.setText(textViewContents);
                textViewStudentItem.setTag(Integer.toString(id));

                textViewStudentItem.setOnLongClickListener(new OnLongClickListenerStudentRecord());

                linearLayoutRecords.addView(textViewStudentItem);
            }
        }
        else {
            TextView locationItem = new TextView(this);
            locationItem.setPadding(8, 8, 8, 8);
            locationItem.setText("No records yet.");
            linearLayoutRecords.addView(locationItem);
        }
    }

    //Clase listener para eventos onclick
    private class OnClickListenerCreateStudent implements View.OnClickListener {
        @Override
        //Metodo que se ejecuta al oprimir el boton de crear estudiante
        public void onClick(View view) {
            final Context context = view.getContext();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View formElementsView = inflater.inflate(R.layout.student_input_form, null, false);

            final EditText editTextStudentFirstname = (EditText) formElementsView.findViewById(R.id.editTextStudentFirstname);
            final EditText editTextStudentEmail = (EditText) formElementsView.findViewById(R.id.editTextStudentEmail);

            new AlertDialog.Builder(context)
                    .setView(formElementsView)
                    .setTitle("Create Student")
                    .setPositiveButton("Add",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    String studentFirstname = editTextStudentFirstname.getText().toString();
                                    String studentEmail = editTextStudentEmail.getText().toString();
                                    Student student = new Student(studentFirstname,studentEmail);

                                    boolean createSuccessful = new TableControllerStudent(context).create(student);
                                    if(createSuccessful){
                                        Toast.makeText(context, "Student information was saved.", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(context, "Unable to save student information.", Toast.LENGTH_SHORT).show();
                                    }
                                    countRecords();
                                    readRecords();
                                    dialog.cancel();
                                }
                            }).show();
        }
    }

    private class OnLongClickListenerStudentRecord implements View.OnLongClickListener {
        Context context;
        String id;
        @Override
        public boolean onLongClick(View view) {
            context = view.getContext();
            id = view.getTag().toString();

            final CharSequence[] items = { "Edit", "Delete" };

            new AlertDialog.Builder(context).setTitle("Student Record")
                    .setItems(items, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {
                            if (item == 0) {
                                editRecord(Integer.parseInt(id));
                            }
                            else if (item == 1) {
                                boolean deleteSuccessful = new TableControllerStudent(context).delete(Integer.parseInt(id));

                                if (deleteSuccessful){
                                    Toast.makeText(context, "Student record was deleted.", Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(context, "Unable to delete student record.", Toast.LENGTH_SHORT).show();
                                }
                            }
                            countRecords();
                            readRecords();

                            dialog.dismiss();
                        }
                    }).show();
            return false;
        }

        public void editRecord(final int studentId) {
            final TableControllerStudent tableControllerStudent = new TableControllerStudent(context);
            Student student = tableControllerStudent.readSingleRecord(studentId);

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View formElementsView = inflater.inflate(R.layout.student_input_form, null, false);

            final EditText editTextStudentFirstname = (EditText) formElementsView.findViewById(R.id.editTextStudentFirstname);
            final EditText editTextStudentEmail = (EditText) formElementsView.findViewById(R.id.editTextStudentEmail);

            editTextStudentFirstname.setText(student.getFirstname());
            editTextStudentEmail.setText(student.getEmail());

            new AlertDialog.Builder(context)
                    .setView(formElementsView)
                    .setTitle("Edit Record")
                    .setPositiveButton("Save Changes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Student objectStudent = new Student(
                                            studentId,
                                            editTextStudentFirstname.getText().toString(),
                                            editTextStudentEmail.getText().toString());
                                    boolean updateSuccessful = tableControllerStudent.update(objectStudent);

                                    if(updateSuccessful){
                                        Toast.makeText(context, "Student record was updated.", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(context, "Unable to update student record.", Toast.LENGTH_SHORT).show();
                                    }
                                    countRecords();
                                    readRecords();
                                    dialog.cancel();
                                }
                            }).show();
        }
    }
}

