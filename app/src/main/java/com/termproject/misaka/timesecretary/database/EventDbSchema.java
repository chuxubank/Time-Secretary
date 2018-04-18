package com.termproject.misaka.timesecretary.database;

public class EventDbSchema {
    public static final class EventTable {
        public static final String NAME = "events";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DESCRIPTION = "description";
            public static final String AS_TASK = "as_task";
            public static final String COMPLETED = "completed";
            public static final String START_DATE = "start_date";
            public static final String END_DATE = "end_date";
        }
    }
}
