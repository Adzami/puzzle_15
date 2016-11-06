package com.micro_army.a15;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends Activity implements View.OnClickListener {
    private int[] board = new int[16]; // Our board
    private Button[] buttons = new Button[16]; // Visualization of board
    private int moves = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Initialize buttons
        buttons[0] = (Button) findViewById(R.id.b0);
        buttons[1] = (Button) findViewById(R.id.b1);
        buttons[2] = (Button) findViewById(R.id.b2);
        buttons[3] = (Button) findViewById(R.id.b3);
        buttons[4] = (Button) findViewById(R.id.b4);
        buttons[5] = (Button) findViewById(R.id.b5);
        buttons[6] = (Button) findViewById(R.id.b6);
        buttons[7] = (Button) findViewById(R.id.b7);
        buttons[8] = (Button) findViewById(R.id.b8);
        buttons[9] = (Button) findViewById(R.id.b9);
        buttons[10] = (Button) findViewById(R.id.b10);
        buttons[11] = (Button) findViewById(R.id.b11);
        buttons[12] = (Button) findViewById(R.id.b12);
        buttons[13] = (Button) findViewById(R.id.b13);
        buttons[14] = (Button) findViewById(R.id.b14);
        buttons[15] = (Button) findViewById(R.id.b15);
        // and set them same listener
        for (Button button : buttons){
            button.setOnClickListener(this);
        }
        // If we recreate activity, get data from Bundle
        if (savedInstanceState != null){
            board = savedInstanceState.getIntArray("board");
            moves = savedInstanceState.getInt("moves");
        } else {
            // Create and draw new board
            createBoard();
            drawBoard();
        }
    }

    private void createBoard(){ // Create and shuffle board on start
        // New sorted board
        for (int i = 0; i < 16; i++){
            board[i] = i;
        }
        // Shuffle it
        for (int i = 15; i >= 0; i--){
            int j = (int) (Math.random() * i);
            swap(board, j, i);
        }
        // It it's not solvable, swap any two neighbors
        if (!isSolvable()){
            swap(board, 0, 1);
        }
    }

    private void swap(int[] arr, int i, int j){ // Let's swap two numbers
        int t = arr[i];
        arr[i] = arr[j];
        arr[j] = t;
    }

    private void drawBoard(){
        // Set numbers on board
        for (int i = 0; i < 16; i++){
            if (board[i] != 0){
                buttons[i].setText(String.format(Locale.US, "%1$d", board[i]));
            } else {
                buttons[i].setText("");
            }
        }
        // Disable all buttons we can't press right now because it's not valid move
        int emptyPosition = getArrayIndex(board, 0);
        for (Button button : buttons){
            button.setEnabled(false);
        }
        if (emptyPosition - 1 > -1){
            buttons[emptyPosition - 1].setEnabled(true);
        }
        if (emptyPosition + 1 < 16){
            buttons[emptyPosition + 1].setEnabled(true);
        }
        if (emptyPosition - 4 > -1){
            buttons[emptyPosition - 4].setEnabled(true);
        }
        if (emptyPosition + 4 < 16){
            buttons[emptyPosition + 4].setEnabled(true);
        }
        if ((emptyPosition+1)%4 == 0 && (emptyPosition+1) < 16){
            buttons[emptyPosition + 1].setEnabled(false);
        }
        if (emptyPosition%4 == 0 && (emptyPosition-1) > -1){
            buttons[emptyPosition - 1].setEnabled(false);
        }
    }

    private boolean isSolvable(){ // We want only solvable boards
        boolean solvable = false;
        int result = 0;
        for (int i = 0; i < 16; i++){
            if (board[i] == 0){
                continue;
            }
            for (int j = i+1; j < 16; j++){
                if (board[j] == 0){
                    continue;
                }
                if (board[j] < board[i]){
                    result += 1;
                }
            }
        }
        result = result + ((getArrayIndex(board, 0) / 4)+1);
        // If it's even, board is solvable
        if (result%2 == 0){
            solvable = true;
        }
        return solvable;
    }

    private boolean checkVictory(){
        for (int i = 0; i < 15; i++){
            if (board[i] != (i+1)){
                return false;
            }
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        moves++;
        Button pressedButton = (Button) v;
        // This means "change on board element with index that have a pressed button value with empty cell
        swap(board, getArrayIndex(board, Integer.parseInt(pressedButton.getText().toString())), getArrayIndex(board, 0));
        if(checkVictory()){
            TextView result = (TextView) findViewById(R.id.result);
            result.setText("Congratulations! You've completed\nthis puzzle for " + moves + " moves");
            for (Button button : buttons){
                button.setEnabled(false);
            }
        }
        drawBoard();
    }

    public void onClickRestart(View view) {
        moves = 0;
        createBoard();
        drawBoard();
    }

    public int getArrayIndex(int[] arr,int value) { // Custom Array.indexOf(int value) method
        int k=0;
        for(int i=0;i<arr.length;i++){
            if(arr[i]==value){
                k=i;
                break;
            }
        }
        return k;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putIntArray("board", board);
        savedInstanceState.putInt("moves", moves);
    }
}
