package com.example.chatbot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.example.chatbot.adapter.SearchUserRecyclerAdapter;
import com.example.chatbot.adapter.SearchUserRecyclerProgressAdapter;
import com.example.chatbot.model.UserModel;
import com.example.chatbot.utils.AndroidUtil;
import com.example.chatbot.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.chatbot.adapter.SearchUserRecyclerAdapter;
import com.example.chatbot.utils.AndroidUtil;
import com.google.firebase.firestore.Query;

import com.example.chatbot.utils.FirebaseUtil;
import com.example.chatbot.model.UserModel;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import androidx.recyclerview.widget.LinearLayoutManager;

public class SearchActivityProgress extends AppCompatActivity {


    ImageButton backButton;
    RecyclerView recyclerView;

    SearchUserRecyclerProgressAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_progress);


        backButton = findViewById(R.id.back_btn);
        recyclerView = findViewById(R.id.search_user_recycler_view);




        backButton.setOnClickListener(v -> {
            onBackPressed();
        });




        setupSearchRecyclerView();

    }




    void setupSearchRecyclerView(){

        /*Query query = FirebaseUtil.allUserCollectionReference()
                .whereGreaterThanOrEqualTo("username",searchTerm);*/

        Query query = FirebaseUtil.allUserCollectionReference();


        FirestoreRecyclerOptions<UserModel> options = new FirestoreRecyclerOptions.Builder<UserModel>()
                .setQuery(query,UserModel.class).build();


        adapter = new SearchUserRecyclerProgressAdapter(options,getApplicationContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(adapter!=null)
            adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(adapter!=null)
            adapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(adapter!=null)
            adapter.startListening();
    }
}