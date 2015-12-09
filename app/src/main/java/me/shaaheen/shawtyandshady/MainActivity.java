package me.shaaheen.shawtyandshady;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    ChatManager chatManager;
    EditText messageBox;
    Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        messageBox   = (EditText)findViewById(R.id.editText);
        submitButton = (Button)findViewById(R.id.submitButton);
        submitButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        sendMessage();
                    }
                });

        System.out.println("BEFORE 1");
        Firebase.setAndroidContext(this);
        chatManager = new ChatManager("https://boiling-torch-6214.firebaseio.com/Messages");
        System.out.println("BEFORE HTTP");
    }

    private void sendMessage(){
        chatManager.sendMessage("CarlyCatzSnooze",messageBox.getText().toString());
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
