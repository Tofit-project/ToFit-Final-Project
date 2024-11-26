package com.tofit.mvc.model.dao;

import java.util.List;

import com.tofit.mvc.model.dto.Records;
import com.tofit.mvc.model.dto.RecordsView;

public interface RecordDao {

	int insertRecords(Records rec);

	List<RecordsView> selectRecordsView(String userId);

	int searchRecords(Records rec);

	int deleteRecords(Records rec);

	List<RecordsView> selectMonthlyRecordsView(String userId);

}
