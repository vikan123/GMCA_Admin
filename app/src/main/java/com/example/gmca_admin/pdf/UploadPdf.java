package com.example.gmca_admin.pdf;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

import java.io.File;
import java.util.HashMap;

public class UploadPdf extends AppCompatActivity {

    private CardView addPdf;
    private final int REQ = 1;
    private String pdfName;
    private String tittle;

    private EditText pdfTitle;
    private Button uploadPdfBtn;
    private Uri pdfData;
    private TextView pdfTextView;
    private DatabaseReference reference;
    private StorageReference storageReference;

    private ProgressDialog pd;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_pdf);

        addPdf=findViewById(R.id.addPdf);
        reference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();



        pdfTitle = findViewById(R.id.pdfTitle);
        uploadPdfBtn = findViewById(R.id.uploadPdfBtn);
        pdfTextView = findViewById(R.id.pdfTextView);
        pd = new ProgressDialog(this);


        uploadPdfBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tittle =pdfTitle.getText().toString();
                if(tittle.isEmpty()){
                    pdfTitle.setError("Empty");
                    pdfTitle.requestFocus();

                } else if (pdfData==null) {
                    Toast.makeText(UploadPdf.this,"Please upload pdf",Toast.LENGTH_LONG).show();
                }
                else{
                    uploadPdf();
                }
            }

            private void uploadPdf() {
                pd.setTitle("Please wait...");
                pd.setMessage("Uploading pdf");
                pd.show();
                StorageReference reference1 = storageReference.child("pdf/"+pdfName+"-"+System.currentTimeMillis()+".pdf");
                reference1.putFile(pdfData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while(!uriTask.isComplete());
                        Uri uri = uriTask.getResult();
                        uploadData(String.valueOf(uri));

                    }




                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(UploadPdf.this,"Something went wrong",Toast.LENGTH_LONG).show();
                    }
                });

            }

            private void uploadData(String downloadUrl) {

                String uniqueKey = reference.child("pdf").push().getKey();

                HashMap data = new HashMap();
                data.put("pdfTitle",tittle);
                data.put("pdfUrl",downloadUrl);

                reference.child("pdf").child(uniqueKey).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        pd.dismiss();
                        Toast.makeText(UploadPdf.this,"Pdf uploaded successfully",Toast.LENGTH_LONG).show();
                        pdfTitle.setText("");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(UploadPdf.this,"Failed to upload pdf",Toast.LENGTH_LONG).show();
                    }
                });
            }

        });

        addPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

    }
    private void openGallery() {
       Intent intent = new Intent();
       intent.setType("*/*");//pdf/docs/ppt
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Pdf File"),REQ);
    }
    @SuppressLint("Range")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQ && resultCode ==RESULT_OK){
            pdfData = data.getData();

            if(pdfData.toString().startsWith("content://")){
                Cursor cursor =null;
                try {
                    cursor= UploadPdf.this.getContentResolver().query(pdfData,null,null,null,null);
                    if(cursor!=null && cursor.moveToFirst()){
                        pdfName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }else if(pdfData.toString().startsWith("file://")){
                pdfName = new File(pdfData.toString()).getName();
            }
            pdfTextView.setText(pdfName);
        }


    }
}