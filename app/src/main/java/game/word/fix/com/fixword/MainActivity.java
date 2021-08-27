package game.word.fix.com.fixword;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    //CONSTANTS DEFINE
    private static final String FIXWORD_SP_NAME = "FIXWORD_SP_NAME";
    private static final String LEVEL_SP = "LEVEL_SP";
    private static final String LEVEL1_SP = "LEVEL1_SP";
    private static final String LEVEL2_SP = "LEVEL2_SP";
    private static final String LEVEL3_SP = "LEVEL3_SP";
    private static final int DEFAULT_LEVEL = 1;
    public static final int DEFAULT_SCORE = 0;
    public static final int UNANSWERED_INT = 0;
    public static final int ANSWERED_INT = 2;
    private static final int SCORE_INCREMENT_FACTOR = 1;

    //GLOBAL VARIABLE DECLARATION
    private int current_level;
    private Scores scores = new Scores();
    private String currentWordCorrect, currentWordShuffled;
    private TextView shuffleTV, userWordET, score_lvl1, score_lvl2, score_lvl3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //INIT VIEWS
        shuffleTV = findViewById(R.id.text);
        score_lvl1 = findViewById(R.id.score_lvl1);
        score_lvl2 = findViewById(R.id.score_lvl2);
        score_lvl3 = findViewById(R.id.score_lvl3);
        userWordET = findViewById(R.id.edittext);

        //LOAD SCORES AND LEVEL
        SharedPreferences editor = getSharedPreferences(FIXWORD_SP_NAME, MODE_PRIVATE);
        current_level = editor.getInt(LEVEL_SP, DEFAULT_LEVEL);
        scores.level1 = editor.getInt(LEVEL1_SP, DEFAULT_SCORE);
        scores.level2 = editor.getInt(LEVEL2_SP, DEFAULT_SCORE);
        scores.level3 = editor.getInt(LEVEL3_SP, DEFAULT_SCORE);

        //SHOW SCORES ON BOARD
        ShowUpdatedScore();

        //GET WORD, SHUFFLE AND SHOW
        fetchShowWord();
    }

    private void fetchShowWord() {
        currentWordCorrect = WordHelper.getRandomWord(this,current_level);
        currentWordShuffled = WordHelper.shuffleWord(currentWordCorrect);
        shuffleTV.setText(currentWordShuffled);
    }

    public void CheckWord(View view) {
        if (!userWordET.getText().toString().isEmpty()) {
            if (userWordET.getText().toString().toLowerCase().equals(currentWordCorrect.toLowerCase())) {

                //INCREASE SCORE
                IncrementScore(current_level, SCORE_INCREMENT_FACTOR);

                //ADD WORD TO PREDICTED HISTORY
                AddWordToHistory(currentWordCorrect);

                //UPDATE WORD STATUS TO DATABASE
                UpdateWordStatusToDB(currentWordCorrect, ANSWERED_INT, current_level);

                //SHOW SCORES ON BOARD
                ShowUpdatedScore();

                ToastShow.showToast(MainActivity.this, ToastShow.SUCCESS, "Correct\nWord was : " + currentWordCorrect);

                //GET WORD, SHUFFLE AND SHOW
                fetchShowWord();

            } else {
                ToastShow.showToast(MainActivity.this, ToastShow.ERROR, "Invalid Word !!! Try Again...");
            }
            userWordET.setText("");
        }
    }

    private void UpdateWordStatusToDB(String currentWordCorrect, int status, int level) {
        DbHelperWord dbHelperWord = new DbHelperWord(this);
        dbHelperWord.putStatus(currentWordCorrect,level,status);
    }

    private void AddWordToHistory(String currentWordCorrect) {
        DbHelperHistory dbHelperHistory = new DbHelperHistory(this);
        dbHelperHistory.insertData(currentWordCorrect);
        dbHelperHistory.close();
    }

    private void IncrementScore(int current_level, int factor) {
        SharedPreferences.Editor editor = getSharedPreferences(FIXWORD_SP_NAME, MODE_PRIVATE).edit();
        switch (current_level) {
            case 1:
                editor.putInt(LEVEL1_SP, scores.level1 + factor);
                scores.level1++;
                break;
            case 2:
                editor.putInt(LEVEL2_SP, scores.level2 + factor);
                scores.level1++;
                break;
            case 3:
                editor.putInt(LEVEL3_SP, scores.level3 + factor);
                scores.level1++;
                break;
        }
        editor.apply();
    }

    private void LevelUpdate(int newLevel) {
        current_level = newLevel;
        SharedPreferences.Editor editor = getSharedPreferences(FIXWORD_SP_NAME, MODE_PRIVATE).edit();
        editor.putInt(LEVEL_SP, current_level);
        editor.apply();
    }

    public void alphabets(View view) {
        userWordET.setText(userWordET.getText().toString() + ((Button) view).getText().toString());
    }

    public void erase(View view) {
        if (userWordET.getText().toString().length() > 0)
            userWordET.setText(userWordET.getText().toString().substring(0, userWordET.getText().toString().length() - 1));
    }

    private void ShowUpdatedScore() {
        score_lvl1.setText("Level-1 : " + scores.level1);
        score_lvl2.setText("Level-2 : " + scores.level2);
        score_lvl3.setText("Level-3 : " + scores.level3);
    }

    public void ShowHistoryWordsDialog(View view) {
        DbHelperHistory dbHelperHistory = new DbHelperHistory(this);
        ArrayList<String> list = dbHelperHistory.getAllData();
        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.database_view_layout);
        TextView textView = dialog.findViewById(R.id.heading);
        textView.setText("History");
        Button button_close = dialog.findViewById(R.id.button_close);
        ListView listView = dialog.findViewById(R.id.listview);
        listView.setAdapter(adapter);
        button_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.level1) {
            LevelUpdate(1);
            ToastShow.showToast(this,ToastShow.SUCCESS,"Level - 1 Selected");
        }
        else if (id == R.id.level2) {
            LevelUpdate(2);
            ToastShow.showToast(this,ToastShow.SUCCESS,"Level - 2 Selected");
        }
        else if (id == R.id.level3) {
            LevelUpdate(3);
            ToastShow.showToast(this,ToastShow.SUCCESS,"Level - 3 Selected");
        }
        else if (id == R.id.hint) {
            showHintDialog();
            return true;
        }
        else if (id == R.id.show) {
            showWordDialog();
            //GET WORD, SHUFFLE AND SHOW
            fetchShowWord();
            return true;
        }

        //LOAD SCORES AND LEVEL
        SharedPreferences editor = getSharedPreferences(FIXWORD_SP_NAME, MODE_PRIVATE);
        current_level = editor.getInt(LEVEL_SP, DEFAULT_LEVEL);
        scores.level1 = editor.getInt(LEVEL1_SP, DEFAULT_SCORE);
        scores.level2 = editor.getInt(LEVEL2_SP, DEFAULT_SCORE);
        scores.level3 = editor.getInt(LEVEL3_SP, DEFAULT_SCORE);

        //SHOW SCORES ON BOARD
        ShowUpdatedScore();

        //GET WORD, SHUFFLE AND SHOW
        fetchShowWord();

        return true;
    }

    private void showHintDialog() {
        String message = "Start Letter : " + currentWordCorrect.substring(0, 1) + "\n\nEnd Letter : " + currentWordCorrect.substring(currentWordCorrect.length() - 1);
        new AlertDialog.Builder(this)
                .setTitle("Hint")
                .setMessage(message)
                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setIcon(getResources().getDrawable(R.drawable.hint))
                .create().
                show();
    }

    private void showWordDialog() {
        String message = "Too Tough!!!\n\nWord was : " + currentWordCorrect;
        new AlertDialog.Builder(this)
                .setTitle("WORD SKIPPED")
                .setMessage(message)
                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setIcon(getResources().getDrawable(R.drawable.hint))
                .create().
                show();
    }


}