package com.example.mitchel.a436project;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class TicTacToeGame extends AppCompatActivity {
//    BufferedReader opponentMove;
//    DataOutputStream userMove;

    String marker = " ";
    public static boolean gameInSession = false;
    String game[][] = new String[3][3];
    String x, o;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tic_tac_toe_game);
        Intent intent = getIntent();
        String type = intent.getStringExtra("type");
        if (type.equals("server")) {
            FirebaseDatabase.getInstance().getReference().child("buttons").child("Winner").child("messageText").setValue(" ");
            marker = "X";
            x = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
            o= "";
            for (int i = 1; i < 10; i++) {
                FirebaseDatabase.getInstance()
                        .getReference().child("buttons")
                        .child("button" + i)
                        .setValue(new ChatMessage(" ",
                                String.valueOf(i), "game")
                        );
            }
        } else {
            marker = "O";
            o = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
            x = "";
            findViewById(R.id.Button00).setClickable(true);
            findViewById(R.id.Button01).setClickable(true);
            findViewById(R.id.Button02).setClickable(true);
            findViewById(R.id.Button10).setClickable(true);
            findViewById(R.id.Button11).setClickable(true);
            findViewById(R.id.Button12).setClickable(true);
            findViewById(R.id.Button20).setClickable(true);
            findViewById(R.id.Button21).setClickable(true);
            findViewById(R.id.Button22).setClickable(true);
        }

        FirebaseDatabase.getInstance().getReference().child("buttons").child("Winner").child("messageText").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TextView win = (TextView)findViewById(R.id.textView3);
                win.setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        final Button button1 = (Button) findViewById(R.id.Button00);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance()
                        .getReference().child("buttons")
                        .child("button1")
                        .setValue(new ChatMessage(marker,
                                FirebaseAuth.getInstance()
                                        .getCurrentUser()
                                        .getDisplayName(), "game")
                        );
                button1.setClickable(false);
                button1.setText(marker);
            }
        });
        FirebaseDatabase.getInstance().getReference().child("buttons").child("button1").child("messageText").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                button1.setText(dataSnapshot.getValue(String.class));
                if (!Objects.equals(" ", dataSnapshot.getValue(String.class))) {
                    button1.setClickable(false);
                    game[0][0] = dataSnapshot.getValue(String.class);
                    if (Objects.equals(winner(game), "X")) {
                        if(x != "") {
                            FirebaseDatabase.getInstance().getReference().child("buttons").child("Winner").child("messageText").setValue(x + " Wins!");
                        }
                    }
                    if (Objects.equals(winner(game), "O")) {
                        if(o != "") {
                            FirebaseDatabase.getInstance().getReference().child("buttons").child("Winner").child("messageText").setValue(o + " Wins!");
                        }
                    }
                } else {
                    button1.setClickable(true);
                    game[0][0] = dataSnapshot.getValue(String.class);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        final Button button2 = (Button) findViewById(R.id.Button01);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance()
                        .getReference().child("buttons")
                        .child("button2")
                        .setValue(new ChatMessage(marker,
                                FirebaseAuth.getInstance()
                                        .getCurrentUser()
                                        .getDisplayName(), "game")
                        );
                button2.setClickable(false);
                button2.setText(marker);
            }
        });
        FirebaseDatabase.getInstance().getReference().child("buttons").child("button2").child("messageText").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                button2.setText(dataSnapshot.getValue(String.class));
                if (!Objects.equals(" ", dataSnapshot.getValue(String.class))) {
                    button2.setClickable(false);
                    game[0][1] = dataSnapshot.getValue(String.class);
                    if (Objects.equals(winner(game), "X")) {
                        if(x != "") {
                            FirebaseDatabase.getInstance().getReference().child("buttons").child("Winner").child("messageText").setValue(x + " Wins!");
                        }
                    }
                    if (Objects.equals(winner(game), "O")) {
                        if(o != "") {
                            FirebaseDatabase.getInstance().getReference().child("buttons").child("Winner").child("messageText").setValue(o + " Wins!");
                        }
                    }
                } else {
                    button2.setClickable(true);
                    game[0][1] = dataSnapshot.getValue(String.class);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        final Button button3 = (Button) findViewById(R.id.Button02);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance()
                        .getReference().child("buttons")
                        .child("button3")
                        .setValue(new ChatMessage(marker,
                                FirebaseAuth.getInstance()
                                        .getCurrentUser()
                                        .getDisplayName(), "game")
                        );
                button3.setClickable(false);
                button3.setText(marker);
            }
        });

        FirebaseDatabase.getInstance().getReference().child("buttons").child("button3").child("messageText").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                button3.setText(dataSnapshot.getValue(String.class));
                if (!Objects.equals(" ", dataSnapshot.getValue(String.class))) {
                    button3.setClickable(false);
                    game[0][2] = dataSnapshot.getValue(String.class);
                    if (Objects.equals(winner(game), "X")) {
                        if(x != "") {
                            FirebaseDatabase.getInstance().getReference().child("buttons").child("Winner").child("messageText").setValue(x + " Wins!");
                        }
                    }
                    if (Objects.equals(winner(game), "O")) {
                        if(o != "") {
                            FirebaseDatabase.getInstance().getReference().child("buttons").child("Winner").child("messageText").setValue(o + " Wins!");
                        }
                    }
                } else {
                    button3.setClickable(true);
                    game[0][2] = dataSnapshot.getValue(String.class);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        final Button button4 = (Button) findViewById(R.id.Button10);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance()
                        .getReference().child("buttons")
                        .child("button4")
                        .setValue(new ChatMessage(marker,
                                FirebaseAuth.getInstance()
                                        .getCurrentUser()
                                        .getDisplayName(), "game")
                        );
                button4.setClickable(false);
                button4.setText(marker);
            }
        });

        FirebaseDatabase.getInstance().getReference().child("buttons").child("button4").child("messageText").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                button4.setText(dataSnapshot.getValue(String.class));
                if (!Objects.equals(" ", dataSnapshot.getValue(String.class))) {
                    button4.setClickable(false);
                    game[1][0] = dataSnapshot.getValue(String.class);
                    if (Objects.equals(winner(game), "X")) {
                        if(x != "") {
                            FirebaseDatabase.getInstance().getReference().child("buttons").child("Winner").child("messageText").setValue(x + " Wins!");
                        }
                    }
                    if (Objects.equals(winner(game), "O")) {
                        if(o != "") {
                            FirebaseDatabase.getInstance().getReference().child("buttons").child("Winner").child("messageText").setValue(o + " Wins!");
                        }
                    }
                } else {
                    button4.setClickable(true);
                    game[1][0] = dataSnapshot.getValue(String.class);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        final Button button5 = (Button) findViewById(R.id.Button11);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance()
                        .getReference().child("buttons")
                        .child("button5")
                        .setValue(new ChatMessage(marker,
                                FirebaseAuth.getInstance()
                                        .getCurrentUser()
                                        .getDisplayName(), "game")
                        );
                button5.setClickable(false);
                button5.setText(marker);
            }
        });

        FirebaseDatabase.getInstance().getReference().child("buttons").child("button5").child("messageText").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                button5.setText(dataSnapshot.getValue(String.class));
                if (!Objects.equals(" ", dataSnapshot.getValue(String.class))) {
                    button5.setClickable(false);
                    game[1][1] = dataSnapshot.getValue(String.class);
                    if (Objects.equals(winner(game), "X")) {
                        if(x != "") {
                            FirebaseDatabase.getInstance().getReference().child("buttons").child("Winner").child("messageText").setValue(x + " Wins!");
                        }
                    }
                    if (Objects.equals(winner(game), "O")) {
                        if(o != "") {
                            FirebaseDatabase.getInstance().getReference().child("buttons").child("Winner").child("messageText").setValue(o + " Wins!");
                        }
                    }
                } else {
                    button5.setClickable(true);
                    game[1][1] = dataSnapshot.getValue(String.class);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        final Button button6 = (Button) findViewById(R.id.Button12);
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance()
                        .getReference().child("buttons")
                        .child("button6")
                        .setValue(new ChatMessage(marker,
                                FirebaseAuth.getInstance()
                                        .getCurrentUser()
                                        .getDisplayName(), "game")
                        );
                button6.setClickable(false);
                button6.setText(marker);
            }
        });

        FirebaseDatabase.getInstance().getReference().child("buttons").child("button6").child("messageText").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                button6.setText(dataSnapshot.getValue(String.class));
                if (!Objects.equals(" ", dataSnapshot.getValue(String.class))) {
                    button6.setClickable(false);
                    game[1][2] = dataSnapshot.getValue(String.class);
                    if (Objects.equals(winner(game), "X")) {
                        if(x != "") {
                            FirebaseDatabase.getInstance().getReference().child("buttons").child("Winner").child("messageText").setValue(x + " Wins!");
                        }
                    }
                    if (Objects.equals(winner(game), "O")) {
                        if(o != "") {
                            FirebaseDatabase.getInstance().getReference().child("buttons").child("Winner").child("messageText").setValue(o + " Wins!");
                        }
                    }
                } else {
                    button6.setClickable(true);
                    game[1][2] = dataSnapshot.getValue(String.class);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        final Button button7 = (Button) findViewById(R.id.Button20);
        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance()
                        .getReference().child("buttons")
                        .child("button7")
                        .setValue(new ChatMessage(marker,
                                FirebaseAuth.getInstance()
                                        .getCurrentUser()
                                        .getDisplayName(), "game")
                        );
                button7.setClickable(false);
                button7.setText(marker);
            }
        });

        FirebaseDatabase.getInstance().getReference().child("buttons").child("button7").child("messageText").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                button7.setText(dataSnapshot.getValue(String.class));
                if (!Objects.equals(" ", dataSnapshot.getValue(String.class))) {
                    button7.setClickable(false);
                    game[2][0] = dataSnapshot.getValue(String.class);
                    if (Objects.equals(winner(game), "X")) {
                        if(x != "") {
                            FirebaseDatabase.getInstance().getReference().child("buttons").child("Winner").child("messageText").setValue(x + " Wins!");
                        }
                    }
                    if (Objects.equals(winner(game), "O")) {
                        if(o != "") {
                            FirebaseDatabase.getInstance().getReference().child("buttons").child("Winner").child("messageText").setValue(o + " Wins!");
                        }
                    }
                } else {
                    button7.setClickable(true);
                    game[2][0] = dataSnapshot.getValue(String.class);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        final Button button8 = (Button) findViewById(R.id.Button21);
        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance()
                        .getReference().child("buttons")
                        .child("button8")
                        .setValue(new ChatMessage(marker,
                                FirebaseAuth.getInstance()
                                        .getCurrentUser()
                                        .getDisplayName(), "game")
                        );
                button8.setClickable(false);
                button8.setText(marker);
            }
        });

        FirebaseDatabase.getInstance().getReference().child("buttons").child("button8").child("messageText").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                button8.setText(dataSnapshot.getValue(String.class));
                if (!Objects.equals(" ", dataSnapshot.getValue(String.class))) {
                    button8.setClickable(false);
                    game[2][1] = dataSnapshot.getValue(String.class);
                    if (Objects.equals(winner(game), "X")) {
                        if(x != "") {
                            FirebaseDatabase.getInstance().getReference().child("buttons").child("Winner").child("messageText").setValue(x + " Wins!");
                        }
                    }
                    if (Objects.equals(winner(game), "O")) {
                        if(o != "") {
                            FirebaseDatabase.getInstance().getReference().child("buttons").child("Winner").child("messageText").setValue(o + " Wins!");
                        }
                    }
                } else {
                    button8.setClickable(true);
                    game[2][1] = dataSnapshot.getValue(String.class);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        final Button button9 = (Button) findViewById(R.id.Button22);
        button9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance()
                        .getReference().child("buttons")
                        .child("button9")
                        .setValue(new ChatMessage(marker,
                                FirebaseAuth.getInstance()
                                        .getCurrentUser()
                                        .getDisplayName(), "game")
                        );
                button9.setClickable(false);
                button9.setText(marker);
            }
        });

        FirebaseDatabase.getInstance().getReference().child("buttons").child("button9").child("messageText").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                button9.setText(dataSnapshot.getValue(String.class));
                if (!Objects.equals(" ", dataSnapshot.getValue(String.class))) {
                    button9.setClickable(false);
                    game[2][2] = dataSnapshot.getValue(String.class);
                    if (Objects.equals(winner(game), "X")) {
                        if(x != "") {
                            FirebaseDatabase.getInstance().getReference().child("buttons").child("Winner").child("messageText").setValue(x + " Wins!");
                        }
                    }
                    if (Objects.equals(winner(game), "O")) {
                        if(o != "") {
                            FirebaseDatabase.getInstance().getReference().child("buttons").child("Winner").child("messageText").setValue(o + " Wins!");
                        }
                    }
                } else {
                    button9.setClickable(true);
                    game[2][2] = dataSnapshot.getValue(String.class);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


//        FOR SOCKET CONNECTION
//        ServerSocket senderSocket = null;
//        Socket clientSocket = null;
//
//        try {
//            if(type.equals("server"))
//            {
//                senderSocket = new ServerSocket(9292);
//                Socket socket = senderSocket.accept();
//                //From client
//                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//                //To client
//                DataOutputStream writer = new DataOutputStream(socket.getOutputStream());
//
//
//                //play game here
//
//                opponentMove = reader;
//                userMove = writer;
//            } else {
//                String ip = intent.getStringExtra("ip");
//                clientSocket = new Socket(ip,9292);
//
//                //From server
//                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//                //To server
//                DataOutputStream writer = new DataOutputStream(clientSocket.getOutputStream());
//
//
//                //play game here
//
//                opponentMove = reader;
//                userMove = writer;
//
//
//            }
//
//            setContentView(R.layout.activity_tic_tac_toe_game);
//
//            userMove.writeChars("CONNECTION MADE. GAME STARTED");
//
//            String fromOpponent = opponentMove.readLine();
//
//            //Play game
//            //Choose a player to start game
//            //Call playGame function
//            clientSocket.close();
//
//        } catch(Exception e) {
//            //TODO: handle exception
//        }
    }


    public String winner(String gameState[][]) {
        //first check all rows
        if (gameState[0][0] != " ") {
            String curr = gameState[0][0];
            for (int i = 0; i < 3; i++) {
                if (gameState[0][i] != curr) {
                    break;
                }
                if (i == 2 && gameState[0][i] == curr) {
                    //top row has a winner
                    return curr;
                }
            }
        }
        if (gameState[1][0] != " ") {
            String curr = gameState[1][0];
            for (int i = 0; i < 3; i++) {
                if (gameState[1][i] != curr) {
                    break;
                }
                if (i == 2 && gameState[1][i] == curr) {
                    //middle row has a winner
                    return curr;
                }
            }
        }
        if (gameState[2][0] != " ") {
            String curr = gameState[2][0];
            for (int i = 0; i < 3; i++) {
                if (gameState[2][i] != curr) {
                    break;
                }
                if (i == 2 && gameState[2][i] == curr) {
                    //bottom row has a winner
                    return curr;
                }
            }
        }


        //check columns
        if (gameState[0][0] != " ") {
            String curr = gameState[0][0];
            for (int i = 0; i < 3; i++) {
                if (gameState[i][0] != curr) {
                    break;
                }
                if (i == 2 && gameState[i][0] == curr) {
                    //first column has a winner
                    return curr;
                }
            }
        }
        if (gameState[0][1] != " ") {
            String curr = gameState[0][1];
            for (int i = 0; i < 3; i++) {
                if (gameState[i][1] != curr) {
                    break;
                }
                if (i == 2 && gameState[i][1] == curr) {
                    //middle column has a winner
                    return curr;
                }
            }
        }
        if (gameState[0][2] != " ") {
            String curr = gameState[0][2];
            for (int i = 0; i < 3; i++) {
                if (gameState[i][2] != curr) {
                    break;
                }
                if (i == 2 && gameState[i][2] == curr) {
                    //last column has a winner
                    return curr;
                }
            }
        }

        //check diagonals
        if (gameState[0][0] != " ") {
            String curr = gameState[0][0];
            for (int i = 0; i < 3; i++) {
                if (gameState[i][i] != curr) {
                    break;
                }
                if (i == 2 && gameState[i][i] == curr) {
                    //top left to bottom right has a winner
                    return curr;
                }
            }
        }
        if (gameState[0][2] != " ") {
            String curr = gameState[0][1];
            for (int i = 0; i < 3; i++) {
                if (gameState[0 + i][2 - i] != curr) {
                    break;
                }
                if (i == 2 && gameState[0 + i][2 - i] == curr) {
                    //top right to bottom left has a winner
                    return curr;
                }
            }
        }


        //didnt return true above, so no winners yet
        return " ";
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
        public void run() {
            ServerSocket socket = null;
            try {
                socket = new ServerSocket(9292);
                Socket connectionSocket = socket.accept();
                System.out.println("Connecting to client");


            } catch (Exception e) {
                //TODO: Handle exceptions

            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        /*stop() - Changes receiver.exit to true to close receiver thread
         *	Inputs:
         *		None
         *	Outputs:
         *		receiver.exit is set to true
         */
        public void stop() {
            exit = true;
        }
    }

}
