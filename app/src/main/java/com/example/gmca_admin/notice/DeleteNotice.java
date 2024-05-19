package com.example.gmca_admin.notice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.gmca_admin.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DeleteNotice extends AppCompatActivity {
    private RecyclerView itemRemove;
    private ProgressBar progressBar;
    private ArrayList<NoticeData> list;
    private NoticeAdapter adapter;
    private DatabaseReference reference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_notice);
        itemRemove=findViewById(R.id.itemRemove);
        progressBar=findViewById(R.id.progressBar);
        itemRemove.setLayoutManager(new LinearLayoutManager(this));
        itemRemove.setHasFixedSize(true);
        reference = FirebaseDatabase.getInstance().getReference().child("Notice");

        getNotice();
    }

    private void getNotice() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list = new ArrayList<>();
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    NoticeData data = snapshot1.getValue(NoticeData.class);
                    list.add(data);
                }
                adapter=new NoticeAdapter(DeleteNotice.this,list);
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                itemRemove.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(DeleteNotice.this,error.getMessage(),Toast.LENGTH_LONG).show();

            }
        });
    }
}