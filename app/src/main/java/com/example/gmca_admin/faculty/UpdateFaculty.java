package com.example.gmca_admin.faculty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.gmca_admin.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UpdateFaculty extends AppCompatActivity {
    private FloatingActionButton fab;
    private RecyclerView department;
    private List<TeacherData> list;

    private TeacherAdapter adapter;

    private DatabaseReference reference,dbRef;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_faculty);

        fab = findViewById(R.id.fab);
        department = findViewById(R.id.department);


        reference = FirebaseDatabase.getInstance().getReference().child("teacher");

        teacherDepartment();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UpdateFaculty.this, AddTeacher.class));
            }
        });
    }

    private void teacherDepartment() {
        department.setAdapter(adapter);
        dbRef = reference.child("category");
        dbRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list = new ArrayList<>();
                if (!snapshot.exists()) {
                    department.setVisibility(View.GONE);
                    Toast.makeText(UpdateFaculty.this, "Teachers are not Available", Toast.LENGTH_LONG).show();
                } else {
                    department.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        TeacherData data = snapshot1.getValue(TeacherData.class);
                        list.add(data);
                    }
                    department.setHasFixedSize(true);
                    department.setLayoutManager(new LinearLayoutManager(UpdateFaculty.this));
                    adapter = new TeacherAdapter(list, UpdateFaculty.this);
                    department.setAdapter(adapter);



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateFaculty.this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
}