package com.xiutao.serviceapi.income;

import com.xiutao.pojo.income.Income;

import java.util.Date;
import java.util.List;
public interface IncomeService {

	/**
     * 加载全部income
     * @return
     */
	List<Income> loadIncomes(int userId);

	/**
	 * 加载30天内全部income
	 * @return
	*/
	public List<Income> load30DaysIncomes(int userId);
	/**
	 * 增加收入
	 * @param item
	 * @param money
	 * @param remark
	 */
	void addIncome(int userId, String date, int item, float money, String moneyType, String remark);

	/**
	 * 修改收入
	 * @param incomeId
	 * @param money
	 * @param itemId
	 * @param remark
	 */
	void changeIncome(int incomeId, float money, String moneyType, int itemId, String remark, Date date);
	
	/**
	 * 删除收入
	 * @param incomeId
	 */
	void deleIncome(int incomeId);
}
