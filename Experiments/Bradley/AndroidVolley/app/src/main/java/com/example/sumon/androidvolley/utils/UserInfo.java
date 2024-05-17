package com.example.sumon.androidvolley.utils;

public class UserInfo {

        private static UserInfo INSTANCE = null;

        public long userId;
        public String fName;
        public String lName;
        public String username;
        public String password;
        public int role;

        private UserInfo() {};

        public static UserInfo getInstance() {
            if (INSTANCE == null) {
                INSTANCE = new UserInfo();
            }
            return(INSTANCE);
        }

        // other instance methods can follow
}
