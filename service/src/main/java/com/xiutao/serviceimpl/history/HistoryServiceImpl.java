package com.xiutao.serviceimpl.history;

import com.xiutao.mapper.expenditure.ExpenditureMapper;
import com.xiutao.mapper.income.IncomeMapper;
import com.xiutao.serviceapi.history.HistoryService;
import com.xiutao.util.Constants;
import com.xiutao.util.NumberFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

public class HistoryServiceImpl implements HistoryService {

	private IncomeMapper incomeMapper;
	private ExpenditureMapper expenditureMapper;

	
	/**
	 * 根据条件查询全部的收入和支出(历史页面)
	 */
	@Override
	public List<Map> loadIncomesAndExpenditure(int userId,String inOrEx, int year, int month, String keyword, String sortBy, int curPage, int totalPages)  {
		int limit = Constants.RECORD_NUM_PER_PAGE;
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		if(year!=-1){
			if(month!=-1){ //指定了年份和月份
				String monthStr;
				if(month<10){
					monthStr="0"+month;
				}else{
					monthStr=""+month;
				}
				map.put("beginDate", year+"-"+monthStr+"-01");
				map.put("endDate", year+"-"+monthStr+"-28");  // 数据库中是按照月份查的，因此最后的日期项无所谓
			}else{ // 只指定了年份，没有指定月份
				map.put("beginDate", year+"-01-01");
				map.put("endDate", year+"-12-28");
			}
		}else{ //没有指定时间
			map.put("beginDate", null);
			map.put("endDate", null);
		}
		map.put("inOrEx", inOrEx);
		map.put("userId", userId);
		map.put("keyword", keyword);
		
		if (curPage < 1){  
			curPage = 1;  
	    }else if (curPage > totalPages){  
	    	curPage = totalPages;  
	    } 
	    map.put("sortBy", sortBy);
		map.put("beginRecord", (curPage - 1) * limit < 0 ? 0 : (curPage - 1)*limit);
		map.put("limit", limit);
		List<Map>list = incomeMapper.selectIncomesAndExpenditure(map);

		// 把数据库里取出来的float转成BigDecimal，重新放回list
		for(Map maptemp: list){
			BigDecimal money = new BigDecimal( maptemp.get("money").toString());
			maptemp.put("money", money.toString());
		}
		return list;
	}
	
	/**
	 * 根据条件查询全部的收入和支出 的记录总数
	 */
	@Override
	public int countIncomesAndExpenditure(int userId,String inOrEx,int year, int month, String keyword){
		HashMap<String, Object> map = new HashMap<String, Object>();
		if(year!=-1){
			if(month!=-1){ //指定了年份和月份
				String monthStr;
				if(month<10){
					monthStr="0"+month;
				}else{
					monthStr=""+month;
				}
				map.put("beginDate", year+"-"+monthStr+"-01");
				map.put("endDate", year+"-"+monthStr+"-28");  // 数据库中是按照月份查的，因此最后的日期项无所谓
			}else{ // 只指定了年份，没有指定月份
				map.put("beginDate", year+"-01-01");
				map.put("endDate", year+"-12-28");
			}
		}else{ //没有指定时间
			map.put("beginDate", null);
			map.put("endDate", null);
		}
		map.put("inOrEx", inOrEx);
		map.put("userId", userId);
		map.put("keyword", keyword);
		
		return incomeMapper.countIncomesAndExpenditure(map); // 根据条件查到的记录的总条数
	}
	
	@Override
	public List<Map> last12Income(int userId){
		Map<String, String>paraMap = new HashMap<String, String>();
		paraMap.put("userId", userId+"");
		List<Map> list = incomeMapper.searchLast12Income(paraMap);
		completMonth(list);
		return sortListByMonth(list);
	}
	
	@Override
	public List<Map> last12Expenditure(int userId){
		Map<String, String>paraMap = new HashMap<String, String>();
		paraMap.put("userId", userId+"");
		List<Map> list = expenditureMapper.searchLast12Expenditure(paraMap);
		completMonth(list);
		return sortListByMonth(list);
	}

	/**
	 * 补全月份，将没有数据的月份 补上0
	 * @param list
	 */
	private void completMonth(List<Map> list) {
		LocalDate date = LocalDate.now();
		for(int i=0;i<12;i++){ 
			String key = date.toString().substring(0, 7);
			boolean containKeyFlag = false;
			for(int j=0;j<list.size();j++){
				Map map = list.get(j);
				if(map.get("month").equals(key)){
					map.put("sumMoney", NumberFormat.save2Decimals((double)map.get("sumMoney")));
					containKeyFlag = true;
				}
			}
			if(containKeyFlag == false){
				Map map = new HashMap();
				map.put("month", key);
				map.put("sumMoney", 0);
				list.add(map);
			}
			date = date.minusMonths(1);
		}
	}
	
	private List<Map> sortListByMonth(List<Map> list){
		String[]arr = new String[12];
		for(int i=0;i<list.size();i++){
			arr[i] = (String)list.get(i).get("month");
		}
		Arrays.sort(arr);

		List<Map> retList = new ArrayList<Map>();
		for(int i=0;i<12;i++){
			for(int j=0;j<list.size();j++){
				if(list.get(j).get("month").equals(arr[i])){
					retList.add(list.get(j));
					list.remove(j);
				}
			}
		}
		return retList;
	}

	/**
	 * 用于图表展示页面
	 * @param userId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	@Override
	public List<Map> selectIncomeGroupByItemName(int userId, String startTime, String endTime) {
		Map<String, String>paraMap = new HashMap<String, String>();
		paraMap.put("userId", userId+"");
		paraMap.put("startTime", startTime);
		paraMap.put("endTime", endTime);
		List<Map> list= incomeMapper.selectIncomeGroupByItemName(paraMap);
		return list;
	}

	/**
	 * 用于图表展示页面
	 * @param userId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	@Override
	public List<Map> selectExpenditureGroupByItemName(int userId, String startTime, String endTime) {
		Map<String, String>paraMap = new HashMap<String, String>();
		paraMap.put("userId", userId+"");
		paraMap.put("startTime", startTime);
		paraMap.put("endTime", endTime);
		List<Map> list= expenditureMapper.selectExpenditureGroupByItemName(paraMap);
		return list;
	}

	/**
	 * 用于图表展示页面
	 * @param userId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	@Override
	public List<Map> selectAllExpenditureGroupByItemName(int userId, String startTime, String endTime) {
		Map<String, String>paraMap = new HashMap<String, String>();
		paraMap.put("userId", userId+"");
		paraMap.put("startTime", startTime);
		paraMap.put("endTime", endTime);
		List<Map> list= expenditureMapper.selectAllExpenditureGroupByItemName(paraMap);
		return list;
	}
	
	//------------------ getter and setter ----------------------
	public IncomeMapper getIncomeMapper() {
		return incomeMapper;
	}
	@Autowired
	public void setIncomeMapper(IncomeMapper incomeMapper) {
		this.incomeMapper = incomeMapper;
	}
	public ExpenditureMapper getExpenditureMapper() {
		return expenditureMapper;
	}
	@Autowired
	public void setExpenditureMapper(ExpenditureMapper expenditureMapper) {
		this.expenditureMapper = expenditureMapper;
	}

	

}
