package com.example.arielscupid;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class QuizMainAct extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private Uri scorePath;


    private FirebaseFirestore firebaseFirestore;

    private ImageView mgetnewimageinimageview;
    private StorageReference storageReference;

    private String ImageURIaccestoken;

    private androidx.appcompat.widget.Toolbar mtoolbarofUpdateprofile;
    private ImageButton mbackbuttonofupdateprofile;

    private FirebaseStorage firebaseStorage;

    private TextView questions, questionNum;
    private Button option1Btn, option2Btn, option3Btn, option4Btn;
    private ArrayList<QuizModel> quizModelArrayList;
    Random random;
    int currentScore = 0, questionAttempted = 0, currentPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_main);

        firebaseAuth=FirebaseAuth.getInstance();

        firebaseFirestore = FirebaseFirestore.getInstance();

        questions = findViewById(R.id.idQuestions);
        questionNum = findViewById(R.id.idQuest);
        option1Btn = findViewById(R.id.idButtonOption1);
        option2Btn = findViewById(R.id.idButtonOption2);
        option3Btn = findViewById(R.id.idButtonOption3);
        option4Btn = findViewById(R.id.idButtonOption4);
        quizModelArrayList = new ArrayList<>();

        random = new Random();

        getQuizQuestion(quizModelArrayList);

        currentPos = random.nextInt(quizModelArrayList.size());

        setDataToViews(currentPos);

        option1Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quizModelArrayList.get(currentPos).getAnswer().trim().toLowerCase().equals(option1Btn.getText().toString().trim().toLowerCase())) {
                    currentScore++;
                }
                questionAttempted++;
                currentPos = random.nextInt(quizModelArrayList.size());
                setDataToViews(currentPos);
            }
        });

        option2Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quizModelArrayList.get(currentPos).getAnswer().trim().toLowerCase().equals(option2Btn.getText().toString().trim().toLowerCase())) {
                    currentScore++;
                }
                questionAttempted++;
                currentPos = random.nextInt(quizModelArrayList.size());
                setDataToViews(currentPos);
            }
        });

        option3Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quizModelArrayList.get(currentPos).getAnswer().trim().toLowerCase().equals(option3Btn.getText().toString().trim().toLowerCase())) {
                    currentScore++;
                }
                questionAttempted++;
                currentPos = random.nextInt(quizModelArrayList.size());
                setDataToViews(currentPos);
            }
        });

        option4Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quizModelArrayList.get(currentPos).getAnswer().trim().toLowerCase().equals(option4Btn.getText().toString().trim().toLowerCase())) {
                    currentScore++;
                }
                questionAttempted++;
                currentPos = random.nextInt(quizModelArrayList.size());
                setDataToViews(currentPos);
            }
        });



        DatabaseReference databaseReference;
        databaseReference = FirebaseDatabase.getInstance().getReference("Question");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    QuizModel quizModel = dataSnapshot.getValue(QuizModel.class);
                    quizModelArrayList.add(quizModel);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showBottomSheet(){
        BottomSheetDialog bottomSheetDialog =new BottomSheetDialog(QuizMainAct.this);
        View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.score_bottom_sheets, (LinearLayout)findViewById(R.id.idLLScore));
        TextView score = bottomSheetView.findViewById(R.id.idScore);
        Button backQuizBtn = bottomSheetView.findViewById(R.id.idBackQuiz);
        score.setText("Your score is \n" + currentScore + "/10");
        backQuizBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPos = random.nextInt(quizModelArrayList.size());
                setDataToViews(currentPos);
                questionAttempted = 1;
                currentScore = 0;
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();




    }

    private void setDataToViews(int currentPos){
        questionNum.setText("Question Attempted :" + questionAttempted + "/10");
        if (questionAttempted == 10){
            showBottomSheet();
            DocumentReference documentReference = firebaseFirestore.collection("Users").document(firebaseAuth.getUid());
            documentReference.update("questions", currentScore).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(getApplicationContext(), "Update Score", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            questions.setText(quizModelArrayList.get(currentPos).getQuestion());
            option1Btn.setText(quizModelArrayList.get(currentPos).getOption1());
            option2Btn.setText(quizModelArrayList.get(currentPos).getOption2());
            option3Btn.setText(quizModelArrayList.get(currentPos).getOption3());
            option4Btn.setText(quizModelArrayList.get(currentPos).getOption4());
        }
    }






    private void getQuizQuestion(ArrayList<QuizModel> quizModelArrayList) {
        quizModelArrayList.add(new QuizModel("what do you like to eat?", "pizza", "hamburger", "sushi", "pasta", "pasta"));
        quizModelArrayList.add(new QuizModel("2 + 2 = ?", "0", "1", "2", "4", "4"));
        quizModelArrayList.add(new QuizModel("6%4 = ? ", "0", "1", "2", "4", "2"));
        quizModelArrayList.add(new QuizModel("pariz iz in ?  ", "Africa", "Israel", "America", "France", "France"));
        quizModelArrayList.add(new QuizModel("What green inside and outside ? ", "Watermelon", "strawberry", "apple", "broccoli", "broccoli"));
    }






}
