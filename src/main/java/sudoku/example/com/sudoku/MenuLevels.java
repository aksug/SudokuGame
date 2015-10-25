package sudoku.example.com.sudoku;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.cocosw.bottomsheet.BottomSheet;
import com.software.shell.fab.ActionButton;


public class MenuLevels extends AppCompatActivity {
    private String TAG = "Menu_levels";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO add back button on the top
        setContentView(R.layout.activity_menu_levels);

        //to allow Up
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Log.d(TAG, "in onCreate()");

        final MenuLevels action = this;
        ActionButton fab = (ActionButton) findViewById(R.id.action_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheet buttomSheet = new BottomSheet.Builder(action, R.style.BottomSheet_Dialog)
                        .grid() // <-- important part
                        .sheet(R.menu.menu_bottom_sheet)
                        .listener(new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO
                            }
                        }).show();


//                Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
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