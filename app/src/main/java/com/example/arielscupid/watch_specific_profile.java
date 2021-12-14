package com.example.arielscupid;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class watch_specific_profile extends AppCompatActivity {


    TextView mviewusername;
    TextView mviewabout;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;

    FirebaseFirestore firebaseFirestore;

    ImageView mviewuserinimageview;
    StorageReference storageReference;

    private String ImageURIaccestoken;

    androidx.appcompat.widget.Toolbar mtoolbarofviewprofile;
    ImageButton mbackbuttonofviewotherprofile;

    FirebaseStorage firebaseStorage;

    android.widget.Button disablematchbtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_specific_profile);


        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseStorage=FirebaseStorage.getInstance();

        mviewuserinimageview=findViewById(R.id.viewuserimageinimageview1);
        mviewusername=findViewById(R.id.myapptext1);
        mviewabout=findViewById(R.id.viewAbout1);

        mtoolbarofviewprofile=findViewById(R.id.toolbarOfViewProfileActivity1);


        mbackbuttonofviewotherprofile=findViewById(R.id.backbuttonofviewotherprofile1);

        disablematchbtn=findViewById(R.id.disablematch);


        setSupportActionBar(mtoolbarofviewprofile);
        mbackbuttonofviewotherprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String uid = getIntent().getStringExtra("getUID");

        storageReference=firebaseStorage.getReference();
        storageReference.child("Images").child(uid).child("Profile Picture").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                ImageURIaccestoken=uri.toString();
                Picasso.get().load(uri).into(mviewuserinimageview);
            }
        });
        mviewabout.setText(getIntent().getStringExtra("about"));
        mviewusername.setText(getIntent().getStringExtra("nameofuser"));
        mviewabout.setMovementMethod(new ScrollingMovementMethod());



        disablematchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"disable button pressed",Toast.LENGTH_SHORT).show();
            }
        });




    }
}