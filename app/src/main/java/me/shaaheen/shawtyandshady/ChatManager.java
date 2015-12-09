package me.shaaheen.shawtyandshady;

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

    ChatManager(String firebaseURL){
        myFirebaseRef = new Firebase(firebaseURL);
    }

    protected void sendMessage(String userName,String message){
        Map<String, String> post = new HashMap<String, String>();
        post.put("User", userName);
        post.put("Message", message);
        myFirebaseRef.push().setValue(post);
    }

    protected void startReceivingMessages(){

        myFirebaseRef.addChildEventListener(new ChildEventListener() {
            // Retrieve new posts as they are added to the database
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildKey) {
                Map<String, String> post = snapshot.getValue(Map.class);
                System.out.println("Message is :" + post.get(1));
            }
            // ....

            // Get the data on a post that has changed
            @Override
            public void onChildChanged(DataSnapshot snapshot, String previousChildKey) {
                //String title = (String) snapshot.child("title").getValue();
                //System.out.println("The updated post title is " + title);
            }

            // ....
            // Get the data on a post that has been removed
            @Override
            public void onChildRemoved(DataSnapshot snapshot) {
                //String title = (String) snapshot.child("title").getValue();
                //System.out.println("The blog post titled " + title + " has been deleted");
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }
}
