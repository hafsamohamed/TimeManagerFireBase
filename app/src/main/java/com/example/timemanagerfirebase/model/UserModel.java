package com.example.timemanagerfirebase.model;

public class UserModel {
        private String userName, email, password;

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getUserName() {
            return userName;
        }

        public String getEmail() {
            return email;
        }

        public String getPassword() {
            return password;
        }
}
