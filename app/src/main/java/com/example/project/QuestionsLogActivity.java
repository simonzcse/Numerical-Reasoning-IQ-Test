package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class QuestionsLogActivity extends AppCompatActivity {
    ListView listView;

    IQTestDbHelper dbHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions_log);

        listView = findViewById(R.id.list);

        dbHelper = new IQTestDbHelper(this);
        db= dbHelper.getReadableDatabase();

        Cursor cursor= db.rawQuery("select * from questionslog", null);
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                R.layout.item_layout2,
                cursor,
                new String[]{
                        QuestionsLog.QuestionsLogEntry.COLUMN_NAME_QUESTION,
                        QuestionsLog.QuestionsLogEntry.COLUMN_NAME_YOURANSWER,
                        QuestionsLog.QuestionsLogEntry.COLUMN_NAME_ISCORRECT
                },
                new int[] {R.id.txtTestDate, R.id.txtTestTime, R.id.txtDuration});
        listView.setAdapter(adapter);
    }
}
