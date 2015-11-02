package sudoku.example.com.sudoku;

import android.os.Parcel;
import android.os.Parcelable;

import java.lang.ref.SoftReference;
import java.util.ArrayList;

public class DataBoard implements Parcelable {

    private String start_board_numbers;
    private String solution;
    private String id;
    private int[][] users_solutions;
    private ArrayList<int[]>  users_propositionsToFillcCell;

    DataBoard() {
        this.start_board_numbers = "000724000 003000700 206000810 960003082 810002095 300980070 704000150 000267000 009000200";
        this.solution = "198724563 453816729 276539814 965173482 817642395 342985671 724398156 531267948 689451237";

    }

    protected DataBoard(Parcel in) {
        start_board_numbers = in.readString();
        solution = in.readString();
    }

    public static final Creator<DataBoard> CREATOR = new Creator<DataBoard>() {
        @Override
        public DataBoard createFromParcel(Parcel in) {
            return new DataBoard(in);
        }

        @Override
        public DataBoard[] newArray(int size) {
            return new DataBoard[size];
        }
    };

    public String getStartBoardNumbers() {
        return start_board_numbers;
    }

    public String getBoardSolution() {
        return solution;
    }

    public void setStartBoardNumbers(String start_board_numbers) {
        this.start_board_numbers = start_board_numbers;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public int[][] getUsers_solutions() {
        return users_solutions;
    }

    public void setUsers_solutions(int[][] users_solutions) {
        this.users_solutions = users_solutions;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<int[]>  getUsers_propositionsToFillcCell() {
        return users_propositionsToFillcCell;
    }

    public void setUsers_propositionsToFillcCell(ArrayList<int[]>  users_propositionsToFillcCell) {
        this.users_propositionsToFillcCell = users_propositionsToFillcCell;
    }
    public DataBoard getDataBoard(){
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(start_board_numbers);
        dest.writeString(solution);
    }
}
