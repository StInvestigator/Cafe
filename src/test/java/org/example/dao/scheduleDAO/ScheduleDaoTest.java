package org.example.dao.scheduleDAO;

import org.example.model.Schedule;
import org.example.service.CafeDbInitializer;
import org.example.utils.CreateTestData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.setProperty;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ScheduleDaoTest {

    private final ScheduleDao scheduleDao = new ScheduleDaoImpl();

    @BeforeAll
    static void initTestDB() {
        setProperty("test", "true");
    }

    @BeforeEach
    void prepareTestData() {
        CafeDbInitializer.createTables();
        CreateTestData.insertData();
    }

    @Test
    void save_ShouldInsertScheduleIntoTable_WhenCalled() {
        Schedule newSchedule = new Schedule();
        newSchedule.setWorkerId(1L);
        newSchedule.setDate(Date.valueOf("2024-12-25"));
        newSchedule.setStartTime(Time.valueOf("09:00:00"));
        newSchedule.setEndTime(Time.valueOf("13:00:00"));

        scheduleDao.save(newSchedule);

        List<Schedule> allSchedules = scheduleDao.findAll();
        assertEquals(4, allSchedules.size()); // Проверяем, что добавилась новая запись
        Schedule insertedSchedule = allSchedules.get(3);
        assertEquals(1L, insertedSchedule.getWorkerId());
        assertEquals(Date.valueOf("2024-12-25"), insertedSchedule.getDate());
        assertEquals(Time.valueOf("09:00:00"), insertedSchedule.getStartTime());
        assertEquals(Time.valueOf("13:00:00"), insertedSchedule.getEndTime());
    }

    @Test
    void saveMany_ShouldInsertMultipleSchedules_WhenCalled() {
        List<Schedule> newSchedules = new ArrayList<>();

        Schedule schedule1 = new Schedule();
        schedule1.setWorkerId(2L);
        schedule1.setDate(Date.valueOf("2024-12-26"));
        schedule1.setStartTime(Time.valueOf("10:00:00"));
        schedule1.setEndTime(Time.valueOf("14:00:00"));

        Schedule schedule2 = new Schedule();
        schedule2.setWorkerId(3L);
        schedule2.setDate(Date.valueOf("2024-12-27"));
        schedule2.setStartTime(Time.valueOf("11:00:00"));
        schedule2.setEndTime(Time.valueOf("15:00:00"));

        newSchedules.add(schedule1);
        newSchedules.add(schedule2);

        scheduleDao.saveMany(newSchedules);

        List<Schedule> allSchedules = scheduleDao.findAll();
        assertEquals(5, allSchedules.size()); // Проверяем, что добавились 2 новые записи
    }

    @Test
    void findAll_ShouldReturnAllSchedules_WhenCalled() {
        List<Schedule> schedules = scheduleDao.findAll();
        assertEquals(3, schedules.size()); // Изначально в базе 3 записи
    }

    @Test
    void update_ShouldUpdateSchedule_WhenCalled() {
        List<Schedule> schedules = scheduleDao.findAll();
        Schedule scheduleToUpdate = schedules.get(0);

        scheduleToUpdate.setWorkerId(2L);
        scheduleToUpdate.setDate(Date.valueOf("2024-12-28"));
        scheduleToUpdate.setStartTime(Time.valueOf("12:00:00"));
        scheduleToUpdate.setEndTime(Time.valueOf("16:00:00"));

        scheduleDao.update(scheduleToUpdate);

        List<Schedule> updatedSchedules = scheduleDao.findAll();
        Schedule updatedSchedule = updatedSchedules.get(0);

        assertEquals(2L, updatedSchedule.getWorkerId());
        assertEquals(Date.valueOf("2024-12-28"), updatedSchedule.getDate());
        assertEquals(Time.valueOf("12:00:00"), updatedSchedule.getStartTime());
        assertEquals(Time.valueOf("16:00:00"), updatedSchedule.getEndTime());
    }

    @Test
    void delete_ShouldRemoveSchedule_WhenCalled() {
        List<Schedule> schedules = scheduleDao.findAll();
        assertEquals(3, schedules.size()); // Изначально 3 записи

        Schedule scheduleToDelete = schedules.get(0);
        scheduleDao.delete(scheduleToDelete);

        List<Schedule> updatedSchedules = scheduleDao.findAll();
        assertEquals(2, updatedSchedules.size()); // Проверяем, что запись удалена
    }

    @Test
    void deleteAll_ShouldClearSchedulesTable_WhenCalled() {
        scheduleDao.deleteAll();

        List<Schedule> schedules = scheduleDao.findAll();
        assertEquals(0, schedules.size()); // Таблица должна быть пустой
    }
}
