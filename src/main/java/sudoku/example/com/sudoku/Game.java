package sudoku.example.com.sudoku;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.cocosw.bottomsheet.BottomSheet;
import com.software.shell.fab.ActionButton;

import java.util.ArrayList;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);


        Bundle bundle = getIntent().getExtras();
        level = bundle.getString("level");
        Log.d(TAG, "in onCreate()");
        board = (Board) findViewById(R.id.boardView);
        gameView = (RelativeLayout) findViewById(R.id.linarlayout);

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
                                        board.checkSolution();
                                        break;
                                }
                            }
                        }).show();
            }
        });
    }

    //TODO zapisywanie i przywracanie gry !!!
    private void resume_game(DataBoard state) {
        board.setUser_solution(state.getUsers_solutions());
        board.setPossible_numbers_squere(state.getUsers_propositionsToFillcCell());
        board.setDataBoard(state.getDataBoard());
    }

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
        dataBoardToSave = board.getDataBoard();
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
        Bundle b = new Bundle();
        b.putParcelable(STATE_GAME, save());
        onSaveInstanceState(b);
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
}
