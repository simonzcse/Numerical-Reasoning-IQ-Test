package com.example.project;

import android.provider.BaseColumns;

public final class TestLog {

    private TestLog(){}

    public static class TestLogEntry implements BaseColumns{

        public static final String TABLE_NAME ="testlog";

        public static final String  COLUMN_NAME_TESTDATE="testdate";
        public static final String  COLUMN_NAME_TESTTIME="testtime";
        public static final String  COLUMN_NAME_DURATION="duration";
        public static final String  COLUMN_NAME_CORRECTCOUNT="correctcount";
    }








}
