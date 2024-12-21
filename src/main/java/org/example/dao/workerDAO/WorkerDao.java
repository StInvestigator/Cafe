package org.example.dao.workerDAO;

import org.example.dao.CRUDInterface;
import org.example.model.MenuItem;
import org.example.model.Worker;

import java.sql.Date;
import java.util.List;

public interface WorkerDao extends CRUDInterface<Worker> {
    List<Worker> findAllWorkersWithPosition(String position);
    List<Worker> findWorkersThatDidMenuTypeOnDate(String type, Date date);
}
