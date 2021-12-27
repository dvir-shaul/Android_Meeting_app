package com.example.arielscupid;

import static android.graphics.Color.*;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;
import com.squareup.picasso.Picasso;


import org.w3c.dom.Text;

import java.util.Objects;

public class ChatsFragment extends Fragment {

    private FirebaseFirestore firebaseFirestore;
    LinearLayoutManager linearLayoutManager;

    private FirebaseAuth firebaseAuth;

    ImageView mimageviewofuser;

    FirestoreRecyclerAdapter<firebasemodel, NoteViewHolder> chatAdapter;

    RecyclerView mrecyclerView;


    double ratio = 0.6;
    double amount, amount_top,amount_bottom;
    String Amountl, want_gen;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.chats_fragment, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        mrecyclerView = v.findViewById(R.id.recyclerview);

        CollectionReference users_db = firebaseFirestore.collection("Users");


//        users_db.whereEqualTo("uid", firebaseAuth.getUid())
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Amountl = Objects.requireNonNull(document.getData().get("questions")).toString();
//                                amount = Double.parseDouble(Amountl);
//                                amount_top = amount + (amount * ratio);
//                                amount_bottom = amount - (amount * ratio);
//                                want_gen = Objects.requireNonNull(document.getData().get("WantedGender")).toString();
//                            }
//                        }
//                    }
//                });
//                            Query query = firebaseFirestore.collection("Users").whereNotEqualTo("uid", firebaseAuth.getUid())
//                                    .whereEqualTo("WantedGender", want_gen); // except me


        Query query = firebaseFirestore.collection("Users").whereNotEqualTo("uid", firebaseAuth.getUid()).limit(1); // except me

        FirestoreRecyclerOptions<firebasemodel> allUsername = new FirestoreRecyclerOptions.Builder<firebasemodel>().setQuery(query, firebasemodel.class).build();


        chatAdapter = new FirestoreRecyclerAdapter<firebasemodel, NoteViewHolder>(allUsername) {
            @Override
            protected void onBindViewHolder(@NonNull NoteViewHolder noteViewHolder, int i, @NonNull firebasemodel firebasemodel) {
                noteViewHolder.paticularusername.setText(firebasemodel.getName());
                String uri = firebasemodel.getImage();
                Picasso.get().load(uri).into(mimageviewofuser);
                if (firebasemodel.getStatus().equals("Online")) {
                    noteViewHolder.statusofuser.setText(firebasemodel.getStatus());
                    noteViewHolder.statusofuser.setTextColor(GREEN);
                } else {
                    noteViewHolder.statusofuser.setText(firebasemodel.getStatus());
                }

                /**
                 * to start chatting with 1 person
                 */


                noteViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), specificchat.class);
                        intent.putExtra("name", firebasemodel.getName());
                        intent.putExtra("reciveruid", firebasemodel.getUid());
                        intent.putExtra("imageuri", firebasemodel.getImage());
                        intent.putExtra("getAbout", firebasemodel.getAbout());
                        intent.putExtra("getQuestions", firebasemodel.getQuestions());

                        startActivity(intent);
                    }
                });
            }


            @NonNull
            @Override
            public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatviewlayout, parent, false);
                return new NoteViewHolder(view);
            }
        };


        mrecyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mrecyclerView.setLayoutManager(linearLayoutManager);
        mrecyclerView.setAdapter(chatAdapter);
        return v;
    }


    /**
     * how to show the name and status on chats fragment
     */

    public class NoteViewHolder extends RecyclerView.ViewHolder {

        private TextView paticularusername;
        private TextView statusofuser;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            paticularusername = itemView.findViewById(R.id.nameofuser);
            statusofuser = itemView.findViewById(R.id.statusofuser);
            mimageviewofuser = itemView.findViewById(R.id.imageviewofuser);
        }

    }


    @Override
    public void onStart() {
        super.onStart();
        chatAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (chatAdapter != null) {
            chatAdapter.stopListening();
        }
    }
}