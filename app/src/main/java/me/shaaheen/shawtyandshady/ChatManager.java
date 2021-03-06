package me.shaaheen.shawtyandshady;

import android.content.Context;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Shaaheen on 12/10/2015.
 * Class to handle the communication of the application to the online database
 * That is to handle the sending and receiving of messages with cloud database
 */
public class ChatManager {
    Firebase myFirebaseRef; //Link to Firebase online database - used as place to send and receive messages from
    MainActivity mainView;
    ChildEventListener mainListener;

    //Constructor to connect to database
    ChatManager(String firebaseURL,MainActivity main) {
        myFirebaseRef = new Firebase(firebaseURL);
        this.mainView = main;
        startReceivingMessages();
    }

    //Method to update database with a new message entry
    protected boolean sendMessage(String userName,String message,boolean loggedOn){
        //Map used so the message entry contains two bits of information
        // - the user the sent the message and - the message itself
        if (loggedOn){
            Map<String, String> post = new HashMap<String, String>();
            post.put("User", userName); //The key is "User" and the value is the actual username
            post.put("Message", message); //Key is "Message" and value is actual message

            //Make new entry in database - set the value of the new entry to the hashmap object
            myFirebaseRef.push().setValue(post);
            return true;
        }
        else{
            System.out.println("Message failed. Not logged on ");
            return false;
        }
    }

    //Method to add the neccessary event listeners to catch any new message entries on the database
    protected void startReceivingMessages(){

        //Create Event listener for all events(reading etc)
        mainListener = new ChildEventListener() {

            // Retrieve new posts as they are added to the database - NB!!!
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildKey) {
                Map<String, String> post = snapshot.getValue(Map.class); //Get the Map object that was written
                System.out.println("Message is :" + post.get("Message"));

                mainView.receiveMessage(post.get("User"),post.get("Message"));
            }

            //METHODS NEEDED TO BE IMPLEMENTED FOR EVENT LISTENER - MUST BE ADDED TO

            // Get the data on a post that has changed
            @Override
            public void onChildChanged(DataSnapshot snapshot, String previousChildKey) {
            }

            // Get the data on a post that has been removed
            @Override
            public void onChildRemoved(DataSnapshot snapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }

        };

        //Add event listener on database
        myFirebaseRef.addChildEventListener(mainListener);

    }

    public void removeListeners(){
        myFirebaseRef.removeEventListener(mainListener);
    }
}
