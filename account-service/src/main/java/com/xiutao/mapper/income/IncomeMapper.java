package com.xiutao.mapper.income;

import com.xiutao.pojo.income.Income;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface IncomeMapper {

	List<Income> selectAllIncomes(HashMap map);
	
	List<Map> selectIncomesAndExpenditure(HashMap map);
	
	int countIncomesAndExpenditure(HashMap map);

	int updateByPrimaryKeySelective(Income record);

	int insert(Income record);
	
	List<Map> searchLast12Income(Map map);
	
	List<Map> selectIncomeGroupByItemName(Map map);
	
	/////////////////////////////////////////////////////////

	int deleteByPrimaryKey(Integer id);

	int insertSelective(Income record);

	Income selectByPrimaryKey(Integer id);

	int updateByPrimaryKey(Income record);
}