package sudoku.example.com.sudoku;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.cocosw.bottomsheet.BottomSheet;
import com.software.shell.fab.ActionButton;

import java.util.ArrayList;
import java.util.List;

import sudoku.example.com.model.DaoMaster;
import sudoku.example.com.model.DaoSession;
import sudoku.example.com.model.SudokuBoards;
import sudoku.example.com.model.SudokuBoardsDao;

public class Game extends Activity {

    private String level;
    private Board board;
    private String TAG = "Game";
    static final String STATE_GAME = "state_game";

    private int[][] userSolutionToSave;
    private ArrayList<int[]> possibleNumbersToSave;
    private DataBoard dataBoardToSave;
    private RelativeLayout gameView;
    private int screen_hight;
    private int screen_width;

    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private SudokuBoardsDao board_data;
    private List<SudokuBoards> founded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);


        Bundle bundle = getIntent().getExtras();
        level = bundle.getString("level");
        Log.d(TAG, "in onCreate()");
        board = (Board) findViewById(R.id.boardView);
        gameView = (RelativeLayout) findViewById(R.id.linarlayout);


        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(getApplicationContext(),
                "Boards.sqlite", null);
        db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        board_data = daoSession.getSudokuBoardsDao();
//        addDataToDataBase("000724000 003000700 206000810 960003082 810002095 300980070 704000150 000267000 009000200", "198724563 453816729 276539814 965173482 817642395 342985671 724398156 531267948 689451237", "latwy", null, null, false);
        returnAllData(board_data);
        final SudokuBoards foundBoard = findBoardByDifficultyLevel(level);
        updateBoardsUsedValue(foundBoard);

        board.setSolution(foundBoard.getSolution());
        board.setStart_board(foundBoard.getVisible_numbers());
        //  board.setPossible_numbers_squere(board_data.getUserSuggestions());// TODO zpisywanie possible numbers to database
        board.setUser_solution(foundBoard.getUsers_solution());

        screen_hight = Resources.getSystem().getDisplayMetrics().heightPixels;
        screen_width = Resources.getSystem().getDisplayMetrics().widthPixels;

        board.setLayoutParams(new RelativeLayout.LayoutParams(screen_width, screen_width));//? TODO

        final Game action = this;
        ActionButton fab = (ActionButton) findViewById(R.id.action_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new BottomSheet.Builder(action, R.style.BottomSheet_StyleDialog)
                        .grid() // <-- important part
                        .sheet(R.menu.menu_bottom_sheet)
                        .listener(new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case R.id.save:
                                        Toast.makeText(getApplicationContext(), "Kliknieto opcję save ", Toast.LENGTH_SHORT).show();
                                        save();
                                        break;
                                    case R.id.exit_endGame:
                                        Toast.makeText(getApplicationContext(), "Kliknieto opcję exit ", Toast.LENGTH_SHORT).show();
                                        endGame();
                                        break;
                                    case R.id.check:
                                        Toast.makeText(getApplicationContext(), "Kliknieto opcję check ", Toast.LENGTH_SHORT).show();
                                        // przegladam odpowiedzi usera i wylapuje bledne
                                        board.checkSolution(foundBoard.getSolution());
                                        break;
                                }
                            }
                        }).show();
            }
        });
    }

    //TODO zapisywanie i przywracanie gry !!!


    public void numberOne(View view) {
        board.setNumber(1);
    }

    public void numberTwo(View view) {
        board.setNumber(2);
    }

    public void numberThree(View view) {
        board.setNumber(3);
    }

    public void numberFour(View view) {
        board.setNumber(4);
    }

    public void numberFive(View view) {
        board.setNumber(5);
    }

    public void numberSix(View view) {
        board.setNumber(6);
    }

    public void numberSeven(View view) {
        board.setNumber(7);
    }

    public void numberEight(View view) {
        board.setNumber(8);
    }

    public void numberNine(View view) {
        board.setNumber(9);
    }

    public void divideSquare(View view) {
        Toast.makeText(getApplicationContext(), "Podziel komorke", Toast.LENGTH_SHORT).show();
        board.divadeSamllSquere();
    }

    public void clear(View view) {
        board.backspace();
        Toast.makeText(getApplicationContext(), "Wcisnieto CLEAR", Toast.LENGTH_SHORT).show();
    }

    private DataBoard save() {
        //TODO zapisywanie danych do BD
        userSolutionToSave = board.getUser_solution();
        possibleNumbersToSave = board.getPossible_numbers_squere();
        // dataBoardToSave = board.getDataBoard();
        dataBoardToSave.setUsers_solutions(userSolutionToSave);
        dataBoardToSave.setUsers_propositionsToFillcCell(possibleNumbersToSave);
        return dataBoardToSave;
    }

    public void endGame() {

        LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        ViewGroup container = (ViewGroup) layoutInflater.inflate(R.layout.popup, null);
//        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.gameView);

        final PopupWindow popupWindow = new PopupWindow(container, screen_width, screen_hight/*bialy pasek*/, true);
//        popupWindow.showAtLocation(linearLayout, Gravity.NO_GRAVITY, 0, (linearLayout.getHeight() - popupWindow.getHeight() / 2) / 2);//czy to jest na srodku

        popupWindow.showAtLocation(gameView, Gravity.NO_GRAVITY, 0, 0);//czy to jest na srodku

        Button nowaGra_button = (Button) container.findViewById(R.id.nowaGraButton);
        Button zakoncz_button = (Button) container.findViewById(R.id.zakonczButton);
        ImageButton disdmisButton = (ImageButton) container.findViewById(R.id.dismissButton);

        disdmisButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        zakoncz_button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
        nowaGra_button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                startActivity(new Intent(getApplicationContext(), MenuLevels.class));
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
//        SharedPreferences.Editor ed = mPrefs.edit();
//        ed.putString("level", level);
//        ed.commit();
//TODO
//        Bundle b = new Bundle();
//        b.putParcelable(STATE_GAME, save());
//        onSaveInstanceState(b);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //okienko czy zapisac gre czy nie TODO
        save();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        save();
    }

    @Override
    protected void onStop() {
        super.onStop();
        save();
    }

    private void returnAllData(SudokuBoardsDao board_data) {

        List<SudokuBoards> entities = board_data.queryBuilder().limit(1).list();
        Log.d("NAME", "ilosc rows = " + entities.size());
        for (SudokuBoards cn : entities) {
            String log = "Id: " + cn.getId() + " , " + cn.getVisible_numbers() + " , " + cn.getSolution() + " , " + cn.getLevel() + " , " + cn.getUsers_solution()
                    + " , " + cn.getUsers_suggestions() + " ,   " + cn.getBoard_used();
            // Writing Boards to log
            //     db.deleteContact(cn);
            Log.d("Name: ", log);
        }
    }

    private void addDataToDataBase(String start_data, String solution, String level, String users_solution, String users_suggestions, boolean used) {
        SudokuBoards board_details = new SudokuBoards(null, start_data, solution, level, users_solution, users_suggestions, used);
        try {
            long resultInsert = board_data.insert(board_details);
        } catch (SQLiteConstraintException e) {


            //select boardso tym start-NUMBERS  i po update row with this id
            founded = board_data.queryBuilder().where(SudokuBoardsDao.Properties.Visible_numbers.eq(start_data)).limit(1).list();
            founded.get(0).setSolution(solution);
            founded.get(0).setUsers_suggestions(users_suggestions);
            founded.get(0).setUsers_solution(users_solution);
            founded.get(0).setLevel(level);
            founded.get(0).setBoard_used(used);

            board_data.update(founded.get(0));

        }
    }

    private SudokuBoards findBoardByDifficultyLevel(String level) {
        SudokuBoards board;
        List<SudokuBoards> found = board_data.queryBuilder()
                .where(SudokuBoardsDao.Properties.Level.eq(level),
                        SudokuBoardsDao.Properties.Board_used.eq(false)).list();
        if (found.size() != 0) {

            Log.d("FOUND! = ", "to: " + found.get(0).getId() + ", " + found.get(0).getVisible_numbers() + ", " + found.get(0).getSolution() + ", " + found.get(0).getBoard_used());
            board = found.get(0);
        } else {
            updateAllRowsChangeUsedValue(level);
            board = findBoardByDifficultyLevel(level);
        }
        return board;

    }

    private void updateAllRowsChangeUsedValue(String level) {

        List<SudokuBoards> founded = board_data.queryBuilder().where(SudokuBoardsDao.Properties.Level.eq(level)).list();
        for (SudokuBoards b : founded) {
            SudokuBoards row = board_data.load(b.getId());
            row.setBoard_used(false);
            board_data.update(row);
        }
    }

    private void updateBoardsUsedValue(SudokuBoards foundBoard) {
        SudokuBoards row = board_data.load(foundBoard.getId());
        row.setBoard_used(true);
        board_data.update(row);
    }
}
