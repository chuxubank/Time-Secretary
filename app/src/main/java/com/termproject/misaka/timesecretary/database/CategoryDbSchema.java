package com.termproject.misaka.timesecretary.database;

public class CategoryDbSchema {
    public static final class CategoryTable {
        public static final String NAME = "categories";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String COLOR = "color";
        }
    }
}
