package com.example.project;

import android.provider.BaseColumns;

public class QuestionsLog {

    private QuestionsLog(){}

    public static class QuestionsLogEntry implements BaseColumns{
        public static final String TABLE_NAME = "questionslog";

        public static final String COLUMN_NAME_QUESTION = "question";
        public static final String COLUMN_NAME_YOURANSWER = "youranswer";
        public static final String COLUMN_NAME_ISCORRECT= "iscorrect";



    }
}
