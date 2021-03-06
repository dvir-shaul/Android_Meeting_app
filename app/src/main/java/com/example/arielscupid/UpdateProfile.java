package com.example.arielscupid;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
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
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UpdateProfile extends AppCompatActivity {


    private EditText mnewusername,mnewabout;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;

    private FirebaseFirestore firebaseFirestore;

    private ImageView mgetnewimageinimageview;
    private StorageReference storageReference;

    private String ImageURIaccestoken;

    private androidx.appcompat.widget.Toolbar mtoolbarofUpdateprofile;
    private ImageButton mbackbuttonofupdateprofile;

    private FirebaseStorage firebaseStorage;

    ProgressBar mprogressBar;

    private Uri imagepath;
    Intent intent;
    android.widget.Button mupdateprofilebutton;

    String gender = "";
    String wantedGender = "";

    private RadioGroup mgetGender, mgetwantedgender;

    private RadioButton rbGender, rbWanted;

    private static int PICK_IMAGE=123;

    String newName, newAbout;

    String male = "Male";
    String female = "Female";
    String other = "Other";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        mgetwantedgender = (RadioGroup) findViewById(R.id.getWantGender);
        mnewabout=findViewById(R.id.getAbout);
        mgetGender = (RadioGroup) findViewById(R.id.getnewGender);
        mtoolbarofUpdateprofile=findViewById(R.id.toolbarOfUpdateProfileActivity);
        mbackbuttonofupdateprofile=findViewById(R.id.backbuttonofUpdateProfile);
        mgetnewimageinimageview=findViewById(R.id.viewuserimageinimageview);
        mprogressBar=findViewById(R.id.progresbarofupdateimage);
        mnewusername=findViewById(R.id.getnewusername);
        mupdateprofilebutton=findViewById(R.id.updateprofilebutton);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        firebaseStorage=FirebaseStorage.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();

        intent=getIntent();
        setSupportActionBar(mtoolbarofUpdateprofile);

        mgetGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                rbGender = mgetGender.findViewById(checkedId);
                switch (checkedId) {
                    case R.id.male:
                        gender = rbGender.getText().toString();
                        break;
                    case R.id.female:
                        gender = rbGender.getText().toString();
                        break;
                    case R.id.other:
                        gender = rbGender.getText().toString();
                }
            }
        });

        mgetwantedgender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                rbWanted = mgetwantedgender.findViewById(checkedId);
                switch (checkedId) {
                    case R.id.male1:
                        wantedGender = rbWanted.getText().toString();
                        break;
                    case R.id.female1:
                        wantedGender = rbWanted.getText().toString();
                        break;
                    case R.id.other1:
                        wantedGender = rbWanted.getText().toString();
                        break;
                }
            }
        });


        mbackbuttonofupdateprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mnewabout.setText(intent.getStringExtra("about"));
        mnewusername.setText(intent.getStringExtra("nameofuser"));
//        rbGender.setText(intent.getStringExtra("genderofuser"));
//        rbWanted.setText(intent.getStringExtra("wantgenderofuser"));

        DatabaseReference databaseReference=firebaseDatabase.getReference(firebaseAuth.getUid());

        mupdateprofilebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newName=mnewusername.getText().toString();
                newAbout=mnewabout.getText().toString();
                gender = rbGender.getText().toString();
                wantedGender = rbWanted.getText().toString();
                if(newName.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"name is empty",Toast.LENGTH_SHORT).show();
                }
                else if( ! (  gender.equals(male) ||  gender.equals(female) || gender.equals(other) ))
                {
                    rbGender.setError("Enter gender Male/Female/Other");
                }
                else if( ! (  wantedGender.equals(male) ||  wantedGender.equals(female) || wantedGender.equals(other) ))
                {
                    rbWanted.setError("Enter gender Male/Female/Other");
                }
                else if(newAbout.isEmpty())
                {
                    mnewabout.setError("About is Empty");
                }
                else if(imagepath!=null)
                {
                    mprogressBar.setVisibility(View.VISIBLE);
                    userprofile muserprofile = new userprofile(newName,firebaseAuth.getUid(),newAbout,gender,wantedGender);
                    databaseReference.setValue(muserprofile);
                    updateimagetostoarge();
                    Toast.makeText(getApplicationContext(),"Updated",Toast.LENGTH_SHORT).show();
                    mprogressBar.setVisibility(View.VISIBLE);
                    Intent intent = new Intent(UpdateProfile.this,chatActivity.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    mprogressBar.setVisibility(View.VISIBLE);
                    userprofile muserprofile = new userprofile(newName,firebaseAuth.getUid(),newAbout,gender,wantedGender);
                    databaseReference.setValue(muserprofile);
                    updateNameOnCloudFirestore();
                    Toast.makeText(getApplicationContext(),"Updated",Toast.LENGTH_SHORT).show();
                    mprogressBar.setVisibility(View.VISIBLE);
                    Intent intent = new Intent(UpdateProfile.this,chatActivity.class);
                    startActivity(intent);
                    finish();
                }




            }
        });


        mgetnewimageinimageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(intent,PICK_IMAGE);
            }
        });

        storageReference=firebaseStorage.getReference();
        storageReference.child("Images").child(firebaseAuth.getUid()).child("Profile Picture").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                ImageURIaccestoken=uri.toString();
                Picasso.get().load(uri).into(mgetnewimageinimageview);
            }
        });





    }


    private void updateNameOnCloudFirestore() {

        DocumentReference documentReference = firebaseFirestore.collection("Users").document((firebaseAuth.getUid()));
        Map<String, Object> userdata = new HashMap<>();
        userdata.put("name",newName);
        userdata.put("image",ImageURIaccestoken);
        userdata.put("uid",firebaseAuth.getUid());
        userdata.put("status","Online");
        userdata.put("questions",0);
        userdata.put("about",newAbout);
        userdata.put("gender",gender);
        userdata.put("WantedGender",wantedGender);


        documentReference.set(userdata).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(),"Profile Update Successfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Data not on Cloud Firestore. didn't send!!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void updateimagetostoarge() {


        StorageReference imageref=storageReference.child("Images").child(firebaseAuth.getUid()).child("Profile Picture");

        Bitmap bitmap=null;
        try {
            bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),imagepath);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,25,byteArrayOutputStream);
        byte[] data=byteArrayOutputStream.toByteArray();

        ///putting image to storage

        UploadTask uploadTask=imageref.putBytes(data);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                imageref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        ImageURIaccestoken=uri.toString();
                        Toast.makeText(getApplicationContext(),"URI get sucess",Toast.LENGTH_SHORT).show();
                        updateNameOnCloudFirestore();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"URI get Failed",Toast.LENGTH_SHORT).show();
                    }


                });
                Toast.makeText(getApplicationContext(),"Image is Updated",Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Image Not Updated",Toast.LENGTH_SHORT).show();
            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==PICK_IMAGE && resultCode==RESULT_OK)
        {
            imagepath=data.getData();
            mgetnewimageinimageview.setImageURI(imagepath);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }




    @Override
    protected void onStop() {
        super.onStop();
        DocumentReference documentReference=firebaseFirestore.collection("Users").document(firebaseAuth.getUid());
        documentReference.update("status","Offline").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(),"Now User is Offline",Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        DocumentReference documentReference=firebaseFirestore.collection("Users").document(firebaseAuth.getUid());
        documentReference.update("status","Online").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(),"Now User is Online",Toast.LENGTH_SHORT).show();
            }
        });

    }


}