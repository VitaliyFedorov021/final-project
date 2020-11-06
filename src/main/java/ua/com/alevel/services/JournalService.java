package ua.com.alevel.services;

import ua.com.alevel.dto.Journal;
import ua.com.alevel.exceptions.IncorrectDataException;
import ua.com.alevel.exceptions.NoDataInDBException;

import java.util.List;

public interface JournalService {
    boolean createNewEntry(Journal journal) throws NoDataInDBException, IncorrectDataException;

    List<Journal> selectAllEntries();

    List<Journal> selectAllByStudentLogin(String login) throws IncorrectDataException, NoDataInDBException;

    List<Journal> selectAllByCourseId(int id) throws IncorrectDataException, NoDataInDBException;

    List<Journal> selectAllByTeacherId(int id) throws IncorrectDataException;

    List<Journal> selectAllHigherThanMark(int mark) throws IncorrectDataException;

    boolean updateJournalEntry(Journal journal) throws NoDataInDBException, IncorrectDataException;
}
