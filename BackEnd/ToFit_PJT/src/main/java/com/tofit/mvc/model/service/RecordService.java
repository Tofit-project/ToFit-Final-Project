package com.tofit.mvc.model.service;

import java.util.List;

import com.tofit.mvc.model.dto.Records;
import com.tofit.mvc.model.dto.RecordsView;

public interface RecordService {

	boolean writeRecords(Records rec);

	List<RecordsView> getList(String userId);

	boolean searchRecord(Records rec);

	boolean removeRecords(Records rec);

	List<RecordsView> getMonthlyList(String userId);

}
