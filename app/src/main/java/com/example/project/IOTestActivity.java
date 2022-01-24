package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class IOTestActivity extends AppCompatActivity {
    private TextView txQuestion;
    private RadioGroup rgChoices;
    private RadioButton rbChoice1;
    private RadioButton rbChoice2;
    private RadioButton rbChoice3;
    private RadioButton rbChoice4;

    private int nAnswer;
    private int nChoice;

    IQTestDbHelper dbHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iotest1);

        txQuestion = findViewById(R.id.txQuestion);
        rgChoices = findViewById(R.id.rgChoices);
        rbChoice1 = findViewById(R.id.rbChoice1);
        rbChoice2 = findViewById(R.id.rbChoice2);
        rbChoice3 = findViewById(R.id.rbChoice3);
        rbChoice4 = findViewById(R.id.rbChoice4);

        Intent intent = getIntent();
        txQuestion.setText(intent.getStringExtra("question"));
        nAnswer= intent.getIntExtra("answer", 0);

        Random random =new Random();
        nChoice = random.nextInt(3);
        switch (nChoice){
            case 0:
                rbChoice1.setText(Integer.toString(nAnswer));
                rbChoice2.setText(Integer.toString(random.nextInt(nAnswer-1)));
                rbChoice3.setText(Integer.toString(random.nextInt(nAnswer-1)));
                rbChoice4.setText(Integer.toString(random.nextInt(nAnswer-1)));
                break;

            case 1:
                rbChoice2.setText(Integer.toString(nAnswer));
                rbChoice1.setText(Integer.toString(random.nextInt(nAnswer-1)));
                rbChoice3.setText(Integer.toString(random.nextInt(nAnswer-1)));
                rbChoice4.setText(Integer.toString(random.nextInt(nAnswer-1)));
                break;
            case 2:
                rbChoice3.setText(Integer.toString(nAnswer));
                rbChoice2.setText(Integer.toString(random.nextInt(nAnswer-1)));
                rbChoice1.setText(Integer.toString(random.nextInt(nAnswer-1)));
                rbChoice4.setText(Integer.toString(random.nextInt(nAnswer-1)));
                break;
            case 3:
                rbChoice4.setText(Integer.toString(nAnswer));
                rbChoice2.setText(Integer.toString(random.nextInt(nAnswer-1)));
                rbChoice3.setText(Integer.toString(random.nextInt(nAnswer-1)));
                rbChoice1.setText(Integer.toString(random.nextInt(nAnswer-1)));
                break;
        }
        rgChoices.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
            Intent returnIntent = new Intent();
            RadioButton rbChecked = findViewById(checkedId);
            int youranswer=Integer.parseInt(rbChecked.getText().toString());
            returnIntent.putExtra("correct", false);
            Log.d("IOTestActivity", "checked " +checkedId);

            switch(checkedId){
                case R.id.rbChoice1:
                    if(nChoice==0){
                        Log.d("IOTestActivity", "checked "+checkedId+"1 correct");
                        returnIntent.putExtra("correct", true);
                    }
                    break;
                case R.id.rbChoice2:
                    if(nChoice==1){
                        Log.d("IOTestActivity", "checked "+checkedId+"2 correct");
                        returnIntent.putExtra("correct", true);
                    }
                    break;
                case R.id.rbChoice3:
                    if(nChoice==2){
                        Log.d("IOTestActivity", "checked "+checkedId+"3 correct");
                        returnIntent.putExtra("correct", true);
                    }
                    break;
                case R.id.rbChoice4:
                    if(nChoice==3){
                        Log.d("IOTestActivity", "checked "+checkedId+"4 correct");
                        returnIntent.putExtra("correct", true);
                    }
                    break;
            }
            dbHelper = new IQTestDbHelper(getApplicationContext());
            db = dbHelper.getWritableDatabase();
            boolean correct = returnIntent.getBooleanExtra("correct", false);
            ContentValues valus = new ContentValues();//insert database questionLog
            valus.put(QuestionsLog.QuestionsLogEntry.COLUMN_NAME_QUESTION, txQuestion.getText().toString());
            valus.put(QuestionsLog.QuestionsLogEntry.COLUMN_NAME_YOURANSWER, youranswer);
            valus.put(QuestionsLog.QuestionsLogEntry.COLUMN_NAME_ISCORRECT, (correct?"Correct":"Incorrect"));
            long newRowId = db.insert(QuestionsLog.QuestionsLogEntry.TABLE_NAME,null, valus);
            Toast cToast = Toast.makeText(getApplicationContext(), "Correct", Toast.LENGTH_SHORT);
            Toast inCToast = Toast.makeText(getApplicationContext(), "Incorrect", Toast.LENGTH_SHORT);
            if(correct){
                cToast.show();
            }
            if(!correct){
                inCToast.show();
            }
                setResult(Activity.RESULT_OK, returnIntent);
            finish();
            }
        });
    }
}
