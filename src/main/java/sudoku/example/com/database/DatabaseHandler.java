package sudoku.example.com.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "SudokuBoards";
    private static final String TABLE_NAME = "boards";
    // Table Columns names
    private static final String KEY_ID = "id";
    private static final String VISIBLE_DIGITS = "visible_digits";
    private static final String SOLUTION = "solution";
    private static final String DIFFICULTY_LEVEL = "level";
    private static final String USERS_SOLUTION = "users_solution";
    private static final String USERS_SUGGESTIONS = "users_suggestions";
    private static final String BOARD_WAS_USED = "used";
    private static final int DEFAULT_VALUE_BOARD_USED = 0;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Table
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_BOARDS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY, "
                + VISIBLE_DIGITS + " TEXT NOT NULL UNIQUE, " + SOLUTION + " TEXT NOT NULL, "
                + DIFFICULTY_LEVEL + " TEXT NOT NULL, " + USERS_SOLUTION + " TEXT, " + USERS_SUGGESTIONS + " TEXT, "
                + BOARD_WAS_USED + " INTEGER  CHECK ( " + BOARD_WAS_USED + " IN (0,1)) )";
        db.execSQL(CREATE_BOARDS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO check if it is working
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        // Create tables again
        onCreate(db);
    }

    // Adding new board
    public void addBoard(BoardDetails boardDetails) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(VISIBLE_DIGITS, boardDetails.getVisibleDigits());
        values.put(SOLUTION, boardDetails.getBoardSolution());
        values.put(DIFFICULTY_LEVEL, boardDetails.getDifficultyLevel());
        values.put(USERS_SOLUTION, boardDetails.getUserSolution());
        values.put(USERS_SUGGESTIONS, boardDetails.getUserSuggestions());
        values.put(BOARD_WAS_USED, DEFAULT_VALUE_BOARD_USED);

        // Inserting Row
        //dodaje elem., ale jesli istnieje to uaktualniam
        int id = (int) db.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        if (id == -1) {
            BoardDetails previousBoard = getBoardByVisibleDigits(boardDetails.getVisibleDigits());
            db.update(TABLE_NAME, values, KEY_ID + " = ?",
                    new String[]{String.valueOf(previousBoard.getID())});
        }
        db.close(); // Closing database connection
    }

    // Getting single board
    public BoardDetails getBoardByVisibleDigits(String start_digits) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[]{KEY_ID,
                        VISIBLE_DIGITS, SOLUTION, DIFFICULTY_LEVEL, USERS_SOLUTION, USERS_SUGGESTIONS, BOARD_WAS_USED}, VISIBLE_DIGITS + " = ?",
                new String[]{start_digits}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        BoardDetails board = new BoardDetails(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), Integer.parseInt(cursor.getString(6)));
        return board;
    }

    private BoardDetails getFirstBoardByDifficultyLevel(String level) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[]{KEY_ID,
                        VISIBLE_DIGITS, SOLUTION, DIFFICULTY_LEVEL, USERS_SOLUTION, USERS_SUGGESTIONS, BOARD_WAS_USED}, DIFFICULTY_LEVEL + " = ?",
                new String[]{level}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        BoardDetails board = new BoardDetails(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), Integer.parseInt(cursor.getString(6)));
        return board;
    }

    // Getting all boards by difficulty level
    private List<BoardDetails> getBoardsByDifficultyLevel(String level) {

        List<BoardDetails> boardsList = new ArrayList<BoardDetails>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE " + DIFFICULTY_LEVEL + " = \'" + level + "\' ORDER BY " + DIFFICULTY_LEVEL + ", RANDOM() LIMIT 1 ;";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                BoardDetails board = new BoardDetails();
                board.setID(Integer.parseInt(cursor.getString(0)));
                board.setVisibleDigits(cursor.getString(1));
                board.setBoardSolution(cursor.getString(2));
                board.setDifficultyLevel(cursor.getString(3));
                board.setUserSolution(cursor.getString(4));
                board.setUserSuggestions(cursor.getString(5));
                board.setBoardUsed(Integer.parseInt(cursor.getString(6)));
                // Adding board to list
                boardsList.add(board);
            } while (cursor.moveToNext());
        }
        return boardsList;
    }

    // Getting all boards by difficulty level
    public BoardDetails getTheMostLeastBoardByDifficultyLevel(String level) {
//TODO to test,  sprawdzic czy dziala jak powinno
        BoardDetails board;
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE " + DIFFICULTY_LEVEL + " = \'" + level
                + "\' AND " + BOARD_WAS_USED + " = 0   LIMIT 1 ;";
//        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            cursor.moveToFirst();
            board = new BoardDetails(Integer.parseInt(cursor.getString(0)),
                    cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), Integer.parseInt(cursor.getString(6)));
        } else {
            updateUsedValueInAllBoards();
            //board = getFirstBoardByDifficultyLevel(level);
            board = getTheMostLeastBoardByDifficultyLevel(level);

        }
        return board;
    }

    // Getting All Boards
    private List<BoardDetails> getAllBoards() {
        List<BoardDetails> boardsList = new ArrayList<BoardDetails>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                BoardDetails board = new BoardDetails();
                board.setID(Integer.parseInt(cursor.getString(0)));
                board.setVisibleDigits(cursor.getString(1));
                board.setBoardSolution(cursor.getString(2));
                board.setDifficultyLevel(cursor.getString(3));
                board.setUserSolution(cursor.getString(4));
                board.setUserSuggestions(cursor.getString(5));
                board.setBoardUsed(Integer.parseInt(cursor.getString(6)));
                // Adding board to list
                boardsList.add(board);
            } while (cursor.moveToNext());
        }
        return boardsList;
    }

    public int cleanDataBoard(String start_digits) {
        SQLiteDatabase db = this.getWritableDatabase();
        String empty = null;
        BoardDetails previousBoard = getBoardByVisibleDigits(start_digits);
        ContentValues values = new ContentValues();
        values.put(VISIBLE_DIGITS, start_digits);
        values.put(SOLUTION, previousBoard.getBoardSolution());
        values.put(DIFFICULTY_LEVEL, previousBoard.getDifficultyLevel());
        values.put(USERS_SOLUTION, empty);
        values.put(USERS_SUGGESTIONS, empty);
        values.put(BOARD_WAS_USED, 1);

        // updating row
        return db.update(TABLE_NAME, values, KEY_ID + " = ?",
                new String[]{String.valueOf(previousBoard.getID())});
    }

    public void updateUsedValueInAllBoards() {
        SQLiteDatabase db = this.getWritableDatabase();
        List<BoardDetails> contacts = getAllBoards();
        for (BoardDetails board : contacts) {
            ContentValues values = new ContentValues();
            values.put(VISIBLE_DIGITS, board.getVisibleDigits());
            values.put(SOLUTION, board.getBoardSolution());
            values.put(DIFFICULTY_LEVEL, board.getDifficultyLevel());
            values.put(USERS_SOLUTION, board.getUserSolution());
            values.put(USERS_SUGGESTIONS, board.getUserSuggestions());
            values.put(BOARD_WAS_USED, 0);
            db.update(TABLE_NAME, values, KEY_ID + " = ?",
                    new String[]{String.valueOf(board.getID())});
        }
    }

    // Updating single board
    public int updateBoard(BoardDetails board_details) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(VISIBLE_DIGITS, board_details.getVisibleDigits());
        values.put(SOLUTION, board_details.getBoardSolution());
        values.put(DIFFICULTY_LEVEL, board_details.getDifficultyLevel());
        values.put(USERS_SOLUTION, board_details.getUserSolution());
        values.put(USERS_SUGGESTIONS, board_details.getUserSuggestions());
        values.put(BOARD_WAS_USED, board_details.getBoardUsed());
        // updating row
        return db.update(TABLE_NAME, values, KEY_ID + " = ?",
                new String[]{String.valueOf(board_details.getID())});
    }

    // Deleting single board
    public void deleteBoard(BoardDetails board) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + " = ?",
                new String[]{String.valueOf(board.getID())});
        db.close();
    }


    // Getting boards Count
    public int getBoardsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

    public void deleteDatabase(Context context) {
        context.deleteDatabase(DATABASE_NAME);
    }
}
