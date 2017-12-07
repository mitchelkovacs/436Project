package com.example.mitchel.a436project;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class TicTacToeGame extends AppCompatActivity {
    BufferedReader opponentMove;
    DataOutputStream userMove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tic_tac_toe_game);

        Intent intent = getIntent();
        String type = intent.getStringExtra("type");
        ServerSocket senderSocket = null;
        Socket clientSocket = null;
                
        try {
            if(type.equals("server"))
            {
                senderSocket = new ServerSocket(8080);

                clientSocket = senderSocket.accept();
                //From client
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                //To client
                DataOutputStream writer = new DataOutputStream(clientSocket.getOutputStream());
    
    
                //play game here
    
                opponentMove = reader;
                userMove = writer;
            } else {
                String ip = intent.getStringExtra("ip");
                clientSocket = new Socket(ip,8080);

                //From server
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                //To server
                DataOutputStream writer = new DataOutputStream(clientSocket.getOutputStream());


                //play game here

                opponentMove = reader;
                userMove = writer;


            }

            setContentView(R.layout.activity_tic_tac_toe_game);
            
            userMove.writeChars("CONNECTION MADE. GAME STARTED");

            String fromOpponent = opponentMove.readLine();

            //Play game
            //Choose a player to start game
            //Call playGame function
            clientSocket.close();

        } catch(Exception e) {
            //TODO: handle exception
        }
    }
}
