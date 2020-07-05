package com.xiutao.serviceimpl.expenditure;

import com.xiutao.mapper.expenditure.ExpenditureMapper;
import com.xiutao.pojo.expenditure.Expenditure;
import com.xiutao.serviceapi.expenditure.ExpenditureService;
import com.xiutao.util.Constants;
import com.xiutao.util.MyDateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ExpenditureServiceImpl implements ExpenditureService {

	private ExpenditureMapper expenditureMapper;

	public ExpenditureMapper getExpenditureMapper() {
		return expenditureMapper;
	}

	@Autowired
	public void setExpenditureMapper(ExpenditureMapper expenditureMapper) {
		this.expenditureMapper = expenditureMapper;
	}

	/**
	 * 加载用户本月的全部支出
	 */
	@Override
	public List<Expenditure> loadExpenditures(int userId) {
		HashMap<String,Object> map = new HashMap<String,Object>();
		map.put("userId", userId);
		map.put("dataScale", Constants.ONLY_THIS_MONTH);
		return expenditureMapper.selectAllExpenditures(map);
	}

	/**
	 * 加载用户本月和30天内的全部支出
	 */
	@Override
	public List<Expenditure> load30DayesExpenditures(int userId) {
		HashMap<String,Object> map = new HashMap<String,Object>();
		map.put("userId", userId);
		map.put("dataScale", "30days");
		return expenditureMapper.selectAllExpenditures(map);
	}

	/**
	 * 添加支出
	 */
	@Override
	public void addExpenditure(int userId, String date, int item, float money, String moneyType, String remark) {
		Expenditure expenditure = new Expenditure();
		expenditure.setItemId(item);
		expenditure.setUserId(userId); 
		expenditure.setDele(Constants.NOT_DELE);
		expenditure.setMoney(money);
		expenditure.setType_of_money(moneyType);
		expenditure.setDate(MyDateFormat.dateFormat(date));
		expenditure.setRemark(remark);
		expenditureMapper.insert(expenditure);
	}

	/**
	 * 修改支出
	 */
	@Override
	public void changeExpenditure(int expenditureId, float money, String moneyType, int itemId, String remark,Date date) {
		Expenditure expenditure = new Expenditure();
		expenditure.setId(expenditureId);
		expenditure.setItemId(itemId);
		expenditure.setMoney(money);
		expenditure.setType_of_money(moneyType);
		expenditure.setRemark(remark);
		expenditure.setDate(date);
		expenditureMapper.updateByPrimaryKeySelective(expenditure);
	}

	/**
	 * 删除支出，只需要expenditureId定位，其他的字段都不需要
	 */
	@Override
	public void deleExpenditure(int expenditureId) {
		Expenditure expenditure = new Expenditure();
		expenditure.setId(expenditureId);
		expenditure.setDele(Constants.DELE);
		expenditureMapper.updateByPrimaryKeySelective(expenditure);
	}
}
