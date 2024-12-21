package com.example.tictac;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class TicTacActivity extends AppCompatActivity {

    SharedPreferences stats;
    boolean withBot;
    char[] board;
    char currentPlayer;
    TextView tvStatus;
    Button[] buttons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tic_tac);

        stats = getSharedPreferences("TicTacToePrefs", MODE_PRIVATE);
        withBot = getIntent().getBooleanExtra("withBot", false);
        board = new char[9];
        currentPlayer = 'X';
        tvStatus = findViewById(R.id.tvStatus);
        buttons = new Button[9];

        for (int i = 0; i < 9; i++) {
            String buttonID = "btn_" + i;
            int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
            buttons[i] = findViewById(resID);
            buttons[i].setOnClickListener(new ButtonClickListener(i));
        }

        updateStatus();
    }

    private void updateStatus() {
        tvStatus.setText("Игрок " + currentPlayer + "ходит");
    }

    private void makeMove(int position) {
        if (board[position] == 0) {
            board[position] = currentPlayer;
            buttons[position].setText(String.valueOf(currentPlayer));
            if (checkWin()) {
                updateStats(true);
                finish();
            } else if (isDraw()) {
                updateStats(false);
                finish();
            } else {
                currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
                updateStatus();
                if (withBot && currentPlayer == 'O') {
                    makeBotMove();
                }
            }
        }
    }

    private void makeBotMove() {
        int position = -1;
        do {
            position = (int) (Math.random() * 9);
        } while (board[position] != 0);
        makeMove(position);
    }

    private boolean checkWin() {
        return (board[0] == currentPlayer && board[1] == currentPlayer && board[2] == currentPlayer) ||
                (board[3] == currentPlayer && board[4] == currentPlayer && board[5] == currentPlayer) ||
                (board[6] == currentPlayer && board[7] == currentPlayer && board[8] == currentPlayer) ||
                (board[0] == currentPlayer && board[3] == currentPlayer && board[6] == currentPlayer) ||
                (board[1] == currentPlayer && board[4] == currentPlayer && board[7] == currentPlayer) ||
                (board[2] == currentPlayer && board[5] == currentPlayer && board[8] == currentPlayer) ||
                (board[0] == currentPlayer && board[4] == currentPlayer && board[8] == currentPlayer) ||
                (board[2] == currentPlayer && board[4] == currentPlayer && board[6] == currentPlayer);
    }

    private boolean isDraw() {
        for (char cell : board) {
            if (cell == 0) {
                return false;
            }
        }
        return true;
    }

    private void updateStats(boolean isWin) {
        SharedPreferences.Editor editor = stats.edit();
        int wins = stats.getInt("wins", 0);
        int losses = stats.getInt("losses", 0);
        int draws = stats.getInt("draws", 0);

        if (isWin) {
            if (currentPlayer == 'X') {
                editor.putInt("wins", wins + 1);
            } else {
                editor.putInt("losses", losses + 1);
            }
        } else {
            editor.putInt("draws", draws + 1);
        }

        editor.apply();
    }

    private class ButtonClickListener implements View.OnClickListener {
        private int position;

        public ButtonClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            makeMove(position);
        }
    }
}