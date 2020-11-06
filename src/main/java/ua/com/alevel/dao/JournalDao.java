package ua.com.alevel.dao;

import ua.com.alevel.dto.Journal;
import ua.com.alevel.exceptions.NoDataInDBException;

import java.util.List;

public interface JournalDao {
     boolean createNewEntry(Journal journal) throws NoDataInDBException;

     List<Journal> selectAllEntries();

     List<Journal> selectAllByStudentLogin(String login) throws NoDataInDBException;

     List<Journal> selectAllByCourseId(int id) throws NoDataInDBException;

     List<Journal> selectAllByTeacherId(int id);

     List<Journal> selectAllHigherThanMark(int mark);

     boolean updateJournalEntry(Journal journal) throws NoDataInDBException;
}
