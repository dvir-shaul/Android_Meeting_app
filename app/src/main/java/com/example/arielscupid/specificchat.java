package com.example.arielscupid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class specificchat extends AppCompatActivity {

    EditText mgetmessage;
    ImageButton msendmessagebutton;

    CardView msendmessagecardview;
    androidx.appcompat.widget.Toolbar mtoolbarofspecificchat;
    ImageView mimageviewofspecificuser;
    TextView mnameofuser;

    private String enteredmessage;
    String mrecivername,msendername,mreciveruid,msenderuid;

    private FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;

    String senderroom,reciverroom;
    /**
     * leave chat
     */
    ImageButton mbackbuttonofspecificchat;
    /**
     * keep messages
     */
    RecyclerView mmessagerecylerview;

    String currenttime;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;


    MessagesAdapter messagesAdapter;
    ArrayList<Messages> messagesArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specificchat);

        mgetmessage=findViewById(R.id.getmessage);
        msendmessagecardview=findViewById(R.id.cardviewofsendmessage);
        msendmessagebutton=findViewById(R.id.sendmessage);

        mtoolbarofspecificchat=findViewById(R.id.toolbarofspecificchat);
        mnameofuser=findViewById(R.id.Nameofspecificuser);
        mimageviewofspecificuser=findViewById(R.id.specificuserinimageview);

        mbackbuttonofspecificchat=findViewById(R.id.backbuttonofspecificchat);




        messagesArrayList = new ArrayList<>();

        mmessagerecylerview=findViewById(R.id.recyclerviewofspecific);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        mmessagerecylerview.setLayoutManager(linearLayoutManager);
        messagesAdapter= new MessagesAdapter(specificchat.this, messagesArrayList);
        mmessagerecylerview.setAdapter(messagesAdapter);



        setSupportActionBar(mtoolbarofspecificchat);
        mtoolbarofspecificchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Toolbar is Clicked",Toast.LENGTH_SHORT);
            }
        });


        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        calendar=Calendar.getInstance();
        simpleDateFormat=new SimpleDateFormat("hh:mm a");

        msenderuid=firebaseAuth.getUid();
        mreciveruid=getIntent().getStringExtra("reciveruid");
        mrecivername=getIntent().getStringExtra("name");

        /**
         * 2 rooms to fetch all messages that send
         */
        senderroom=msenderuid+mreciveruid;
        reciverroom=mreciveruid+msenderuid;


        DatabaseReference databaseReference=firebaseDatabase.getReference().child("chats").child(senderroom).child("messages");
        messagesAdapter=new MessagesAdapter(specificchat.this,messagesArrayList);


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagesArrayList.clear();
                for(DataSnapshot snapshot1:snapshot.getChildren())
                {
                    Messages messages=snapshot1.getValue(Messages.class);
                    messagesArrayList.add(messages);
                }
                messagesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });









        mbackbuttonofspecificchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        mnameofuser.setText(mrecivername);
        String uri=getIntent().getStringExtra("imageuri");

        if(uri.isEmpty()){
            Toast.makeText(getApplicationContext(),"null is recived",Toast.LENGTH_SHORT);
        }
        else
        {
            Picasso.get().load(uri).into(mimageviewofspecificuser);
        }


        /**
         * put the message on realtime database function
         */

        msendmessagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                enteredmessage=mgetmessage.getText().toString();
                if(enteredmessage.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Enter a message first",Toast.LENGTH_SHORT);
                }
                else
                {
                    Date date = new Date();
                    currenttime=simpleDateFormat.format(calendar.getTime());
                    Messages messages = new Messages(enteredmessage, firebaseAuth.getUid(), date.getTime(),currenttime );
                    firebaseDatabase=FirebaseDatabase.getInstance();
                    firebaseDatabase.getReference()
                            .child("chats")
                            .child(senderroom)
                            .child("messages")
                            .push()
                            .setValue(messages)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    firebaseDatabase.getReference()
                                            .child("chats")
                                            .child(reciverroom)
                                            .child("messages")
                                            .push()
                                            .setValue(messages)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {

                                                }
                                            });
                                }
                            });
                    mgetmessage.setText(null);
                }
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        messagesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(messagesAdapter!=null){
            messagesAdapter.notifyDataSetChanged();
        }
    }
}