package sudoku.example.com.database;

public class BoardDetails {

    private int _id;
    private String _visible_digits;
    private String _board_solution;
    private String _difficulty_level;
    private String _user_solution;
    private String _user_suggestions;
    private int _board_used; //0 or 1

    public BoardDetails(){
    }
    public BoardDetails(int id, String name, String board_solution, String difficulty_level, String user_solution, String user_suggestions, int board_used){
        this._id = id;
        this._visible_digits = name;
        this._board_solution = board_solution;
        this._difficulty_level = difficulty_level;
        this._user_solution = user_solution;
        this._user_suggestions = user_suggestions;
        this._board_used = board_used;
    }
    public BoardDetails(String name, String board_solution, String difficulty_level,String user_solution, String user_suggestions, int board_used){
        this._visible_digits = name;
        this._board_solution = board_solution;
        this._difficulty_level = difficulty_level;
        this._user_solution = user_solution;
        this._user_suggestions = user_suggestions;
        this._board_used = board_used;
    }

    public int getID(){
        return this._id;
    }

    public void setID(int id){
        this._id = id;
    }

    public String getVisibleDigits(){
        return this._visible_digits;
    }

    public void setVisibleDigits(String name){
        this._visible_digits = name;
    }

    public String getBoardSolution(){
        return this._board_solution;
    }

    public void setBoardSolution(String phone_number){
        this._board_solution = phone_number;
    }

    public String getDifficultyLevel(){
        return this._difficulty_level;
    }

    public void setDifficultyLevel(String difficulty_level){
        this._difficulty_level = difficulty_level;
    }

    public String getUserSolution() {
        return _user_solution;
    }

    public void setUserSolution(String user_solution) {
        this._user_solution = user_solution;
    }

    public String getUserSuggestions() {
        return _user_suggestions;
    }

    public void setUserSuggestions(String user_suggestions) {
        this._user_suggestions = user_suggestions;
    }

    public int getBoardUsed(){
        return this._board_used;
    }

    public void setBoardUsed(int board_used){
        this._board_used = board_used;
    }
}

