package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

class Panel extends LinearLayout {
    int correcCount;

    public Panel(Context context, int correcCount) {
        super(context);
        this.correcCount = correcCount;

        LayoutParams paras= new LayoutParams(LayoutParams.MATCH_PARENT, 100);
        setLayoutParams(paras);

    }

    @Override
    public void onDraw(Canvas c){
        super.onDraw(c);

        int h = getHeight();
        int w = getWidth();

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLUE);

        float radio = (float)correcCount/(float)5;
        int l = (int)(radio * w);
        if(l>0)
                c.drawRect(20, 20, l, h, paint);
    }
}

class MyAdapter extends SimpleCursorAdapter{

    Context context;
    public MyAdapter(Context context, int layout, Cursor c, String[] from, int[] to){
        super(context, layout, c, from, to);
        this.context = context;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Cursor cursor = (Cursor)getItem(position);
        int correctCount = cursor.getInt(cursor.getColumnIndex(TestLog.TestLogEntry.COLUMN_NAME_CORRECTCOUNT));
        Log.d("BarChartActivity", "correctCount " +correctCount);

        View rowView = new Panel(context,correctCount);
        rowView.setWillNotDraw(false);
        return rowView;
    }
}

public class BarChatActivity extends AppCompatActivity {

    ListView listView;
    IQTestDbHelper dbHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_chat);

        listView = findViewById(R.id.list);

        dbHelper = new IQTestDbHelper(this);
        db=dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("select * from testlog", null);
        MyAdapter adapter = new MyAdapter(this,
                R.layout.item_layout,
                cursor,
                new String[]{
                        TestLog.TestLogEntry.COLUMN_NAME_TESTDATE,
                        TestLog.TestLogEntry.COLUMN_NAME_TESTTIME,
                        TestLog.TestLogEntry.COLUMN_NAME_DURATION,
                        TestLog.TestLogEntry.COLUMN_NAME_CORRECTCOUNT
                },
                 new int[] {R.id.txtTestDate, R.id.txtTestTime, R.id.txtDuration, R.id.txtCorrectCount});


         listView.setAdapter(adapter);
    }
}
