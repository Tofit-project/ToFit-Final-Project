package com.tofit.mvc.model.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tofit.mvc.model.dao.RecordDao;
import com.tofit.mvc.model.dto.Records;
import com.tofit.mvc.model.dto.RecordsView;

@Service
public class RecordServiceImpl implements RecordService{
	
	private final RecordDao recordDao;
	
	public RecordServiceImpl(RecordDao recordDao) {
		super();
		this.recordDao = recordDao;
	}

	// 달력 기록 조회
	@Override
	public List<RecordsView> getList(String userId) {
		return recordDao.selectRecordsView(userId);
	}
	
	// 달력 등록 여부 조회
	@Override
	public boolean searchRecord(Records rec) {
		int result = recordDao.searchRecords(rec);
		return result == 1;
	}
	
	// 달력 기록 등록
	@Override
	public boolean writeRecords(Records rec) {
		int result = recordDao.insertRecords(rec);
		return result == 1;
	}
	
	// 달력 기록 삭제
	@Override
	public boolean removeRecords(Records rec) {
		int result = recordDao.deleteRecords(rec);
		return result == 1;
	}

	// 월별 사용자 기록 가져오기
	@Override
	public List<RecordsView> getMonthlyList(String userId) {
		return recordDao.selectMonthlyRecordsView(userId);
	}



}
