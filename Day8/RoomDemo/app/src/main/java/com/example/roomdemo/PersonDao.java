package com.example.roomdemo;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PersonDao {
    @Query("SELECT * FROM Person ORDER BY ID")
    List<PersonData> loadAllPersons();

    @Insert
    void insertPerson(PersonData data);

    @Update
    void updatePerson(PersonData data);

    @Delete
    void delete(PersonData data);
}
