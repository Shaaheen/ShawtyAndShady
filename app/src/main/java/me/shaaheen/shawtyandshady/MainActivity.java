package me.shaaheen.shawtyandshady;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    ChatManager chatManager;
    EditText messageBox;
    Button submitButton;
    protected static TextView messageDisplay;
    //Defaults
    private String name = "Lurker";
    private String fullName = "Lurker";
    private boolean loggedOn = false;
    private boolean stillLoading = true; //signifies that activity is still loading messages

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Firebase.setAndroidContext(this); //???

        //This changes the text of the chat messages to the same font as the ubuntu terminal
        TextView tv = (TextView) findViewById(R.id.messageDisplay);
        Typeface face = Typeface.createFromAsset(getAssets(),
                "Ubuntu-B.ttf");
        tv.setTypeface(face);

        //This will ensure that the keyboard does not automatically appear when the app starts
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        Bundle loginInfo = getIntent().getExtras();
        if (loginInfo != null) {
            loggedOn = loginInfo.getBoolean("logOn");
            if (loggedOn){
                name = loginInfo.getString("Name");
                fullName = loginInfo.getString("FullName");
            }
        }
        setTitle("Squad - [" + name + "]");

        //Gets element objects from the layout
        messageDisplay = (TextView)findViewById(R.id.messageDisplay);
        messageBox   = (EditText)findViewById(R.id.editText); //Text box for entering messages
        submitButton = (Button)findViewById(R.id.submitButton); //Button to send messages
        submitButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        sendMessage(); //When submit button clicked then launch sendMessage method
                    }
                });



        Button  logInButton = (Button) findViewById(R.id.LoginButton);
        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Create a new intent with the intention of switching classes/views
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                chatManager.removeListeners();

                //Pass the user data to the main class with the switch
                intent.putExtra("Name", name);
                intent.putExtra("FullName",fullName);
                intent.putExtra("logOn",loggedOn);

                startActivity(intent); //Switch to SlideShow class
                finish();
            }
        });

        //Initialise Chat Manager with the reference to the database
        chatManager = new ChatManager("https://boiling-torch-6214.firebaseio.com/Messages",this);
        }

                //Method to send Messages based off what is written the text box

    private void sendMessage(){
        //NB -- "CarlyCatzSnooze" is temp holder name - user needs to choose what username he wants
        //Get text from the message box and send to the chat manager to sort out
        boolean success = chatManager.sendMessage(name, messageBox.getText().toString(),loggedOn);
        messageBox.setText(""); // Reset Text box to null
        if (!success){
            messageFailed();
        }
    }

    //Method to allow changes to the message display - Allows Chat Manager to change layout element
    protected void receiveMessage(String user, String newMessage){
        if (stillLoading){
            stillLoading = false;
            messageDisplay.setText("");
        }
        //Add to message on a new line to what is already being displayed
        System.out.println("Received : " + newMessage);
        messageDisplay.setText(messageDisplay.getText().toString() + user + "-> " + newMessage + "\r\n");
    }

    protected void messageFailed(){
        Context context = getApplicationContext();
        CharSequence text = "Message failed. Not logged on ";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

}
