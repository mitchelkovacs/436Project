package com.example.mitchel.a436project;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.MenuItem;
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
    Boolean gameOver = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tic_tac_toe_game);
        Intent intent = getIntent();
        String type = intent.getStringExtra("type");
        if (type.equals("server")) {
            turn(false, game);
            findViewById(R.id.textView).setBackgroundColor(0x00000000);
            FirebaseDatabase.getInstance().getReference().child("buttons").child("Winner").child("messageText").setValue(" ");
            marker = "X";
            x = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
            o = "";
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
            turn(true, game);
            findViewById(R.id.textView2).setBackgroundColor(0x00000000);
        }

        FirebaseDatabase.getInstance().getReference().child("buttons").child("Winner").child("messageText").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TextView win = (TextView) findViewById(R.id.textView3);
                win.setText(dataSnapshot.getValue(String.class));
                if (!Objects.equals(dataSnapshot.getValue(String.class), " ")) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            finish();
                        }
                    }, 3000); //player has either left or won the game, give 3 seconds to look at display message then kick players
                }
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
                if((Objects.equals("X", dataSnapshot.getValue(String.class)) && marker == "X") || (Objects.equals("O", dataSnapshot.getValue(String.class)) && marker == "O")){
                    turn(false, game);
                }
                else if((Objects.equals("O", dataSnapshot.getValue(String.class)) && marker == "X") || (Objects.equals("X", dataSnapshot.getValue(String.class)) && marker == "O")){
                    turn(true, game);
                }
                if (!Objects.equals(" ", dataSnapshot.getValue(String.class))) {
                    button1.setClickable(false);
                    game[0][0] = dataSnapshot.getValue(String.class);
                    if ((Objects.equals(winner(game), "X")) && (x != "")) {
                        FirebaseDatabase.getInstance().getReference().child("buttons").child("Winner").child("messageText").setValue(x + " Wins!");
                    }
                    else if ((Objects.equals(winner(game), "O")) && (o != "")){
                        FirebaseDatabase.getInstance().getReference().child("buttons").child("Winner").child("messageText").setValue(o + " Wins!");
                    }
                    else if (Objects.equals(winner(game), "Draw")) {
                        FirebaseDatabase.getInstance().getReference().child("buttons").child("Winner").child("messageText").setValue("Draw");
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
                if((Objects.equals("X", dataSnapshot.getValue(String.class)) && marker == "X") || (Objects.equals("O", dataSnapshot.getValue(String.class)) && marker == "O")){
                    turn(false, game);
                }
                else if((Objects.equals("O", dataSnapshot.getValue(String.class)) && marker == "X") || (Objects.equals("X", dataSnapshot.getValue(String.class)) && marker == "O")){
                    turn(true, game);
                }
                if (!Objects.equals(" ", dataSnapshot.getValue(String.class))) {
                    button2.setClickable(false);
                    game[0][1] = dataSnapshot.getValue(String.class);
                    if ((Objects.equals(winner(game), "X")) && (x != "")) {
                        FirebaseDatabase.getInstance().getReference().child("buttons").child("Winner").child("messageText").setValue(x + " Wins!");
                    }
                    else if ((Objects.equals(winner(game), "O")) && (o != "")){
                        FirebaseDatabase.getInstance().getReference().child("buttons").child("Winner").child("messageText").setValue(o + " Wins!");
                    }
                    else if (Objects.equals(winner(game), "Draw")) {
                        FirebaseDatabase.getInstance().getReference().child("buttons").child("Winner").child("messageText").setValue("Draw");
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
                if((Objects.equals("X", dataSnapshot.getValue(String.class)) && marker == "X") || (Objects.equals("O", dataSnapshot.getValue(String.class)) && marker == "O")){
                    turn(false, game);
                }
                else if((Objects.equals("O", dataSnapshot.getValue(String.class)) && marker == "X") || (Objects.equals("X", dataSnapshot.getValue(String.class)) && marker == "O")){
                    turn(true, game);
                }
                if (!Objects.equals(" ", dataSnapshot.getValue(String.class))) {
                    button3.setClickable(false);
                    game[0][2] = dataSnapshot.getValue(String.class);
                    if ((Objects.equals(winner(game), "X")) && (x != "")) {
                        FirebaseDatabase.getInstance().getReference().child("buttons").child("Winner").child("messageText").setValue(x + " Wins!");
                    }
                    else if ((Objects.equals(winner(game), "O")) && (o != "")){
                        FirebaseDatabase.getInstance().getReference().child("buttons").child("Winner").child("messageText").setValue(o + " Wins!");
                    }
                    else if (Objects.equals(winner(game), "Draw")) {
                        FirebaseDatabase.getInstance().getReference().child("buttons").child("Winner").child("messageText").setValue("Draw");
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
                if((Objects.equals("X", dataSnapshot.getValue(String.class)) && marker == "X") || (Objects.equals("O", dataSnapshot.getValue(String.class)) && marker == "O")){
                    turn(false, game);
                }
                else if((Objects.equals("O", dataSnapshot.getValue(String.class)) && marker == "X") || (Objects.equals("X", dataSnapshot.getValue(String.class)) && marker == "O")){
                    turn(true, game);
                }
                if (!Objects.equals(" ", dataSnapshot.getValue(String.class))) {
                    button4.setClickable(false);
                    game[1][0] = dataSnapshot.getValue(String.class);
                    if ((Objects.equals(winner(game), "X")) && (x != "")) {
                        FirebaseDatabase.getInstance().getReference().child("buttons").child("Winner").child("messageText").setValue(x + " Wins!");
                    }
                    else if ((Objects.equals(winner(game), "O")) && (o != "")){
                        FirebaseDatabase.getInstance().getReference().child("buttons").child("Winner").child("messageText").setValue(o + " Wins!");
                    }
                    else if (Objects.equals(winner(game), "Draw")) {
                        FirebaseDatabase.getInstance().getReference().child("buttons").child("Winner").child("messageText").setValue("Draw");
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
                if((Objects.equals("X", dataSnapshot.getValue(String.class)) && marker == "X") || (Objects.equals("O", dataSnapshot.getValue(String.class)) && marker == "O")){
                    turn(false, game);
                }
                else if((Objects.equals("O", dataSnapshot.getValue(String.class)) && marker == "X") || (Objects.equals("X", dataSnapshot.getValue(String.class)) && marker == "O")){
                    turn(true, game);
                }
                if (!Objects.equals(" ", dataSnapshot.getValue(String.class))) {
                    button5.setClickable(false);
                    game[1][1] = dataSnapshot.getValue(String.class);
                    if ((Objects.equals(winner(game), "X")) && (x != "")) {
                        FirebaseDatabase.getInstance().getReference().child("buttons").child("Winner").child("messageText").setValue(x + " Wins!");
                    }
                    else if ((Objects.equals(winner(game), "O")) && (o != "")){
                        FirebaseDatabase.getInstance().getReference().child("buttons").child("Winner").child("messageText").setValue(o + " Wins!");
                    }
                    else if (Objects.equals(winner(game), "Draw")) {
                        FirebaseDatabase.getInstance().getReference().child("buttons").child("Winner").child("messageText").setValue("Draw");
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
                if((Objects.equals("X", dataSnapshot.getValue(String.class)) && marker == "X") || (Objects.equals("O", dataSnapshot.getValue(String.class)) && marker == "O")){
                    turn(false, game);
                }
                else if((Objects.equals("O", dataSnapshot.getValue(String.class)) && marker == "X") || (Objects.equals("X", dataSnapshot.getValue(String.class)) && marker == "O")){
                    turn(true, game);
                }
                if (!Objects.equals(" ", dataSnapshot.getValue(String.class))) {
                    button6.setClickable(false);
                    game[1][2] = dataSnapshot.getValue(String.class);
                    if ((Objects.equals(winner(game), "X")) && (x != "")) {
                        FirebaseDatabase.getInstance().getReference().child("buttons").child("Winner").child("messageText").setValue(x + " Wins!");
                    }
                    else if ((Objects.equals(winner(game), "O")) && (o != "")){
                        FirebaseDatabase.getInstance().getReference().child("buttons").child("Winner").child("messageText").setValue(o + " Wins!");
                    }
                    else if (Objects.equals(winner(game), "Draw")) {
                        FirebaseDatabase.getInstance().getReference().child("buttons").child("Winner").child("messageText").setValue("Draw");
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
                if((Objects.equals("X", dataSnapshot.getValue(String.class)) && marker == "X") || (Objects.equals("O", dataSnapshot.getValue(String.class)) && marker == "O")){
                    turn(false, game);
                }
                else if((Objects.equals("O", dataSnapshot.getValue(String.class)) && marker == "X") || (Objects.equals("X", dataSnapshot.getValue(String.class)) && marker == "O")){
                    turn(true, game);
                }
                if (!Objects.equals(" ", dataSnapshot.getValue(String.class))) {
                    button7.setClickable(false);
                    game[2][0] = dataSnapshot.getValue(String.class);
                    if ((Objects.equals(winner(game), "X")) && (x != "")) {
                        FirebaseDatabase.getInstance().getReference().child("buttons").child("Winner").child("messageText").setValue(x + " Wins!");
                    }
                    else if ((Objects.equals(winner(game), "O")) && (o != "")){
                        FirebaseDatabase.getInstance().getReference().child("buttons").child("Winner").child("messageText").setValue(o + " Wins!");
                    }
                    else if (Objects.equals(winner(game), "Draw")) {
                        FirebaseDatabase.getInstance().getReference().child("buttons").child("Winner").child("messageText").setValue("Draw");
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
                if((Objects.equals("X", dataSnapshot.getValue(String.class)) && marker == "X") || (Objects.equals("O", dataSnapshot.getValue(String.class)) && marker == "O")){
                    turn(false, game);
                }
                else if((Objects.equals("O", dataSnapshot.getValue(String.class)) && marker == "X") || (Objects.equals("X", dataSnapshot.getValue(String.class)) && marker == "O")){
                    turn(true, game);
                }
                if (!Objects.equals(" ", dataSnapshot.getValue(String.class))) {
                    button8.setClickable(false);
                    game[2][1] = dataSnapshot.getValue(String.class);
                    if ((Objects.equals(winner(game), "X")) && (x != "")) {
                        FirebaseDatabase.getInstance().getReference().child("buttons").child("Winner").child("messageText").setValue(x + " Wins!");
                    }
                    else if ((Objects.equals(winner(game), "O")) && (o != "")){
                        FirebaseDatabase.getInstance().getReference().child("buttons").child("Winner").child("messageText").setValue(o + " Wins!");
                    }
                    else if (Objects.equals(winner(game), "Draw")) {
                        FirebaseDatabase.getInstance().getReference().child("buttons").child("Winner").child("messageText").setValue("Draw");
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
                if((Objects.equals("X", dataSnapshot.getValue(String.class)) && marker == "X") || (Objects.equals("O", dataSnapshot.getValue(String.class)) && marker == "O")){
                    turn(false, game);
                }
                else if((Objects.equals("O", dataSnapshot.getValue(String.class)) && marker == "X") || (Objects.equals("X", dataSnapshot.getValue(String.class)) && marker == "O")){
                    turn(true, game);
                }
                if (!Objects.equals(" ", dataSnapshot.getValue(String.class))) {
                    button9.setClickable(false);
                    game[2][2] = dataSnapshot.getValue(String.class);
                    if ((Objects.equals(winner(game), "X")) && (x != "")) {
                        FirebaseDatabase.getInstance().getReference().child("buttons").child("Winner").child("messageText").setValue(x + " Wins!");
                    }
                    else if ((Objects.equals(winner(game), "O")) && (o != "")){
                        FirebaseDatabase.getInstance().getReference().child("buttons").child("Winner").child("messageText").setValue(o + " Wins!");
                    }
                    else if (Objects.equals(winner(game), "Draw")) {
                        FirebaseDatabase.getInstance().getReference().child("buttons").child("Winner").child("messageText").setValue("Draw");
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
//
//        } catch(Exception e) {
//            //TODO: handle exception
//        }
    }

    public void turn(boolean turn, String gameState[][]) {
        if(turn){
            findViewById(R.id.textView2).setBackgroundColor(getResources().getColor(R.color.tw__composer_red));
            findViewById(R.id.textView).setBackgroundColor(0x00000000);
        }
        else{
            findViewById(R.id.textView).setBackgroundColor(getResources().getColor(R.color.tw__composer_red));
            findViewById(R.id.textView2).setBackgroundColor(0x00000000);
        }

        if (Objects.equals(gameState[0][0], " ")) {
            findViewById(R.id.Button00).setClickable(turn);
        }
        if (Objects.equals(gameState[0][1], " ")) {
            findViewById(R.id.Button01).setClickable(turn);
        }
        if (Objects.equals(gameState[0][2], " ")) {
            findViewById(R.id.Button02).setClickable(turn);
        }
        if (Objects.equals(gameState[1][0], " ")) {
            findViewById(R.id.Button10).setClickable(turn);
        }
        if (Objects.equals(gameState[1][1], " ")) {
            findViewById(R.id.Button11).setClickable(turn);
        }
        if (Objects.equals(gameState[1][2], " ")) {
            findViewById(R.id.Button12).setClickable(turn);
        }
        if (Objects.equals(gameState[2][0], " ")) {
            findViewById(R.id.Button20).setClickable(turn);
        }
        if (Objects.equals(gameState[2][1], " ")) {
            findViewById(R.id.Button21).setClickable(turn);
        }
        if (Objects.equals(gameState[2][2], " ")) {
            findViewById(R.id.Button22).setClickable(turn);
        }
    }

    public String winner(String gameState[][]) {
        //first check all rows
        String curr = "";

        if (gameState[0][0] != " ") {
            curr = gameState[0][0];
            for (int i = 0; i < 3; i++) {
                if (gameState[0][i] != curr) {
                    curr = "";
                    break;
                }
                else if (i == 2 && gameState[0][i] == curr) {
                    //top row has a winner
                    gameOver = true;
                    return curr;
                }
            }
        }
        if (gameState[1][0] != " ") {
            curr = gameState[1][0];
            for (int i = 0; i < 3; i++) {
                if (gameState[1][i] != curr) {
                    curr = "";
                    break;
                }
                else if (i == 2 && gameState[1][i] == curr) {
                    //middle row has a winner
                    gameOver = true;
                    return curr;
                }
            }
        }
        if (gameState[2][0] != " ") {
            curr = gameState[2][0];
            for (int i = 0; i < 3; i++) {
                if (gameState[2][i] != curr) {
                    curr = "";
                    break;
                }
                else if (i == 2 && gameState[2][i] == curr) {
                    //bottom row has a winner
                    gameOver = true;
                    return curr;
                }
            }
        }


        //check columns
        if (gameState[0][0] != " ") {
            curr = gameState[0][0];
            for (int i = 0; i < 3; i++) {
                if (gameState[i][0] != curr) {
                    curr = "";
                    break;
                }
                else if (i == 2 && gameState[i][0] == curr) {
                    //first column has a winner
                    gameOver = true;
                    return curr;
                }
            }
        }
        if (gameState[0][1] != " ") {
            curr = gameState[0][1];
            for (int i = 0; i < 3; i++) {
                if (gameState[i][1] != curr) {
                    curr = "";
                    break;
                }
                else if (i == 2 && gameState[i][1] == curr) {
                    //middle column has a winner
                    gameOver = true;
                    return curr;
                }
            }
        }
        if (gameState[0][2] != " ") {
            curr = gameState[0][2];
            for (int i = 0; i < 3; i++) {
                if (gameState[i][2] != curr) {
                    curr = "";
                    break;
                }
                else if (i == 2 && gameState[i][2] == curr) {
                    //last column has a winner
                    gameOver = true;
                    return curr;
                }
            }
        }

        //check diagonals
        if (gameState[0][0] != " ") {
            curr = gameState[0][0];
            for (int i = 0; i < 3; i++) {
                if (gameState[i][i] != curr) {
                    curr = "";
                    break;
                }
                else if (i == 2 && gameState[i][i] == curr) {
                    //top left to bottom right has a winner
                    gameOver = true;
                    return curr;
                }
            }
        }
        if (gameState[0][2] != " ") {
            curr = gameState[0][1];
            for (int i = 0; i < 3; i++) {
                if (gameState[0 + i][2 - i] != curr) {
                    curr = "";
                    break;
                }
                else if (i == 2 && gameState[0 + i][2 - i] == curr) {
                    //top right to bottom left has a winner
                    gameOver = true;
                    return curr;
                }
            }
        }

        if(curr == "" && gameOver == false)
        {
            curr = "Draw";

            //check for draw
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (Objects.equals(gameState[i][j], " ")) {
                        curr = "";
                        break;
                    }
                }
            }
        }
        return curr;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) // Press Back Icon
        {
            FirebaseDatabase.getInstance().getReference().child("buttons").child("Winner").child("messageText").setValue("Game Terminated");
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            FirebaseDatabase.getInstance().getReference().child("buttons").child("Winner").child("messageText").setValue("Game Terminated");
            finish();
        }
        return super.onKeyDown(keyCode, event);
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
