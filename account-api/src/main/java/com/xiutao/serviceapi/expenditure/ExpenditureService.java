package com.xiutao.serviceapi.expenditure;

import com.xiutao.pojo.expenditure.Expenditure;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

public interface ExpenditureService {

	/**
     * 加载本月的全部支出
     * @return
     */
	List<Expenditure> loadExpenditures(int userId);

	/**
	 * 加载30天内的全部支出
	 * @return
	 */
	public List<Expenditure> load30DayesExpenditures(int userId);

	/**
	 * 增加Expenditure
	 * @param item
	 * @param money
	 * @param remark
	 */
	void addExpenditure(int userId, String date, int item, float money, String moneyType, String remark);

	/**
	 * 修改Expenditure
	 * @param ExpenditureId
	 * @param money
	 * @param itemId
	 * @param remark
	 * @param date
	 */
	void changeExpenditure(int ExpenditureId, float money, String moneyType, int itemId, String remark, Date date);
	
	/**
	 * 删除Expenditure
	 * @param ExpenditureId
	 */
	void deleExpenditure(int ExpenditureId);
}
