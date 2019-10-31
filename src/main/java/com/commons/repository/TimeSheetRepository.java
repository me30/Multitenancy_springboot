package com.commons.repository;

import java.sql.Date;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.commons.model.Timesheet;

public interface TimeSheetRepository extends JpaRepository<Timesheet, Long>{ 
    
    @Query("select e from Timesheet e where e.valid=1 and e.type like 't' and e.employee.id  = :emp order by e.from_time desc")
    public List<Timesheet> findValidTimeStamps(@Param("emp") long emp);
    
    @Query("select e from Timesheet e  where e.id = :id")
    public List<Timesheet> findbyId(@Param("id") long id);

    @Query("select e from Timesheet e where e.valid=1")
    public List<Timesheet> findValidAllTimeStamps();
    
    @Query("select e from Timesheet e where e.valid=1 and e.type like 't' and ( e.from_time BETWEEN :startDate AND :endDate )")
    public List<Timesheet> findValidAllTimeStampsFromDateBetween(@Param("startDate")Date startDate,@Param("endDate")Date endDate);
    
    @Query("select e from Timesheet e where e.valid=1 and e.type like 't' and e.employee.id = :emp and ( e.from_time BETWEEN :startDate AND :endDate )")
    public List<Timesheet> findValidAllTimeStampsFromDateBetween(@Param("emp")long emp, @Param("startDate")Date startDate,@Param("endDate")Date endDate);

    @Query("select e from Timesheet e  where e.valid=1 and e.type like 'h' and e.employee.id  = :emp order by e.from_time desc")
    public List<Timesheet> findValidHolidays(@Param("emp") long emp);
    
    @Query("select e from Timesheet e  where e.valid=1 and e.type like 's' and e.employee.id  = :emp order by e.from_time desc")
    public List<Timesheet> findValidSickNotes(@Param("emp") long emp);
    
    @Query("select e from Timesheet e  where e.valid=1 and datediff(curdate(),e.from_time)<=61 and e.employee.id  = :emp order by e.from_time desc")
    public List<Timesheet> findValidLast61DaysTimeStamps(@Param("emp")long emp);
    
    @Query("select e from Timesheet e  where e.valid=1 and e.type like 't' and e.employee.id  = :emp and month(e.from_time)= :month order by e.from_time desc")
    public List<Timesheet> findValidAllTimeStampsperMonth(@Param("emp")long emp, @Param("month")int month);
    
    @Query("select e from Timesheet e  where e.valid=1 and e.type like 't' and (month(e.from_time)= :month or month(e.from_time)= :month -1 ) order by e.from_time desc")
    public List<Timesheet> findValidAllTimeStampsperCurrentAndPreviousMonth(@Param("month")int month);
    
    @Query("select e from Timesheet e  where e.valid=1 and e.type like 't' and e.employee.id  = :emp and month(e.from_time)= :month and year(e.to_time)>1970 order by e.from_time desc")
    public List<Timesheet> findValidClosedTimeStampsperMonth(@Param("emp")long emp, @Param("month")int month);
    @Query("select e from Timesheet e  where e.valid=1 and e.type like 't' and e.employee.id  = :emp and (month(e.from_time)= :month or month(e.from_time)= :month -1) and year(e.to_time)>1970 order by e.from_time desc")
    public List<Timesheet> findValidClosedTimeStampsperCurrentAndPreviousMonth(@Param("emp")long emp, @Param("month")int month);
    
    @Query("select e from Timesheet e  where e.valid=1 and e.type like 't' and e.employee.id  = :emp and (e.from_time >= :startDate and e.from_time <= :endDate) and year(e.to_time)>1970 order by e.from_time desc")
    public List<Timesheet> findValidClosedTimeStampsperDates(@Param("emp")long emp, @Param("startDate")Date startDate,@Param("endDate")Date endDate);
    
    @Query("select e from Timesheet e  where e.valid=1 and e.type like 't' and e.employee.id  = :emp and month(e.from_time)= :month and year(e.to_time)<=1970 order by e.from_time desc")
    public List<Timesheet> findValidOpenTimeStampsperMonth(@Param("emp")long emp, @Param("month")int month);
    @Query("select e from Timesheet e  where e.valid=1 and e.type like 't' and e.employee.id  = :emp and (month(e.from_time)= :month or month(e.from_time)= :month -1) and year(e.to_time)<=1970 order by e.from_time desc")
    public List<Timesheet> findValidOpenTimeStampsperCurrentAndPreviousMonth(@Param("emp")long emp, @Param("month")int month);
    
    @Query("select e from Timesheet e  where e.valid=1 and e.type like 't' and e.employee.id  = :emp and year(e.to_time)<=1970 order by e.from_time desc")
    public List<Timesheet> findTimeStampsOpenShifts(@Param("emp")long emp);
    
    @Query("select e from Timesheet e where e.valid=1 and e.type like :type and e.employee.id  IN :emp_ids and ((from_time <= :from_date_within_end and from_time >= :from_date_within_begin) and (to_time <= :to_date_within_end and to_time >= :to_date_within_begin)) order by e.from_time desc")
    public List<Timesheet> findValidByTimeFrames(@Param("type") String type, @Param("from_date_within_begin") Date from_date_within_begin, @Param("from_date_within_end") Date from_date_within_end, @Param("to_date_within_begin") Date to_date_within_begin, @Param("to_date_within_end") Date to_date_within_end, @Param("emp_ids") Set<Long> emp_ids);
    @Query("select e from Timesheet e where e.valid=1 and e.type like :type and e.employee.id  IN :emp_ids and (from_time <= :timeframe_end and to_time >= :timeframe_start) order by e.from_time desc")
    public List<Timesheet> findValidWithinTimeFrame(@Param("type") String type, @Param("timeframe_start") Date timeframe_start, @Param("timeframe_end") Date timeframe_end, @Param("emp_ids") Set<Long> emp_ids);
    @Query("select e from Timesheet e where e.valid=1 and e.type like :type and e.employee.id  IN :emp_ids and (from_time <= :timeframe_end and from_time >= :timeframe_start) order by e.from_time desc")
    public List<Timesheet> findValidFromTimeWithinTimeFrame(@Param("type") String type, @Param("timeframe_start") Date timeframe_start, @Param("timeframe_end") Date timeframe_end, @Param("emp_ids") Set<Long> emp_ids);
    @Query("select e from Timesheet e where e.valid=1 and e.type like :type and e.employee.id  IN :emp_ids and (to_time <= :timeframe_end and to_time >= :timeframe_start) order by e.from_time desc")
    public List<Timesheet> findValidToTimeWithinTimeFrame(@Param("type") String type, @Param("timeframe_start") Date timeframe_start, @Param("timeframe_end") Date timeframe_end, @Param("emp_ids") Set<Long> emp_ids);
    
    @Query("select e from Timesheet e  where e.id=1")
    public List<Timesheet> findRecordWithId1();
    
}

