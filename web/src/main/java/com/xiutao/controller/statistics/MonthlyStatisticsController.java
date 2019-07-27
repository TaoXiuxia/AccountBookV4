package com.xiutao.controller.statistics;

import com.xiutao.serviceapi.statistic.MonthlyStatisticsService;
import com.xiutao.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/monthlyStatisticsController")
public class MonthlyStatisticsController {
	private MonthlyStatisticsService monthlyStatisticsService;

	public MonthlyStatisticsService getMonthlyStatisticsService() {
		return monthlyStatisticsService;
	}

	@Autowired
	public void setMonthlyStatisticsService(MonthlyStatisticsService monthlyStatisticsService) {
		this.monthlyStatisticsService = monthlyStatisticsService;
	}

	/**
	 * 增加Balance
	 * 
	 */
	@RequestMapping("/addBalance")
	public void addBalance(float actualBalance, HttpSession session) {
		int userId = Integer.valueOf((String)session.getAttribute(Constants.USER_ID));
		monthlyStatisticsService.addBalance(actualBalance, userId);
	}

	/**
	 * 修改balance
	 * @param balanceId
	 * @param changed_balance
	 */
	@RequestMapping("/changeBalance")
	public void changeBalance(int balanceId,float changed_balance) {
		monthlyStatisticsService.changeBalance(balanceId, changed_balance);
	}
}
