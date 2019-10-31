package com.commons.model;

import java.io.Serializable;
import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.beans.BeanUtils;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public final class Timesheet implements Serializable{
    /**
	 * 
	 */ 
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue
    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    @Column(name="FROM_TIME")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date from_time;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    @Column(name="TO_TIME")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date to_time;

    @Column(name="TYPE")
    private String type;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss.SSSXXX")
    @Column(name="MANUAL_TIME")
    private Time manual_time;
    
    @Column(name="NOTE")
    private String note;
    
    @Column(name="REF_ID")
    private long ref_id;

    @Column(name="VALID")
    private int valid;
    
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "EMP_ID")
    private Employee employee;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    @Column(name="TS")
    @CreationTimestamp
    private Date ts;

    public Timesheet() {
    }

    public Timesheet(Date from_time, Date to_time, String type, Time manual_time, String note, int valid, long ref_id) {
            this.from_time = from_time;
            this.to_time = to_time;
            this.type = type;
            this.manual_time = manual_time;
            this.note = note;
            this.valid=valid;
            this.ref_id=ref_id;
    }
    
    public Timesheet(Timesheet ts){
            this.updateAttributes(ts);
    }
    
    public void updateAttributes(Timesheet ts){
            this.from_time = ts.getFrom_time();
            this.to_time = ts.getTo_time();
            this.type = ts.getType();
            this.manual_time = ts.getManual_time();
            this.note = ts.getNote();
            this.valid= ts.getValid();
            this.ref_id= ts.getRef_id();
            this.employee = ts.getEmployee();
    }

    public Long getId() {
            return id;
    }

    public void setId(Long id) {
            this.id = id;
    }

    public Date getFrom_time() {
        return from_time;
    }

    public void setFrom_time(Date from_time) {
        //this.from_time = TimeSheetUtils.getDefaultZoneDate(from_time);
    	this.from_time = from_time;
    }

    public Date getTo_time() {
        return to_time;
    }

    public void setTo_time(Date to_time) {
       // this.to_time = TimeSheetUtils.getDefaultZoneDate(to_time);
    	this.to_time = to_time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Time getManual_time() {
        return manual_time;
    }

    public void setManual_time(Time manual_time) {
        this.manual_time = manual_time;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Date getTs() {
        return ts;
    }

    public void setTs(Date ts) {
        this.ts = ts;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public long getRef_id() {
        return ref_id;
    }

    public void setRef_id(long ref_id) {
        this.ref_id = ref_id;
    }

    public int getValid() {
        return valid;
    }

    public void setValid(int valid) {
        this.valid = valid;
    }
    
    /*
    * IF hours is <= 6h THEN result = actual hours
    * IF hours is >6h and <6.5h THEN result = 6h
    * IF hours is >=6.5h THEN result = hours-0.5h
    *
    */
    private double deductBreak(double hours){
        if(hours<=6){
            return hours;
        } else if (hours>6 && hours<=6.5){
            return 6;
        } else if (hours>6.5){
            return hours-0.5;
        } else{
            return hours;
        }
    }
    
    public double getHours(){
        Calendar cal_from = Calendar.getInstance();
        cal_from.setTime(this.from_time);
        Calendar cal_to = Calendar.getInstance();
        cal_to.setTime(this.to_time);
        
        long delta = cal_to.getTimeInMillis()-cal_from.getTimeInMillis();
        
        if(cal_to.get(Calendar.YEAR)<=1970){
            
            Date today = new Date();
            Calendar cal_today = Calendar.getInstance();
            cal_today.setTime(today);
            double delta_double = (cal_today.getTimeInMillis()-cal_from.getTimeInMillis());
            return delta_double/(1000.0*60.0*60.0);
        }else if(delta<0){
            return 0;
        }else{
            double delta_double = delta;
            return delta_double/(1000.0*60.0*60.0);
        }
    }
    
    public double getDeductedHours(){
        return this.deductBreak(this.getHours());
    }

    @Override
    public String toString() {
        return "Timesheet{" + "id=" + id + ", from_time=" + from_time + ", to_time=" + to_time + ", type=" + type + ", manual_time=" + manual_time + ", note=" + note + ", employee=" + employee + ", ts=" + ts + '}';
    }
    
    public Timesheet createCloneWithoutId(){
        Timesheet ts = new Timesheet();
        BeanUtils.copyProperties(this, ts);
        ts.setId(null);
        return ts;
    }
}
