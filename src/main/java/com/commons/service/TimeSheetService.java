package com.commons.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.commons.model.Timesheet;
import com.commons.repository.TimeSheetRepository;

@Service
public class TimeSheetService {
	@Autowired
	TimeSheetRepository repository;
	
	public Timesheet saveTimesheet(Timesheet ts) {
		return repository.save(ts);
	}

}
