package com.example.mitchel.a436project;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Messenger;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.text.format.Formatter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Objects;
import java.net.DatagramSocket;


@TargetApi(19)
public class MainActivity extends AppCompatActivity {
    static final int SIGN_IN_REQUEST_CODE = 1;
    FirebaseListAdapter<ChatMessage> adapter = null;
    String ip;
    String type = "";
    String txt = "";
    boolean gameInSession = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(FirebaseAuth.getInstance().getCurrentUser() == null) {
            // Start sign in/sign up activity
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .build(),
                    SIGN_IN_REQUEST_CODE
            );
        } else {
            // User is already signed in. Therefore, display
            // a welcome Toast
            Toast.makeText(this,
                    "Welcome " + FirebaseAuth.getInstance()
                            .getCurrentUser()
                            .getDisplayName(),
                    Toast.LENGTH_LONG)
                    .show();

            // Load chat room contents
            displayMessages();
        }
        FloatingActionButton fab =
                (FloatingActionButton)findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = (EditText)findViewById(R.id.input);

                ChatMessage newMessage = new ChatMessage(input.getText().toString(),
                        FirebaseAuth.getInstance()
                                .getCurrentUser()
                                .getDisplayName(), "chat");
                // Read the input field and push a new instance
                // of ChatMessage to the Firebase database
                FirebaseDatabase.getInstance()
                        .getReference().child("messages")//.push()
                        .child(Long.toString(newMessage.getMessageTime()))
                        .setValue(newMessage);

                // Clear the input
                input.setText("");
            }
        });

        FirebaseDatabase.getInstance().getReference().child("buttons").child("Winner").child("messageText").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(Objects.equals(dataSnapshot.getValue(String.class), " ") ){
                    gameInSession = true; //because when the person who creates challenge switches screens, they set winner to " "
                }
                else if(Objects.equals(dataSnapshot.getValue(String.class), "Game Terminated")){
                    gameInSession = false;
                }
                else{
                    gameInSession = false; //either will be x Wins! or Game ended(when someone leaves the game)
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void displayMessages() {
        displayChatMessages();
    }

    private void displayChatMessages() {
        ListView chatMessages = (ListView) findViewById(R.id.list_of_chat_messages);

        adapter = new FirebaseListAdapter<ChatMessage>(this, ChatMessage.class, R.layout.message,
                FirebaseDatabase.getInstance().getReference().child("messages")) {
            @Override
            protected void populateView(View v, final ChatMessage model, int position) {
                // Get references to the views of message
                TextView messageText = (TextView) v.findViewById(R.id.message_text);
                TextView messageUser = (TextView) v.findViewById(R.id.message_user);
                TextView messageTime = (TextView) v.findViewById(R.id.message_time);
                final Button acceptButton = (Button) v.findViewById(R.id.button_challenge);

                // Set their text
                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());

                String currType= model.getMessageType();
                if(Objects.equals("challenge",currType))
                {
                    acceptButton.setVisibility(View.VISIBLE);
                    acceptButton.setHint(model.getSenderAddress());
                    //when the challenge button is clicked
                    acceptButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startGame(acceptButton);
                            model.click();
                            //update that message as clicked
                            FirebaseDatabase.getInstance().getReference().child("messages").child(model.getMessageTime() + "Challenge").setValue(model);
                        }
                    });
                    FirebaseDatabase.getInstance().getReference().child("messages").child(model.getMessageTime() + "Challenge").child("clicked").addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(Objects.equals(true, dataSnapshot.getValue(Boolean.class))) {
                                acceptButton.setText("Complete");
                                acceptButton.setClickable(false);
                            }
                            else{
                                acceptButton.setText("Accept Challenge");
                                acceptButton.setClickable(true);
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
                else
                {
                    acceptButton.setVisibility(View.GONE);
                }

                // Format the date before showing it
                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                        model.getMessageTime()));
            }
        };

        chatMessages.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SIGN_IN_REQUEST_CODE) {
            if(resultCode == RESULT_OK) {
                Toast.makeText(this,
                        "Successfully signed in. Welcome!",
                        Toast.LENGTH_LONG)
                        .show();
                displayChatMessages();
            } else {
                Toast.makeText(this,
                        "We couldn't sign you in. Please try again later.",
                        Toast.LENGTH_LONG)
                        .show();

                // Close the app
                finish();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_create_challenge && !gameInSession) {
            ChatMessage newChallenge = new ChatMessage(FirebaseAuth.getInstance()
                    .getCurrentUser()
                    .getDisplayName() + " created an OPEN CHALLENGE in TIC-TAC-TOE!",
                    FirebaseAuth.getInstance()
                            .getCurrentUser()
                            .getDisplayName(), "challenge");


            newChallenge.setSenderAddress(getIpAddress());


            FirebaseDatabase.getInstance()
                    .getReference().child("messages")
                    .child(newChallenge.getMessageTime() + "Challenge")
                    .setValue(newChallenge);

            //SWITCH VIEW THEN CREATE LISTENER THREAD
            Intent intent = new Intent(getBaseContext(),TicTacToeGame.class);
            intent.putExtra("type","server");
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.menu_create_challenge && gameInSession) {
            Toast.makeText(this,
                    "Game Room Busy!",
                    Toast.LENGTH_LONG)
                    .show();
        }
        else if(item.getItemId() == R.id.menu_sign_out) {
            AuthUI.getInstance().signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(MainActivity.this,
                                    "You have been signed out.",
                                    Toast.LENGTH_LONG)
                                    .show();

                            // Close activity
                            finish();
                        }
                    });
        }
        return true;
    }


    public void startGame(View view) {

        Intent intent = new Intent(getBaseContext(),TicTacToeGame.class);
        intent.putExtra("type","client");
        intent.putExtra("ip", ip);
        startActivity(intent);
    }

    private String getIpAddress() {
        String ip = "";
        try {
            Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface
                    .getNetworkInterfaces();
            while (enumNetworkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = enumNetworkInterfaces
                        .nextElement();
                Enumeration<InetAddress> enumInetAddress = networkInterface
                        .getInetAddresses();
                while (enumInetAddress.hasMoreElements()) {
                    InetAddress inetAddress = enumInetAddress.nextElement();

                    if (inetAddress.isSiteLocalAddress()) {
                        ip = inetAddress.getHostAddress();
                    }

                }

            }

        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            ip += "Something Wrong! " + e.toString() + "\n";
        }

        return ip;
    }

}
