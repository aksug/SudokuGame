package sudoku.example.com.sudoku;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.graphics.Paint;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.ArrayList;

public class Board extends View {

    //gift
//    private final InputStream mStream;
//    private final Movie mMovie;
    private long mMoviestart;


    private String stan_gry ="W_TRACIE"; //"ZAPISZ","ZAKONCZ";

    private DataBoard dataBoard;
    private String start_board;
    private String solution;

    private float width_big_square;
    private float width_small_square;
    private float width_Number;
    private float width_divided_cell;
    private int margin;

    private int selected_field_x;
    private int selected_field_y;
    private boolean[][] divided_cell;
    private ArrayList<int[]> possible_numbers_squere;

    private int[][] user_solution;
    private boolean[][] wrong_solution;

    private Paint background;
    private Paint edges_inside;
    private Paint edges_outside;
    private Paint numbers;
    private Paint place_start_numbers;
    private Paint selected_field;
    private Paint posible_numbers;
    private Paint correct_numbers;

    Context context;


    public Board(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        //gift
     //   this.mStream = stream;
    //    mMovie = Movie.decodeStream(mStream);

        init();
    }

    private void init() {

        dataBoard = new DataBoard();
        // plansza wyjsciowa - czyli ta z danmi elementami
        start_board = dataBoard.getStartBoardNumbers();
        margin = 5;

        divided_cell = new boolean[9][9];
        wrong_solution = new boolean[9][9];
        //mozliwe cyfry w podzielonym kwadraciku
        possible_numbers_squere = new ArrayList<>();
        for (int i = 0; i < 81; i++) {
            possible_numbers_squere.add(new int[9]);
        }
         user_solution = new int[9][9];
        //   solution = dataBoard.getBoardSolution();
        // mistakes = new boolean[9][9];
//tlo planszy
        background = new Paint();
        background.setColor(getResources().getColor(R.color.background));
//krawedzie wewnetrzne planszy
        edges_inside = new Paint();
        edges_inside.setColor(getResources().getColor(R.color.small_line));
        edges_inside.setStrokeWidth(2);
//krawedzie zewnetrze planszy
        edges_outside = new Paint();
        edges_outside.setColor(getResources().getColor(R.color.big_line));
        edges_outside.setStrokeWidth(4);
// do koloru,wzoru cyfr uzupelnionych przy rozpoczeciu gry
        numbers = new Paint();
        numbers.setColor(getResources().getColor(R.color.start_number));
        numbers.setTextSize(80);//TODO - uzeleznic te wielkosci od wielkosci komorki w ktorej sie znajduja
        numbers.setStrokeWidth(4);
//uzupelnione elementy przed rozpoczecem gry
        place_start_numbers = new Paint();
        place_start_numbers.setColor(getResources().getColor(R.color.start_place));
// wyrozniony kwadracik
        selected_field = new Paint();
        selected_field.setColor(getResources().getColor(R.color.selection));
// rozwazane cyfry w dane komorce
        posible_numbers = new Paint();
        posible_numbers.setColor(getResources().getColor(R.color.number));
        posible_numbers.setTextSize(60);
        posible_numbers.setStrokeWidth(2);
// poprawne cyfry
        correct_numbers = new Paint();
        correct_numbers.setColor(getResources().getColor(R.color.correct_numbers));
        correct_numbers.setTextSize(100);
        correct_numbers.setStrokeWidth(5);
    }


    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.width_big_square = w;
//        Log.d(TAG, "width=" + width_big_square);
        this.width_small_square = (w - 2 * margin) / 9;
        this.width_Number = width_small_square / 5;
        this.width_divided_cell = width_small_square / 3;

    }


    public void onDraw(Canvas canvas) {
        //gift
        final long now = SystemClock.uptimeMillis();
        if (mMoviestart == 0) { mMoviestart = now; }



        //obramowanie i kwadrat
        canvas.drawRect(0, 0, width_big_square, width_big_square, edges_outside);
        canvas.drawRect(margin, 0 + margin, width_big_square - margin, width_big_square - margin, background);

        //TODO ma najpierw rysowac wewnetrzne linie pozniej zewnetrze
        //TODO jakos z tym marginesem zakombinowac
        for (int i = 1; i < 9; i++) {
            if (i % 3 != 0) {
                canvas.drawLine((i * width_small_square), 0, (i * width_small_square), width_big_square, edges_inside);
                canvas.drawLine(0, (i * width_small_square), width_big_square, (i * width_small_square), edges_inside);
            } else {
                canvas.drawLine((i * width_small_square), 0, (i * width_small_square), width_big_square, edges_outside);
                canvas.drawLine(0, (i * width_small_square), width_big_square, (i * width_small_square), edges_outside);
            }
        }

        //poswietlenie pola //TOOO jest za duze
        canvas.drawRect((selected_field_x * width_small_square), (selected_field_y * width_small_square),
                ((selected_field_x + 1) * width_small_square),
                ((selected_field_y + 1) * width_small_square),
                selected_field);

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                //zaciemniam kwadraciki w ktorych beda dane cyfry na poczatku gry
                if (Character.getNumericValue(start_board.charAt(j + 9 * i + i)) != 0) {
//                    Log.d("Draw,!=0  cell ","j="+ j+ ", i="+i+", currect_cell="+currect_cell);
                    canvas.drawRect((j * width_small_square),
                            (i * width_small_square),
                            ((j + 1) * width_small_square),
                            ((i + 1) * width_small_square),
                            place_start_numbers);
                    //uzupelniam poczatkowe numery //TODO numery musza pasowac w kwadraciu
                    float shift_numbers = ((width_small_square / 5-margin));
                    canvas.drawText(String.valueOf(start_board.charAt(j + 9 * i + i)),
                            (j * width_small_square + shift_numbers),
                            ((i + 1) * width_small_square - shift_numbers),
                            numbers);
                }
                //wyswietlam poprawne rozwiazanie =
                if(stan_gry.equals("ZAKONCZ") && wrong_solution[i][j]){
                    Toast.makeText(context, "ZAKONCZ WYSWIETL ANIMACJE", Toast.LENGTH_SHORT).show();
                    canvas.drawText(String.valueOf(solution.charAt(j + 9 * i + i)),
                            (j * width_small_square + width_Number),
                            ((i + 1) * width_small_square - width_Number),
                            correct_numbers);
                }
                //liczby wpisane przez usera:
                else if (user_solution[i][j] != 0 ) {

                        canvas.drawText(String.valueOf(user_solution[i][j]),
                                (j * width_small_square + width_Number),
                                ((i + 1) * width_small_square - width_Number),
                                numbers);


                }

                //UWAGA  czasami krawedzie nie rysuja sie - TODO
                else if (divided_cell[i][j]) {
                    //rysuj linie w malym kwadracku
                    canvas.drawLine((j * width_small_square + width_divided_cell),
                            (i * width_small_square),
                            (j * width_small_square + width_divided_cell),
                            (i * width_small_square + width_small_square),
                            edges_inside);
                    canvas.drawLine((j * width_small_square + 2 * width_divided_cell),
                            (i * width_small_square),
                            (j * width_small_square + 2 * width_divided_cell),
                            (i * width_small_square + width_small_square),
                            edges_inside);

                    canvas.drawLine((j * width_small_square)
                            , (i * width_small_square + width_divided_cell),
                            (j * width_small_square + width_small_square),
                            (i * width_small_square + width_divided_cell), edges_inside);

                    canvas.drawLine((j * width_small_square)
                            , (i * width_small_square + 2 * width_divided_cell),
                            (j * width_small_square + width_small_square),
                            (i * width_small_square + 2 * width_divided_cell), edges_inside);
                    //wypisz proponowane cyfry w malym kwadraciku
                    for (int k = 0; k < 9; k++) {
                        if (possible_numbers_squere.get(i * 9 + j)[k] != 0) {
                            canvas.drawText(String.valueOf(possible_numbers_squere.get(i * 9 + j)[k]),
                                    (j * width_small_square + width_divided_cell / 6 + k % 3 * width_divided_cell),
                                    (float) (i * width_small_square + width_divided_cell * 0.9 + k / 3 * width_divided_cell),
                                    posible_numbers);
                        }
                    }
                }
            }
        }
//gift
//        final int relTime = (int)((now - mMoviestart) % mMovie.duration());
//        mMovie.setTime(relTime);
//        mMovie.draw(canvas, 10, 10);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("DrawView", "onTouchEvent: " + (int) (event.getX() / width_small_square) + "," + (int) (event.getY() / width_small_square));
//TODO naprawic momenty kiedy klikasz poza plansze
        if (event.getAction() == MotionEvent.ACTION_UP) {

            int x = (int) (event.getX() / width_small_square);
            int y = (int) (event.getY() / width_small_square);

            int clicked_cell = Character.getNumericValue(start_board.charAt(x + 9 * y + y));
            if (clicked_cell == 0) {
                selected_field_x = x;
                selected_field_y = y;
                invalidate();
            }
        }
        return true;
    }


    public void setNumber(int n) {
        //wypisz odpowiedz w polu
        if (!divided_cell[selected_field_y][selected_field_x]) {
            user_solution[selected_field_y][selected_field_x] = n;
        }
        //wypisz propozycje w podzielonoym kwadraciku
        else if (possible_numbers_squere.get(9 * selected_field_y + selected_field_x)[n - 1] == 0) {
            possible_numbers_squere.get(9 * selected_field_y + selected_field_x)[n - 1] = n;
        } else {
            possible_numbers_squere.get(9 * selected_field_x + selected_field_y)[n - 1] = 0;
        }
        invalidate();
    }

    public void divadeSamllSquere() {
        user_solution[selected_field_y][selected_field_x] = 0;
        divided_cell[selected_field_y][selected_field_x] = !divided_cell[selected_field_y][selected_field_x];
        invalidate();
    }

    public void backspace() {
        user_solution[selected_field_y][selected_field_x] = 0;
        possible_numbers_squere.set(9 * selected_field_y + selected_field_x, new int[9]);
        invalidate();
    }

    public void checkSolution() {//TODO


        solution = dataBoard.getBoardSolution();
        //jesli pole nie jest aktualnie podzielone, mozna sprawdzac pola ktore byly na wstepie jako 0
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (!divided_cell[i][j] && Character.getNumericValue(start_board.charAt(j + 9 * i + i)) == 0){
                    if(user_solution[i][j] == Character.getNumericValue(solution.charAt(j + 9 * i + i))){
                        Log.d("Rozwiazanie ","user poprwnie wypelnil pole: "+i+" "+j ); //dziala
                    }else {
                        Log.d("Rozwiazanie ", "user zle wypelnil pole: " + i + " " + j); //dziala
                        //anmacja ? z poprawianiem pola
                        wrong_solution[i][j] = !wrong_solution[i][j];

                    }
                }
            }
        }
        stan_gry = "ZAKONCZ";
        invalidate();

    }
    public ArrayList<int[]> getPossible_numbers_squere() {
        return possible_numbers_squere;
    }

    public int[][] getUser_solution() {
        return user_solution;
    }

    public DataBoard getDataBoard() {
        return dataBoard;
    }


    public void setDataBoard(DataBoard dataBoard) {
        this.dataBoard = dataBoard;
    }

    public void setStart_board(String start_board) {
        this.start_board = start_board;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public void setUser_solution(int[][] user_solution) {
        this.user_solution = user_solution;
    }

    public void setPossible_numbers_squere(ArrayList<int[]> possible_numbers_squere) {
        this.possible_numbers_squere = possible_numbers_squere;
    }

}
