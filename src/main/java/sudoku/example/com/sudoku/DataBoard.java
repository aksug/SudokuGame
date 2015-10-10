package sudoku.example.com.sudoku;

public class DataBoard {

    private String start_board_numbers;
    private String solution;

    DataBoard() {
        this.start_board_numbers = "000724000 003000700 206000810 960003082 810002095 300980070 704000150 000267000 009000200";
        this.solution = "198724563 453816729 276539814 965173482 817642395 342985671 724398156 531267948 689451237";

    }

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

}
