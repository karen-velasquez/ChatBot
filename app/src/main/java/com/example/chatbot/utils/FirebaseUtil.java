package com.example.chatbot.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Collection;

public class FirebaseUtil {

    public static String currentUserId()
    {
        return FirebaseAuth.getInstance().getUid();
    }

    public static boolean isLoggedIn(){
        if(currentUserId()!=null){
            return true;
        }
        return false;
    }
    public static DocumentReference currentUserDetails()
    {
        return FirebaseFirestore.getInstance().collection("users").document(currentUserId());
    }


    public static CollectionReference allUserCollectionReference(){
        return FirebaseFirestore.getInstance().collection("users");
    }
    public static DocumentReference getChatroomReferenceGoals(String chatroomId){
        return FirebaseFirestore.getInstance().collection("goals").document(chatroomId);
    }

    public static DocumentReference getChatroomReferenceProgress(String chatroomId){
        return FirebaseFirestore.getInstance().collection("progress").document(chatroomId);
    }

    public static CollectionReference getChatroomMessageReferenceGoals(String chatroomId){
        return getChatroomReferenceGoals(chatroomId).collection("chats");
    }
    public static CollectionReference getChatroomMessageReferenceProgress(String chatroomId){
        return getChatroomReferenceProgress(chatroomId).collection("chats");
    }

    public static String getChatroomId(String userId1, String userId2){
        if(userId1.hashCode()<userId2.hashCode()){
            return userId1+"_"+userId2;
        }else{
            return userId2+"_"+userId1;
        }
    }

}
