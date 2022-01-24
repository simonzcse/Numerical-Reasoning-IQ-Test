package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class TestLogActivity extends AppCompatActivity {
        ListView listview;
        IQTestDbHelper dbHelper;
        SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_log);

        listview= findViewById(R.id.list);

        dbHelper = new IQTestDbHelper(this);
        db=dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("select * from testlog", null);
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                R.layout.item_layout,
                cursor,
                new String[]{TestLog.TestLogEntry.COLUMN_NAME_TESTDATE,
                        TestLog.TestLogEntry.COLUMN_NAME_TESTTIME,
                        TestLog.TestLogEntry.COLUMN_NAME_DURATION,
                        TestLog.TestLogEntry.COLUMN_NAME_CORRECTCOUNT,

                },
               new int[]{R.id.txtTestDate, R.id.txtTestTime, R.id.txtDuration, R.id.txtCorrectCount });
                listview.setAdapter(adapter);
    }
}
