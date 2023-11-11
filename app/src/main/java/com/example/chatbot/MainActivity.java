package com.example.chatbot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.chatbot.adapter.SearchUserRecyclerAdapter;
import com.example.chatbot.utils.AndroidUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import com.example.chatbot.utils.FirebaseUtil;
import com.example.chatbot.model.UserModel;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.model.FieldIndex;

import androidx.recyclerview.widget.LinearLayoutManager;

public class MainActivity extends AppCompatActivity {


    CardView btnObjetivo;
    CardView btnProgreso;

    CardView btnObjetivoAdmin;
    CardView btnProgresoAdmin;

    Button btnAdmin;
    //usuario que ingreso
    UserModel userModel;

    String typeUser;

    //Inten para pasar a otra pantalla
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnObjetivo = findViewById(R.id.btnObjetivo);
        btnProgreso = findViewById(R.id.btnProgreso);
        btnObjetivoAdmin = findViewById(R.id.btnObjetivoAdmin);
        btnProgresoAdmin = findViewById(R.id.btnProgresoAdmin);



        //create the user
        UserModel theadmin = new UserModel();
        theadmin.setPhone("+59177777777");
        theadmin.setUserId("2wA5U7WfLjXYMVE106fFu4YmbXq2");
        theadmin.setUsername("ChatBot");
        theadmin.setTypeUser("admin");



        btnObjetivo.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                AndroidUtil.passUserModelAsIntent(intent, theadmin);
                startActivity(intent);
            });

        btnProgreso.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, ChatActivityProgress.class);
                AndroidUtil.passUserModelAsIntent(intent, theadmin);
                startActivity(intent);
            });


        btnObjetivoAdmin.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
            });
        btnProgresoAdmin.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, SearchActivityProgress.class);
                startActivity(intent);
            });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseUtil.currentUserId().equals("2wA5U7WfLjXYMVE106fFu4YmbXq2")){
            btnObjetivo.setVisibility(View.INVISIBLE);
            btnProgreso.setVisibility(View.INVISIBLE);
        }else{
            btnObjetivoAdmin.setVisibility(View.GONE);
            btnProgresoAdmin.setVisibility(View.GONE);
        }
    }




}