package com.example.gmca_admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.gmca_admin.faculty.UpdateFaculty;
import com.example.gmca_admin.image.UploadImage;
import com.example.gmca_admin.notice.DeleteNotice;
import com.example.gmca_admin.notice.UploadNotice;
import com.example.gmca_admin.pdf.UploadPdf;


public class HomeScreen extends AppCompatActivity implements View.OnClickListener {

    CardView uploadNotice, addGalleryImage, addEbook, faculty, deleteNotice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        uploadNotice = findViewById(R.id.addNotice);
        uploadNotice.setOnClickListener(this);
        addEbook = findViewById(R.id.addEbook);
        addGalleryImage = findViewById(R.id.addGalleryImage);
        faculty = findViewById(R.id.faculty);
        deleteNotice = findViewById(R.id.deleteNotice);
        faculty.setOnClickListener(this);
        addGalleryImage.setOnClickListener(this);
        addEbook.setOnClickListener(this);
        deleteNotice.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.addNotice) {
            Intent intent = new Intent(HomeScreen.this, UploadNotice.class);
            startActivity(intent);
        } else if (view.getId() == R.id.addGalleryImage) {
            Intent intent = new Intent(HomeScreen.this, UploadImage.class);
            startActivity(intent);
        } else if (view.getId() == R.id.addEbook) {
            Intent intent = new Intent(HomeScreen.this, UploadPdf.class);
            startActivity(intent);
        } else if (view.getId() == R.id.faculty) {
            Intent intent = new Intent(HomeScreen.this, UpdateFaculty.class);
            startActivity(intent);

        } else if (view.getId() == R.id.deleteNotice) {
            Intent intent = new Intent(HomeScreen.this, DeleteNotice.class);
            startActivity(intent);
        }
    }
}