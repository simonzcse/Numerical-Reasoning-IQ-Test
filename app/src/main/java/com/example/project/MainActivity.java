package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private class IQTestTask extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            InputStream inputStream = null;
            String result = "";
            URL url = null;
            try {
                url = new URL(strings[0]);
                HttpURLConnection con = (HttpURLConnection)url.openConnection();

                con.setRequestMethod("GET");
                con.connect();

                inputStream = con.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                Log.d("doInBackground", "get data complete");
                inputStream.close();
            } catch (Exception e) {
                result = e.getMessage();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("MainActivity", "result" + result);
            try {
                JSONObject json = new JSONObject(result);
                questions = json.getJSONArray("questions");
                Log.d("MainActivity","question"+questions.length());
                count = 1;
                int index = random.nextInt(questions.length());
                newTest(index);
                questions.remove(index);
            } catch (JSONException e) {
                e.printStackTrace();

            }
        }
    }
Button btnstart;
TextView textView2;
IQTestDbHelper dbHelper;
SQLiteDatabase db;

IQTestTask task;
JSONArray questions;
int count;
Random random = new Random();


long starttime;
int correctCount;
long newRowId;
private int alertId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper= new IQTestDbHelper(this);
        db=dbHelper.getWritableDatabase();
        textView2=findViewById(R.id.textView2);
        btnstart=findViewById(R.id.btnstart);
        btnstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date= new Date();
                DateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd");
                DateFormat timeFormat= new SimpleDateFormat("hh:mm:ss");

                ContentValues values = new ContentValues();
                values.put(TestLog.TestLogEntry.COLUMN_NAME_TESTDATE, dateFormat.format(date));
                values.put(TestLog.TestLogEntry.COLUMN_NAME_TESTTIME, timeFormat.format(date));

                //long newRawId= db.insert(TestLog.TestLogEntry.TABLE_NAME, null, values);
                //newTest();

                newRowId = db.insert(TestLog.TestLogEntry.TABLE_NAME, null, values);
                starttime = System.currentTimeMillis();
                correctCount = 0;
                btnstart.setEnabled(false);

                if(task == null ||
                    task.getStatus().equals(AsyncTask.Status.FINISHED)){
                    task = new IQTestTask();
                    task.execute("https://ajtdbwbzhh.execute-api.us-east-1.amazonaws.com/default/201920ITP4501Assignment");
                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate((R.menu.main), menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.menuAbout:
                Intent intent = new Intent(getApplicationContext(),AboutActivity.class);
                startActivity(intent);
                return true;
            case R.id.menuTest_Log:
                intent=new Intent(getApplicationContext(), TestLogActivity.class);
                startActivity(intent);
                return true;
            case R.id.memuquestion_Log:
                intent=new Intent(getApplicationContext(), QuestionsLogActivity.class);
                startActivity(intent);
                return true;
            case R.id.memuBarChat:
                intent=new Intent(getApplicationContext(), BarChatActivity.class);
                startActivity(intent);
                return true;
            case R.id.menuExit:
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void newTest(int index) throws JSONException{
        JSONObject question = (JSONObject) questions.get(index);
        Intent intent = new Intent(getApplicationContext(),IOTestActivity.class);
        //intent.putExtra("question", "11, 13, 17, 19, 23, 29, 37, 41, ?");
       // intent.putExtra("answer", 43);
        intent.putExtra("question", question.getString("question"));
        intent.putExtra("answer", question.getInt("answer"));
        intent.putExtra("count", count);
        startActivityForResult(intent, 888);
    }
    @Override
    protected void onResume() {
        super.onResume();
        btnstart.setEnabled(true);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==888 && resultCode == Activity.RESULT_OK){
            boolean correct = data.getBooleanExtra("correct", false);
            if(correct)
                correctCount++;
            Log.d("MainActivity", "count"+count+" correct "+correct+" CorrectCount "+correctCount);
            if(count<5){
                count++;
                int index= random.nextInt(questions.length());
                try {
                    newTest(index);
                    questions.remove(index);
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
            else {
                long duration = (System.currentTimeMillis() - starttime)/1000;
                Log.d("MainActivity", "duration "+duration+" correctCount "+correctCount);

                ContentValues values = new ContentValues();
                values.put(TestLog.TestLogEntry.COLUMN_NAME_DURATION, duration);
                values.put(TestLog.TestLogEntry.COLUMN_NAME_CORRECTCOUNT, correctCount);//insert database testlog

                int recAffected = db.update(TestLog.TestLogEntry.TABLE_NAME,
                        values, TestLog.TestLogEntry._ID+" = "+newRowId, null);
                Log.d("MainActivity", "update testlog return "+ recAffected);
                String re = "Total time " +duration + " seconds";//result
                re += "\nAverage time spent on one question "+((double)duration/5)+ " seconds";
                re += "\nCorrect count " + correctCount;
                re += (correctCount<5?"\nTRY AGAIN!":"\nWELL DONE!");
                textView2.setText(re);
                //SoundPool soundPoolv2 = new SoundPool.Builder()
               //         .setMaxStreams(15)
               //         .build();
              //  int mario = soundPoolv2.load(getApplicationContext(), R.raw.mario, 1);
              //  soundPoolv2.play(mario, 0.99f, 0.99f, 1, 0, 0.99f);
                MusicManager mm = new MusicManager(getApplicationContext(), R.raw.mario);//play sound effect after finish
                mm.play();
                setVibrate(5000); // 震動 1 秒
                btnstart.setEnabled(true);
            }
        }
    }

    public void setVibrate(int time) {
        Vibrator myVibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
        myVibrator.vibrate(time);

    }
}
