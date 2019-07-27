package com.xiutao.controller.income;

import com.xiutao.pojo.user.SessionUser;
import com.xiutao.pojo.item.Item;
import com.xiutao.pojo.paymethod.PayMethod;
import com.xiutao.pojo.income.Income;
import com.xiutao.pojo.balance.Balance;
import com.xiutao.serviceapi.income.IncomeService;
import com.xiutao.serviceapi.item.ItemService;
import com.xiutao.serviceapi.statistic.MonthlyStatisticsService;
import com.xiutao.serviceapi.paymethod.PayMethodService;
import com.xiutao.util.Constants;
import com.xiutao.util.MyDateFormat;
import com.xiutao.util.NumberFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/incomeController")
public class IncomeController {

	private IncomeService incomeService;
	private ItemService itemService;
	private MonthlyStatisticsService monthlyStatisticsService;
	private PayMethodService payMethodService;
	
	public IncomeService getIncomeService() {
		return incomeService;
	}
	@Autowired
	public void setIncomeService(IncomeService incomeService) {
		this.incomeService = incomeService;
	}
	
	
	public MonthlyStatisticsService getMonthlyStatisticsService() {
		return monthlyStatisticsService;
	}
	@Autowired
	public void setMonthlyStatisticsService(MonthlyStatisticsService monthlyStatisticsService) {
		this.monthlyStatisticsService = monthlyStatisticsService;
	}
	
	public ItemService getItemService() {
		return itemService;
	}
	@Autowired
	public void setItemService(ItemService itemService) {
		this.itemService = itemService;
	}

	public PayMethodService getPayMethodService() {
		return payMethodService;
	}
	@Autowired
	public void setPayMethodService(PayMethodService payMethodService) {
		this.payMethodService = payMethodService;
	}

	/**
	 * income页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/showIncome")
	public String showIncomes(Model model, HttpSession session) {
		Map<String, Float> map = monthlyStatistics(session);
		SessionUser sessionUser= (SessionUser) session.getAttribute(Constants.SESSION_USER_KEY);
		int userId = sessionUser.getUserId();
		
		//页面上的统计信息
		model.addAttribute("totalIncome", NumberFormat.save2Decimals(map.get("monthlyIncome")));
		model.addAttribute("totalExpenditure", NumberFormat.save2Decimals(map.get("monthlyExpenditure")));
		model.addAttribute("notActualExpenditure", NumberFormat.save2Decimals(map.get("notActualExpenditure")));
		model.addAttribute("balanceInBeginOfMonth", NumberFormat.save2Decimals(map.get("balanceInBeginOfMonth")));
		model.addAttribute("balanceId_InBeginOfMonth", NumberFormat.save2Decimals(map.get("balanceId_InBeginOfMonth")));
		model.addAttribute("balanceShould", NumberFormat.save2Decimals(map.get("balanceShould")));
		model.addAttribute("actualBalance", NumberFormat.save2Decimals(map.get("actualBalance")));
		model.addAttribute("actualBalanceId", NumberFormat.save2Decimals(map.get("actualBalanceId")));
		model.addAttribute("actualExpenditure", NumberFormat.save2Decimals(map.get("actualExpenditure")));
		
		// income list
		List<Income> incomes = incomeService.loadIncomes(userId);
		model.addAttribute("incomes", incomes);
		
		// 收入项 list
		List<Item> items = itemService.loadIncomeItems(userId);
		model.addAttribute("items", items);

		//收入方式list
		List<PayMethod> payMethods = payMethodService.loadPayMethods(userId, "in");
		model.addAttribute("payMethods", payMethods);
		
		model.addAttribute("sessionUser", sessionUser);

		return "pages/income";
	}

	/**
	 * 增加income
	 * 
	 * @param item
	 * @param money
	 * @param remark
	 */
	@RequestMapping("/addIncome")
	public void addIncomes(String date, int item, float money, String moneyType, String remark, HttpSession session) {
		int userId = Integer.valueOf(String.valueOf(session.getAttribute(Constants.USER_ID)));
		incomeService.addIncome(userId, date, item, money, moneyType, remark);
	}
	
	/**
	 * 修改收入
	 * 
	 * @param incomeId
	 * @param money
	 * @param itemId
	 * @param remark
	 */
	@RequestMapping("/changeIncome")
	public void changeIncome(int incomeId, float money, String moneyType, int itemId, String remark, String date) {
		incomeService.changeIncome(incomeId, money, moneyType, itemId, remark, MyDateFormat.dateFormat(date));
	}

	/**
	 * 删除收入
	 * 
	 * @param incomeId
	 * @param session
	 */
	@RequestMapping("/deleIncome")
	public void deleIncome(int incomeId, HttpSession session) {
		incomeService.deleIncome(incomeId);
	}
	
	/**
	 * 在收入和支出页面上 统计部分的内容 
	 * 这部分和ExpenditureController的相同方法完全一样
	 * 
	 * @param session
	 * @return
	 */
	public Map<String,Float> monthlyStatistics(HttpSession session) {
		int userId = (int)session.getAttribute(Constants.USER_ID);
		Map<String,Float>map = new HashMap<String, Float>();
		
		// 月收入
		float monthlyIncome = monthlyStatisticsService.monthlyIncome(userId); 
		map.put("monthlyIncome", monthlyIncome);
		
		// 月支出
		float monthlyExpenditure = monthlyStatisticsService.monthlyExpenditure(userId);  
		map.put("monthlyExpenditure", monthlyExpenditure);
		
		// 月支出中花呗与信用卡的数额
		float notActualExpenditure = monthlyStatisticsService.notActualExpenditure(userId);
		map.put("notActualExpenditure", notActualExpenditure);
		
		// 本月实际支出 
		float actualExpenditure = monthlyExpenditure - notActualExpenditure;
		map.put("actualExpenditure", actualExpenditure);
		
		// 本月初（上月末）结余
		Balance balance_InBeginOfMonth = monthlyStatisticsService.balanceInBeginOfMonth(userId);
		float actualBalance_InBeginOfMonth = balance_InBeginOfMonth.getActualBalance();
		map.put("balanceInBeginOfMonth", actualBalance_InBeginOfMonth);
		map.put("balanceId_InBeginOfMonth", balance_InBeginOfMonth.getId() + 0f); 
		
		// 本月应结余 ==> 月初结余+月收入- (月支出-花呗/信用卡)
		float balanceShould = actualBalance_InBeginOfMonth + monthlyIncome - (monthlyExpenditure - notActualExpenditure);
		map.put("balanceShould", balanceShould);
		
		// 本月实际结余
		Balance balanceOfThisMonth = monthlyStatisticsService.balanceOfThisMonth(userId);
		float actualBalance; // 本月实际结余
		float actualBalanceId;
		if(balanceOfThisMonth == null){
			actualBalance = -1;
			actualBalanceId = -1;
		}else{
			actualBalance = balanceOfThisMonth.getActualBalance();
			actualBalanceId = balanceOfThisMonth.getId();
		}
		map.put("actualBalance", actualBalance);
		map.put("actualBalanceId", actualBalanceId);
		
		return map;
	}
}
