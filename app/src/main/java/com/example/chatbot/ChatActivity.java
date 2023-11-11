package com.example.chatbot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import java.util.*;

import com.example.chatbot.adapter.ChatRecyclerAdapter;
import com.example.chatbot.model.ChatMessageModel;
import com.example.chatbot.model.ChatroomModel;
import com.example.chatbot.model.UserModel;
import com.example.chatbot.utils.AndroidUtil;
import com.example.chatbot.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.sql.Time;
import java.util.Arrays;

public class ChatActivity extends AppCompatActivity {

    //obteniendo un stepper
    int step = 0;

    UserModel otherUser;
    String chatroomId;
    ChatroomModel chatroomModel;


    ChatRecyclerAdapter adapter;

    EditText messageInput;
    ImageButton sendMessageBtn;
    ImageButton backBtn;
    TextView otherUsername;
    RecyclerView recyclerView;

    ImageButton btnback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //get Usermodel
        otherUser = AndroidUtil.getUserModelFromIntent(getIntent());

        chatroomId = FirebaseUtil.getChatroomId(FirebaseUtil.currentUserId(), otherUser.getUserId());

        messageInput = findViewById(R.id.chat_message_input);
        sendMessageBtn = findViewById(R.id.message_send_btn);
        backBtn = findViewById(R.id.back_btn);
        otherUsername = findViewById(R.id.other_username);
        recyclerView = findViewById(R.id.chat_recycler_view);

        backBtn.setOnClickListener((v -> {
            onBackPressed();
        }));

        otherUsername.setText(otherUser.getUsername());

        //cuando se selecciona enviar el mensaje
        sendMessageBtn.setOnClickListener((v -> {
            String message = messageInput.getText().toString().trim();

            if(message.isEmpty())
                return;
            sendMessageToUser(message);

            //las preguntas para objetivos
            GoalsQuestions();

        }));



        getOrCreateChatroomModel();
        setupChatRecyclerView();
    }



    void GoalsQuestions() {
        step = step + 1;
        Map<String, String> m = new HashMap<String, String>();
        m.put("1", "Hola, podemos delimitar algunos objetivos para el día de hoy");
        m.put("2", "Anota cuales son tus mejores esperanzas para el día de hoy");
        m.put("3", "¿Qué ha mejorado en los últimos días?");
        m.put("4", "¿Qué seria diferente si el problema no estuviera presente?");
        m.put("5", "Mantente atento a lo que pase el dia de hoy y si algo de lo que me comentaste pasó, podrias registrarlo en tu calendario de progreso");

        //prueba de mensaje
        sendMessageToChatBot(m.get(String.valueOf(step)));

        //desabilitanado el edit cuando llegue al limite
        if(step==m.size()){
            messageInput.setFocusable(false);
            messageInput.setEnabled(false);
            messageInput.setCursorVisible(false);
            messageInput.setKeyListener(null);
            messageInput.setBackgroundColor(Color.TRANSPARENT);
        }
    }







    void setupChatRecyclerView(){
        Query query = FirebaseUtil.getChatroomMessageReferenceGoals(chatroomId)
                .orderBy("timestamp", Query.Direction.DESCENDING);


        FirestoreRecyclerOptions<ChatMessageModel> options = new FirestoreRecyclerOptions.Builder<ChatMessageModel>()
                .setQuery(query,ChatMessageModel.class).build();


        adapter = new ChatRecyclerAdapter(options,getApplicationContext());
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setReverseLayout(true);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        adapter.startListening();
        /*
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                recyclerView.smoothScrollToPosition(0);
            }
        });*/

    }


    void sendMessageToUser(String message){

        chatroomModel.setLastMessageTimestamp(Timestamp.now());
        chatroomModel.setLastMessageSenderId(FirebaseUtil.currentUserId());
        FirebaseUtil.getChatroomReferenceGoals(chatroomId).set(chatroomModel);

        ChatMessageModel chatMessageModel = new ChatMessageModel(message, FirebaseUtil.currentUserId(),Timestamp.now());
        FirebaseUtil.getChatroomMessageReferenceGoals(chatroomId).add(chatMessageModel)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if(task.isSuccessful()){
                            messageInput.setText("");
                        }
                    }
                });

   }



   //la pruebaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
   void sendMessageToChatBot(String message){

       chatroomModel.setLastMessageTimestamp(Timestamp.now());
       chatroomModel.setLastMessageSenderId(FirebaseUtil.currentUserId());
       FirebaseUtil.getChatroomReferenceGoals(chatroomId).set(chatroomModel);

       ChatMessageModel chatMessageModel = new ChatMessageModel(message, otherUser.getUserId(),Timestamp.now());
       FirebaseUtil.getChatroomMessageReferenceGoals(chatroomId).add(chatMessageModel)
               .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                   @Override
                   public void onComplete(@NonNull Task<DocumentReference> task) {
                       if(task.isSuccessful()){
                           messageInput.setText("");
                       }
                   }
               });

   }




    void getOrCreateChatroomModel(){
        FirebaseUtil.getChatroomReferenceGoals(chatroomId).get().addOnCompleteListener(task -> {
          if(task.isSuccessful()) {
              chatroomModel = task.getResult().toObject(ChatroomModel.class);
                if(chatroomModel==null)
                {
                    //first time
                    chatroomModel = new ChatroomModel(
                            chatroomId,
                            Arrays.asList(FirebaseUtil.currentUserId(),otherUser.getUserId()),
                            Timestamp.now(),
                            ""
                    );
                    FirebaseUtil.getChatroomReferenceGoals(chatroomId).set(chatroomModel);
                }
          }
        });
    }
}