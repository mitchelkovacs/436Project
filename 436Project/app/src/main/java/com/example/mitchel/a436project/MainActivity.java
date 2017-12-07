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
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.Objects;
import java.net.DatagramSocket;


@TargetApi(19)
public class MainActivity extends AppCompatActivity {
    static final int SIGN_IN_REQUEST_CODE = 1;
    FirebaseListAdapter<ChatMessage> adapter = null;
    String ip;
    String type = "";

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

                // Read the input field and push a new instance
                // of ChatMessage to the Firebase database
                FirebaseDatabase.getInstance()
                        .getReference()
                        .push()
                        .setValue(new ChatMessage(input.getText().toString(),
                                FirebaseAuth.getInstance()
                                        .getCurrentUser()
                                        .getDisplayName(), "chat")
                        );

                // Clear the input
                input.setText("");
            }
        });

    }

    private void displayMessages() {
        displayChatMessages();
    }

    private void displayChatMessages() {
        ListView chatMessages = (ListView) findViewById(R.id.list_of_chat_messages);

        adapter = new FirebaseListAdapter<ChatMessage>(this, ChatMessage.class, R.layout.message,
                FirebaseDatabase.getInstance().getReference()) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                // Get references to the views of message
                TextView messageText = (TextView) v.findViewById(R.id.message_text);
                TextView messageUser = (TextView) v.findViewById(R.id.message_user);
                TextView messageTime = (TextView) v.findViewById(R.id.message_time);
                Button acceptButton = (Button) v.findViewById(R.id.button_challenge);
                // Set their text
                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());

                String currType= model.getMessageType();

                if(Objects.equals("challenge",currType))
                {
                    acceptButton.setText("Accept Challenge");
                    acceptButton.setVisibility(View.VISIBLE);
                    acceptButton.setHint(model.getSenderAddress());
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
        if(item.getItemId() == R.id.menu_create_challenge) {
            ChatMessage newChallenge = new ChatMessage(FirebaseAuth.getInstance()
                    .getCurrentUser()
                    .getDisplayName() + " created an OPEN CHALLENGE in TIC-TAC-TOE!",
                    FirebaseAuth.getInstance()
                            .getCurrentUser()
                            .getDisplayName(), "challenge");

            try {
//                final DatagramSocket socket = new DatagramSocket();
//                socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
//                ip = socket.getLocalAddress().getHostAddress();
//                WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
//                String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());

            } catch (Exception e) {

            }

            newChallenge.setSenderAddress("localhost");

            FirebaseDatabase.getInstance()
                    .getReference()
                    .push()
                    .setValue(newChallenge);

            //CREATE LISTENER THREAD
            Receiver receiver = new Receiver();

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
        Button acceptButton = (Button) view;
        String userReceiver = FirebaseAuth.getInstance()
                .getCurrentUser()
                .getDisplayName();

        ip = (String)acceptButton.getHint();

        acceptButton.setClickable(false);
        acceptButton.setText((CharSequence)"Challenge Accepted by" + userReceiver);


       //PUT SOCKETS HERE

        //tell sender to create game and create game for acceptor
        try {
            System.out.println("About to create client socket. Host:" + ip);

            Socket clientSocket = new Socket("localhost", 8080);

            System.out.println("Created client socket.");

            clientSocket.close();

            System.out.println("Closed client socket.");

            Intent intent = new Intent(getBaseContext(),TicTacToeGame.class);
            intent.putExtra("type","client");
            intent.putExtra("ip", ip);
            startActivity(intent);
        } catch(Exception e) {
            //TODO: handle exceptions
        }

        acceptButton.setText((CharSequence)"Challenge Completed");
    }

    /*Receiver - Runnable thread that listens for connections to game
	 */
    class Receiver implements Runnable {
        Thread thread;
        volatile boolean exit;

        /*Receiver(memberId, msgConn) - Initializes receiver and starts receivers thread
         *	Inputs:
         *		None
         *	Outputs:
         *		None
         */
        public Receiver() {
            thread = new Thread(this);
            exit = false;

            thread.start();
        }

        /*run() - Initializes receiver and starts receivers thread
         *	Inputs:
         *		None
         *	Outputs:
         *		Prints chatroom messages to std:out
         */
        @Override
        public void run(){
            ServerSocket senderSocket = null;
            try {
                senderSocket = new ServerSocket(8080);
                System.out.println("Reached creating server socket, Address:" + ip);

                Socket connectionSocket = senderSocket.accept();

                System.out.println("Reached connecting to client");

                //play game here
                senderSocket.close();

                Intent intent = new Intent(getBaseContext(),TicTacToeGame.class);
                intent.putExtra("type","server");
                startActivity(intent);

                //After game, close connection
                senderSocket.close();
            } catch (Exception e) {
                //TODO: Handle exceptions

            }
        }

        /*stop() - Changes receiver.exit to true to close receiver thread
         *	Inputs:
         *		None
         *	Outputs:
         *		receiver.exit is set to true
         */
        public void stop(){
            exit = true;
        }
    }
}
