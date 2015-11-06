package sudoku.example.com.database;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
//
//import sudoku.example.com.database.R;

/**
 * Created by C5839 on 06/11/2015.
 */
public class DatabaseActivity  extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
  //      setContentView(R.layout.activity_menu_levels);

        DatabaseHandler db = new DatabaseHandler(this);
//        db.deleteDatabase(this);


        Log.d("Insert: ", "Inserting ..");
        db.addBoard(new BoardDetails("000724000 003000700 206000810 960003082 810002095 300980070 704000150 000267000 009000200", "198724563 453816729 276539814 965173482 817642395 342985671 724398156 531267948 689451237", "latwy", null, null, 0));
        //   db.addBoard(new BoardDetails("Ravi", "143","latwy" ,"haha", "hgruiehgeir", 0));
//        db.addBoard(new BoardDetails("Srinivas", "9199999999", "trudny", "143", "haha", 0));
//        db.addBoard(new BoardDetails("Tommy", "9522222222", "latwy", "143", "haha", 1));
//        db.addBoard(new BoardDetails("Tommy", "9522222222", "trudny", null, "haha", 1));
//        db.addBoard(new BoardDetails("Karthik", "9533333333", "sredni", "143", null, 1));
//        db.addBoard(new BoardDetails("120321023212", "952222", "trudny", null, null, 1));
//        db.addBoard(new BoardDetails("0343", "95333", "trudny", null, null, 1));
        // Reading all contacts
        //    db.cleanDataBoard("Ravi");

        Log.d("Reading: ", "Reading all boards..");
        //   List<BoardDetails> boards = db.getAllBoards();
        //    List<BoardDetails> boards = db.getBoardsByDifficultyLevel("trudny");
        BoardDetails cn = db.getTheMostLeastBoardByDifficultyLevel("latwy");
        //     for (BoardDetails cn : boards) {
        String log = "Id: " + cn.getID() + " , " + cn.getVisibleDigits() + " , " + cn.getBoardSolution() + " , " + cn.getDifficultyLevel() + " , " + cn.getUserSolution()
                + " , " + cn.getUserSuggestions() + " ,   " + cn.getBoardUsed();
        // Writing Boards to log
        //     db.deleteContact(cn);
        Log.d("Name: ", log);

    }
}

