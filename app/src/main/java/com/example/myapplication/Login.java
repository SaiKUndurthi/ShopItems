package com.example.myapplication;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Insert;
import androidx.room.PrimaryKey;
import androidx.room.Query;
import androidx.room.RoomDatabase;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "login")
public class Login
{
    @PrimaryKey
    @NonNull
    public String email;
    @ColumnInfo
    public String firstname;
    @ColumnInfo
    public String lastname;
    @ColumnInfo
    @NonNull
    public String password;

    @Ignore
    public Login(String email, String password){this.email =email; this.password=password;}
    public Login(String firstname, String lastname, String email, String password){this.firstname=firstname; this.lastname=lastname; this.email =email; this.password=password;}

    @NonNull
    public String getPassword() {
        return password;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }
}
@Dao
interface LoginDao
{
    //    @Query("SELECT * FROM user")
//    List<User> getAll();
//
//    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
//    List<User> loadAllByIds(int[] userIds);
//
//    @Query("SELECT * FROM user WHERE first_name LIKE :first AND " +
//            "last_name LIKE :last LIMIT 1")
//    User findByName(String first, String last);
//
//    @Insert
//    void insertAll(User... users);
//
//    @Delete
//    void delete(User user);
    @Query("SELECT * from login")
    List<Login> getAllUsers();

    @Query("SELECT * FROM login WHERE email LIKE :n LIMIT 1")
    Login findByName(String n);

    @Insert
    void insertUser(Login user);

    //@Query("SELECT * FROM login WHERE ")
}

@Database(entities = {Login.class}, version = 1)
abstract class AppDatabase extends RoomDatabase {
    public abstract LoginDao loginDao();
}
