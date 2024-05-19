package com.example.gmca_admin.faculty;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.gmca_admin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AddTeacher extends AppCompatActivity {
    private ImageView addTeacherImage;
    private EditText addTeacherName,addTeacherEmail,addTeacherPost;

    private Button addTeacherBtn;
    private final  int REQ = 1;

    private String category;

    private Bitmap bitmap = null;
    ProgressDialog pd;
    private DatabaseReference reference,dbRef;
    private StorageReference storageReference;

    private String name,email,post,downloadUrl ="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_teacher);

        addTeacherImage = findViewById(R.id.addTeacherImage);
        addTeacherName = findViewById(R.id.addTeacherName);
        addTeacherEmail = findViewById(R.id.addTeacherEmail);
        addTeacherPost = findViewById(R.id.addTeacherPost);

        addTeacherBtn=findViewById(R.id.addTeacherBtn);
        reference = FirebaseDatabase.getInstance().getReference().child("teacher");
        storageReference = FirebaseStorage.getInstance().getReference();

        pd = new ProgressDialog(this);
         addTeacherBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 checkValidation();
             }

             private void checkValidation() {
                 name = addTeacherName.getText().toString();
                 email= addTeacherEmail.getText().toString();
                 post = addTeacherPost.getText().toString();

                 if(name.isEmpty()){
                     addTeacherName.requestFocus();
                 } else if (email.isEmpty()) {
                     addTeacherEmail.requestFocus();
                 }
                 else if(post.isEmpty()){
                     addTeacherPost.requestFocus();
                 } else if (bitmap==null) {
                     pd.setMessage("Uploading...");
                     pd.show();
                     insertData();
                 }else{
                     pd.setMessage("Uploading...");
                     pd.show();
                     uploadImage();
                 }
             }

             private void uploadImage() {

                 ByteArrayOutputStream baos = new ByteArrayOutputStream();
                 bitmap.compress(Bitmap.CompressFormat.JPEG,50,baos);
                 byte[] finalimg = baos.toByteArray();
                 final StorageReference filePath;
                 filePath= storageReference.child("Teachers").child(finalimg+"jpg");
                 final UploadTask uploadTask = filePath.putBytes(finalimg);
                 uploadTask.addOnCompleteListener(AddTeacher.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
                     @Override
                     public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                         if(task.isSuccessful()){
                             uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                 @Override
                                 public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                     filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                         @Override
                                         public void onSuccess(Uri uri) {
                                             downloadUrl = String.valueOf(uri);
                                             insertData();
                                         }
                                     });
                                 }
                             });
                         }else{
                             pd.dismiss();
                             Toast.makeText(AddTeacher.this,"Something went wrong",Toast.LENGTH_LONG).show();
                         }
                     }
                 });

             }

             private void insertData() {

                 dbRef= reference.child("category");
                     final String uniqueKey = dbRef.push().getKey();




                     TeacherData teacherData = new TeacherData(name,email,post,downloadUrl,uniqueKey);

                     dbRef.child(uniqueKey).setValue(teacherData).addOnSuccessListener(new OnSuccessListener<Void>() {
                         @Override
                         public void onSuccess(Void unused) {
                             pd.dismiss();
                             Toast.makeText(AddTeacher.this,"Teacher Added",Toast.LENGTH_LONG).show();

                         }
                     }).addOnFailureListener(new OnFailureListener() {
                         @Override
                         public void onFailure(@NonNull Exception e) {
                             pd.dismiss();
                             Toast.makeText(AddTeacher.this,"Something went wrong",Toast.LENGTH_LONG).show();
                         }
                     });
                 }

         });

        addTeacherImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }

            private void openGallery() {
                Intent pickimage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickimage,REQ);
            }



        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQ && resultCode ==RESULT_OK){
            Uri uri=data.getData();
            try {
                bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            addTeacherImage.setImageBitmap(bitmap);
        }


    }
}
