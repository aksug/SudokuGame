package sudoku.example.com.sudoku;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MenuLevels extends Activity {
    private String TAG = "Menu_levels";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_levels);
        Log.d(TAG, "in onCreate()");
    }

    public void latwy(View view) {
        runGameActivity("latwy");
    }

    public void sredni(View view) {
        runGameActivity("latwy");
    }

    public void trudny(View view) {
        runGameActivity("latwy");
    }
    private void runGameActivity(String level) {
        Intent intent = new Intent(this, Game.class);
        intent.putExtra("level", level);
        startActivity(intent);
    }

}