package com.example.arielscupid;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class setProfile extends AppCompatActivity {

    private CardView mgetuserimage;
    private ImageView mgetuserimageview;
    private static int PICK_IMAGE=123;
    private Uri imagepath;

    private EditText mgetusername;
    private android.widget.Button msaveprofile;
    private FirebaseAuth firebaseAuth;
    private String name,about, gender;

    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    private String ImageUriAcessToken;
    private FirebaseFirestore firebaseFirestore;

    private DatabaseReference mDatabase;

    ProgressBar mprogressbarofsetprofile;


    private EditText mgetabout, mgetGender;

    String male = "Male";
    String female = "Female";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_profile);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        /**
         * storage reference to store the image
         */
        storageReference = firebaseStorage.getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();


        mgetusername = findViewById(R.id.getusername);
        mgetuserimage = findViewById(R.id.getuserimage);
        mgetuserimageview = findViewById(R.id.getuserimageinimageview);
        msaveprofile = findViewById(R.id.saveprofile);
        mprogressbarofsetprofile = findViewById(R.id.progressbarofsetprofile);

        mgetabout= findViewById(R.id.getAbout);
        mgetGender = findViewById(R.id.getGender);

          //pick a picture from photos on your phone
        mgetuserimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE);
            }
        });



        msaveprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name=mgetusername.getText().toString();
                about=mgetabout.getText().toString();
                gender = mgetGender.getText().toString();
                if(name.isEmpty())
                {
                    mgetusername.setError("Name is Empty");
                }
                else if(imagepath==null)
                {
                    Toast.makeText(getApplicationContext(),"Image is Empty", Toast.LENGTH_SHORT).show();
                }
                else if( ! (gender.equals(male) ||  gender.equals(female)))
                {
                    mgetGender.setError("Enter gender Male/Female");
                }
                else if(about.isEmpty())
                {
                    mgetabout.setError("About is Empty");
                }
                else
                {
                    mprogressbarofsetprofile.setVisibility(View.VISIBLE);
                    sendDataForNewUser();
                    mprogressbarofsetprofile.setVisibility(View.INVISIBLE);
                    Intent intent = new Intent(setProfile.this, chatActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private void sendDataForNewUser(){
        sendDataToRealTimeDatabase();
    }

    /**
     * data sending on real time database
     */
    private void sendDataToRealTimeDatabase(){
        about = mgetabout.getText().toString().trim();
        name = mgetusername.getText().toString().trim();
        gender = mgetGender.getText().toString().trim();

        FirebaseDatabase firebaseDatabase= FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference(firebaseAuth.getUid());
        /**
         * create reference to the userprofile java file we create and their public objects
         */
        userprofile muserprofile = new userprofile(name,firebaseAuth.getUid(),about,gender);
        databaseReference.setValue(muserprofile);
        Toast.makeText(getApplicationContext(),"User Profile Added Successfully", Toast.LENGTH_SHORT).show();
        sendImagetoStoarge();

    }

    /**
     * user to see only name and image.
     * send to cloud firestore.
     *
     * image compression technique to make upload faster to cloudfirestore
     */
    private void sendImagetoStoarge() {
        StorageReference imageref= storageReference.child("Images").child(firebaseAuth.getUid()).child("Profile Picture");

        //Image compression
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imagepath);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,25,byteArrayOutputStream);
        byte[] data= byteArrayOutputStream.toByteArray();

        // upload image to storage

        UploadTask uploadTask = imageref.putBytes(data);


        /**
         * upload image to cloudFirestore database function
         */

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                imageref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        ImageUriAcessToken = uri.toString();
                        Toast.makeText(getApplicationContext(),"URI get success", Toast.LENGTH_SHORT).show();
                        sendDataToCloudFirestore();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"URI get Failed", Toast.LENGTH_SHORT).show();
                    }
                });
                Toast.makeText(getApplicationContext(),"Image is Uploaded", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Image not Uploaded", Toast.LENGTH_SHORT).show();
            }
        });

    }





    private void sendDataToCloudFirestore() {
        DocumentReference documentReference = firebaseFirestore.collection("Users").document((firebaseAuth.getUid()));
        Map<String, Object> userdata = new HashMap<>();
        userdata.put("name",name);
        userdata.put("image",ImageUriAcessToken);
        userdata.put("uid",firebaseAuth.getUid());
        userdata.put("status","Online");
        userdata.put("questions",0);
        userdata.put("about",about);



        documentReference.set(userdata).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(),"Data on Cloud Firestore send success", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Data not on Cloud Firestore. didn't send!!", Toast.LENGTH_SHORT).show();
            }
        });
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==PICK_IMAGE && resultCode==RESULT_OK)
        {
            imagepath=data.getData();
            mgetuserimageview.setImageURI(imagepath);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }





}