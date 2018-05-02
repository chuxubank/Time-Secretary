package com.termproject.misaka.timesecretary.database;

public class TaskDbSchema {
    public static final class TaskTable {
        public static final String NAME = "tasks";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String CATEGORY = "category";
            public static final String TITLE = "title";
            public static final String NOTES = "notes";
            public static final String START_TIME = "start_time";
            public static final String END_TIME = "end_time";
            public static final String DEFER_UNTIL = "defer_until";
            public static final String DEADLINE = "deadline";
            public static final String CHECKED = "checked";

        }
    }
}
