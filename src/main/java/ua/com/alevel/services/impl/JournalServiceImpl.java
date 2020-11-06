package ua.com.alevel.services.impl;

import ua.com.alevel.util.Checker;
import ua.com.alevel.dao.JournalDao;
import ua.com.alevel.dao.impl.JournalDaoImpl;
import ua.com.alevel.dto.Journal;
import ua.com.alevel.exceptions.IncorrectDataException;
import ua.com.alevel.exceptions.NoDataInDBException;
import ua.com.alevel.services.JournalService;

import java.util.List;

public class JournalServiceImpl implements JournalService {
    JournalDao journalDao = JournalDaoImpl.getInstance();
    @Override
    public boolean createNewEntry(Journal journal) throws NoDataInDBException, IncorrectDataException {
        if (Checker.isCorrectNumber(journal.getStudentId()) &&
                Checker.isCorrectNumber(journal.getCourseId()) &&
                Checker.isCorrectNumber(journal.getTeacherId()) &&
                Checker.isCorrectNumber(journal.getMark())) {
            journalDao.createNewEntry(journal);
            return true;
        }
        throw new IncorrectDataException("Incorrect data");
    }

    @Override
    public List<Journal> selectAllEntries() {
        return journalDao.selectAllEntries();
    }

    @Override
    public List<Journal> selectAllByStudentLogin(String login) throws IncorrectDataException, NoDataInDBException {
        if (Checker.isCorrectName(login)) {
            return journalDao.selectAllByStudentLogin(login);
        }
        throw new IncorrectDataException("Incorrect login");
    }

    @Override
    public List<Journal> selectAllByCourseId(int id) throws IncorrectDataException, NoDataInDBException {
        if (Checker.isCorrectNumber(id)) {
            return journalDao.selectAllByCourseId(id);
        }
        throw new IncorrectDataException("Incorrect id");
    }

    @Override
    public List<Journal> selectAllByTeacherId(int id) throws IncorrectDataException {
        if (Checker.isCorrectNumber(id)) {
            return journalDao.selectAllByTeacherId(id);
        }
        throw new IncorrectDataException("Incorrect id");
    }

    @Override
    public List<Journal> selectAllHigherThanMark(int mark) throws IncorrectDataException {
        if (Checker.isCorrectNumber(mark)) {
            return journalDao.selectAllHigherThanMark(mark);
        }
        throw new IncorrectDataException("Incorrect mark");
    }

    @Override
    public boolean updateJournalEntry(Journal journal) throws NoDataInDBException, IncorrectDataException {
        if (journal.getMark() >= 0 && journal.getMark() <= 100) {
            journalDao.updateJournalEntry(journal);
            return true;
        }
        throw new IncorrectDataException("Incorrect mark value");
    }
}
