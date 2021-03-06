package com.termproject.misaka.timesecretary.database;

public class EventDbSchema {
    public static final class EventTable {
        public static final String NAME = "events";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String CATEGORY = "category";
            public static final String TITLE = "title";
            public static final String NOTES = "notes";
            public static final String START_TIME = "start_time";
            public static final String END_TIME = "end_time";
        }
    }
}
