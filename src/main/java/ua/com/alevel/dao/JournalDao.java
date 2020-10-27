package ua.com.alevel.dao;

import ua.com.alevel.dto.Journal;

import java.util.List;

public interface JournalDao {
     void createNewEntry();

     List<Journal> selectAllEntry();

     List<Journal> selectAllByStudentLogin(String login);


}
