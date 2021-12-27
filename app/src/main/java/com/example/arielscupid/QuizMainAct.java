package com.example.arielscupid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
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


    ProgressDialog progressDialog;
    private TextView questions, questionNum;
    private Button option1Btn, option2Btn, option3Btn, option4Btn;
    private ArrayList<QuizModel> quizModelArrayList;
    Random random;
    int currentScore = 0, questionAttempted = 0, currentPos;
    private static int Splash_Timer=3500;
    BottomSheetDialog bottomSheetDialog ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_main);

        bottomSheetDialog =new BottomSheetDialog(QuizMainAct.this);

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

        currentPos = 0;//random.nextInt(quizModelArrayList.size());

        setDataToViews(currentPos);

        option1Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (quizModelArrayList.get(currentPos).getAnswer().trim().toLowerCase().equals(option1Btn.getText().toString().trim().toLowerCase())) {
//                    currentScore++;
//                }
                currentScore = currentScore + 10;

                questionAttempted++;
                currentPos +=  1;//random.nextInt(quizModelArrayList.size());
                setDataToViews(currentPos);
            }
        });

        option2Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (quizModelArrayList.get(currentPos).getAnswer().trim().toLowerCase().equals(option2Btn.getText().toString().trim().toLowerCase())) {
//                    currentScore++;
//                }
                currentScore = currentScore + 75;

                questionAttempted++;
                currentPos +=  1;//random.nextInt(quizModelArrayList.size());
                setDataToViews(currentPos);
            }
        });

        option3Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (quizModelArrayList.get(currentPos).getAnswer().trim().toLowerCase().equals(option3Btn.getText().toString().trim().toLowerCase())) {
//                    currentScore++;
//                }
                currentScore = currentScore + 200;

                questionAttempted++;
                currentPos +=  1;//random.nextInt(quizModelArrayList.size());
                setDataToViews(currentPos);
            }
        });

        option4Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (quizModelArrayList.get(currentPos).getAnswer().trim().toLowerCase().equals(option4Btn.getText().toString().trim().toLowerCase())) {
//                    currentScore++;
//                }
                currentScore = currentScore + 500;
                questionAttempted++;
                currentPos +=  1;//random.nextInt(quizModelArrayList.size());
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
//        progressDialog.setMessage("Please wait while Our AI algorithm find you a match...");
//        progressDialog.setTitle("Matching");
//        progressDialog.setCanceledOnTouchOutside(false);
//        progressDialog.show();
        View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.score_bottom_sheets, (LinearLayout)findViewById(R.id.idLLScore));
        TextView score = bottomSheetView.findViewById(R.id.idScore);
        Button backQuizBtn = bottomSheetView.findViewById(R.id.idBackQuiz);

        score.setText("Please wait while Our AI algorithm find you a match on the app based on your answer...");
        backQuizBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPos += 1;// random.nextInt(quizModelArrayList.size());
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
                    //Toast.makeText(getApplicationContext(), "Update Score", Toast.LENGTH_SHORT).show();
                }
            });


            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    bottomSheetDialog.dismiss();
                    Intent intent = new Intent( QuizMainAct.this,chatActivity.class);
                    startActivity(intent);
                    finish();
                }
            },Splash_Timer);
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
        quizModelArrayList.add(new QuizModel("What do you like to eat ?", "pizza", "hamburger", "sushi", "pasta", "pasta"));
        quizModelArrayList.add(new QuizModel("Where would you live ?", "Tel-Aviv", "Haifa", "Afula", "Nof Ayalon", "Afula"));
        quizModelArrayList.add(new QuizModel("Any specific qualities you are looking for in your partner?", "Caring", "Respect", "Funny", "Kind", "Respect"));
        quizModelArrayList.add(new QuizModel("What is your ideal Saturday morning ?", "travel", "sit at home", "picnic", "work", "picnic"));
        quizModelArrayList.add(new QuizModel("Favorite animal ?  ", "Monkey", "Dog", "Cat", "Lion", "Dog"));
        quizModelArrayList.add(new QuizModel("Favorite drink ", "Vodka", "Wine", "Tequila", "Whiskey", "Whiskey"));

        quizModelArrayList.add(new QuizModel(" Describe yourself in a tweet:", "Caring", "Respect", "Funny", "Kind", "Funny"));
        quizModelArrayList.add(new QuizModel(" What’s something you just don’t understand the hype about?", "Football", "Dogs", "Cristiano Ronaldo", "work", "work"));
        quizModelArrayList.add(new QuizModel("Favorite music genre ?  ", "Rock", "Pop", "Noa Kirel", "Mizrahit", "Mizrahit"));
        quizModelArrayList.add(new QuizModel("Favorite Movie ", "Spiderman", "Superman", "Horrible Bosses", "american sniper", "american sniper"));
    }






}
