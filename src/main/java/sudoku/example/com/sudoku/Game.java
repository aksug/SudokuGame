package sudoku.example.com.sudoku;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class Game extends Activity {

    private String level;
    private Board board;
    private String TAG = "Game";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Bundle bundle = getIntent().getExtras();
        level = bundle.getString("level");
        Log.d(TAG, "in onCreate()");
        board = (Board) findViewById(R.id.boardView);

    }

    public void numberOne(View view) {
        board.setNumber(1);
        Toast.makeText(getApplicationContext(), "Wcisnieto 1", Toast.LENGTH_SHORT).show();

    }

    public void numberTwo(View view) {
        board.setNumber(2);
        Toast.makeText(getApplicationContext(), "Wcisnieto 2", Toast.LENGTH_SHORT).show();
    }

    public void numberThree(View view) {
        board.setNumber(3);
        Toast.makeText(getApplicationContext(), "Wcisnieto 3", Toast.LENGTH_SHORT).show();
    }

    public void numberFour(View view) {
        board.setNumber(4);
        Toast.makeText(getApplicationContext(), "Wcisnieto 4", Toast.LENGTH_SHORT).show();
    }

    public void numberFive(View view) {
        board.setNumber(5);
        Toast.makeText(getApplicationContext(), "Wcisnieto 5", Toast.LENGTH_SHORT).show();
    }

    public void numberSix(View view) {
        board.setNumber(6);
        Toast.makeText(getApplicationContext(), "Wcisnieto 6", Toast.LENGTH_SHORT).show();
    }

    public void numberSeven(View view) {
        board.setNumber(7);
        Toast.makeText(getApplicationContext(), "Wcisnieto 7", Toast.LENGTH_SHORT).show();
    }

    public void numberEight(View view) {
        board.setNumber(8);
        Toast.makeText(getApplicationContext(), "Wcisnieto 8", Toast.LENGTH_SHORT).show();
    }

    public void numberNine(View view) {
        board.setNumber(9);
        Toast.makeText(getApplicationContext(), "Wcisnieto 9", Toast.LENGTH_SHORT).show();
    }

    public void divideSquare(View view) {
        Toast.makeText(getApplicationContext(), "Podziel komorke", Toast.LENGTH_SHORT).show();
        board.divadeSamllSquere();
    }

    public void clear(View view) {
        board.backspace();
        Toast.makeText(getApplicationContext(), "Wcisnieto CLEAR", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //okienko czy zapisac gre czy nie TODO
        save();
    }

    public void saveGame(View view) {
        save();
    }

    private void save() {
        //TODO
    }

    public void endGame(View view) {
// przegladam odpowiedzi usera i wylapuje bledne
        board.checkSolution();
    }
}
