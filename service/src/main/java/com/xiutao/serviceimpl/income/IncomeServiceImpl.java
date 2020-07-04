package com.xiutao.serviceimpl.income;

import com.xiutao.mapper.income.IncomeMapper;
import com.xiutao.pojo.income.Income;
import com.xiutao.serviceapi.income.IncomeService;
import com.xiutao.util.Constants;
import com.xiutao.util.MyDateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class IncomeServiceImpl implements IncomeService {

	private IncomeMapper incomeMapper;

	public IncomeMapper getIncomeMapper() {
		return incomeMapper;
	}

	@Autowired
	public void setIncomeMapper(IncomeMapper incomeMapper) {
		this.incomeMapper = incomeMapper;
	}

	/**
	 * 加载用户的本月的收入
	 */
	@Override
	public List<Income> loadIncomes(int userId) {
		HashMap<String,Object> map = new HashMap<String,Object>();
		map.put("userId", userId);
		map.put("dataScale", Constants.ONLY_THIS_MONTH);
		return incomeMapper.selectAllIncomes(map);
	}

	/**
	 * 加载用户的本月的收入
	 */
	@Override
	public List<Income> load30DaysIncomes(int userId) {
		HashMap<String,Object> map = new HashMap<String,Object>();
		map.put("userId", userId);
		map.put("dataScale", "30days");
		return incomeMapper.selectAllIncomes(map);
	}


	/**
	 * 添加收入income
	 */
	@Override
	public void addIncome(int userId, String date, int item, float money, String moneyType, String remark) {
		Income income = new Income();
		income.setItemId(item);
		income.setUserId(userId); 
		income.setDele(Constants.NOT_DELE);
		income.setMoney(money);
		income.setType_of_money(moneyType);
		income.setDate(MyDateFormat.dateFormat(date));
		income.setRemark(remark);
		incomeMapper.insert(income);
	}
	
	/**
	 * 修改income
	 */
	@Override
	public void changeIncome(int incomeId, float money, String moneyType, int itemId, String remark, Date date) {
		Income income = new Income();
		income.setId(incomeId);
		income.setItemId(itemId);
		income.setMoney(money);
		income.setType_of_money(moneyType);
		income.setRemark(remark);
		income.setDate(date);
		incomeMapper.updateByPrimaryKeySelective(income);
	}

	/**
	 * 删除income，只需要incomeId定位，其他的字段都不需要
	 */
	@Override
	public void deleIncome(int incomeId) {
		Income income = new Income();
		income.setId(incomeId);
		income.setDele(Constants.DELE); 
		incomeMapper.updateByPrimaryKeySelective(income);
	}
}
