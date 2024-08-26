package com.example.adminghorbagan;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.adminghorbagan.Data.ItemsModel;
import com.example.adminghorbagan.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding actMain;
    String title = "",intro= "",description= "",process= "",category = "",tag = "";
    Intent intent;
    Uri imageUri;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    ProgressDialog progressDialog;
    String[] categoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actMain = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(actMain.getRoot());

        categoryName = getResources().getStringArray(R.array.category_items);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,categoryName);
        actMain.spinnerCatergory.setAdapter(spinnerAdapter);

        actMain.itemImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        actMain.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog(MainActivity.this).setTitle("Setting Data!");
                progressDialog(MainActivity.this).show();

                if (getInitData()) return;
                uploadImage();

                Log.d("initCheck",getInitData()+"");
            }
        });

    }

    private boolean getInitData() {

        title = actMain.etItemName.getText().toString();
        intro = actMain.etItemIntro.getText().toString();
        description = actMain.etItemDescription.getText().toString();
        process = actMain.etItemProcess.getText().toString();
        category = actMain.spinnerCatergory.getSelectedItem().toString();
        tag = actMain.etItemTag.getTag().toString();

        if(title.isEmpty() || intro.isEmpty() || description.isEmpty() || process.isEmpty() || category.isEmpty() || tag.isEmpty()){
            Toast.makeText(this, "Add all data properly", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;

    }


    void selectImage() {
        intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,100);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && data != null && data.getData() != null) {
            imageUri = data.getData();
            actMain.itemImages.setImageURI(imageUri);
        }
    }

    void uploadImage() {

        SimpleDateFormat formater = new SimpleDateFormat("dd-MM-yyyy_HH:mm:ss", Locale.ENGLISH);
        Date now = new Date();
        String file_name = formater.format(now);

        storageReference = FirebaseStorage.getInstance().getReference("itemImages/"+file_name);
        storageReference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String imageUrl = uri.toString();
//                                Log.d("imageUrlCheck",imageUrl+"");
                                Toast.makeText(MainActivity.this, imageUrl+"", Toast.LENGTH_SHORT).show();
                                addData(imageUrl);
//                                Toast.makeText(MainActivity.this, "Image Uploaded!", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                if (progressDialog.isShowing()) progressDialog.dismiss();
                                Toast.makeText(MainActivity.this, "Image Upload Failed!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    private ProgressDialog progressDialog(Context context) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(context);
        }
        return progressDialog;
    }


    private void addData(String imageUrl) {
        databaseReference = FirebaseDatabase.getInstance().getReference("ItemData");
        String id = databaseReference.push().getKey();
        ItemsModel itemsModel = new ItemsModel(title,intro,description,process,imageUrl,category,tag);
        databaseReference.child(id)
                .setValue(itemsModel)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (progressDialog.isShowing()) progressDialog.dismiss();
                        Toast.makeText(MainActivity.this, "Data is set!", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (progressDialog.isShowing()) progressDialog.dismiss();
                        Toast.makeText(MainActivity.this, "Data setting failed!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}