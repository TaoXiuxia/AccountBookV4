package com.xiutao.controller.paymethod;

import com.xiutao.pojo.paymethod.PayMethod;
import com.xiutao.pojo.user.SessionUser;
import com.xiutao.serviceapi.paymethod.PayMethodService;
import com.xiutao.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/payMethodController")
public class PayMethodController {
	private Logger logger=LoggerFactory.getLogger(PayMethodController.class);
	
	private PayMethodService payMethodService;

	public PayMethodService getPayMethodService() {
		return payMethodService;
	}
	
	@Autowired
	public void setPayMethodService(PayMethodService payMethodService) {
		this.payMethodService = payMethodService;
	}

	/**
	 * 收支方式管理页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/showManagePayMethods")
	public String showManagePayMethods(Model model, HttpSession session) {
		SessionUser sessionUser = (SessionUser) session.getAttribute(Constants.SESSION_USER_KEY);
		int userId = sessionUser.getUserId();
		List<PayMethod> incomePayMethods = payMethodService.loadPayMethods(userId, "in");
		List<PayMethod> expenditurePayMethods = payMethodService.loadPayMethods(userId, "ex");
		model.addAttribute("incomePayMethods", incomePayMethods);
		model.addAttribute("expenditurePayMethods", expenditurePayMethods);
		model.addAttribute("sessionUser", sessionUser);
		
		return "pages/managePayMethods";
	}
	
	/**
	 * 修改PayMethod
	 * @param payMethodId
	 * @param payMethodName
	 * @param isCountInThisMonthEx
	 * @param remark
	 */
	@RequestMapping("/changePayMethod")
	public void changeItem(int payMethodId, String payMethodName, int isCountInThisMonthEx, String remark) {
		payMethodService.changePayMethod(payMethodId, payMethodName, isCountInThisMonthEx, remark);
	}
	
	/**
	 * 删除PayMethod
	 * @param payMethodId
	 */
	@RequestMapping("/delePayMethod")
	public void delePayMethod(int payMethodId) {
		payMethodService.delePayMethod(payMethodId);
	}
	
	/**
	 * 增加PayMethod
	 * @param session
	 * @param payMethodName
	 * @param isCountInThisMonthEx
	 * @param remark
	 * @param inOrEx
	 */
	@RequestMapping("/addPayMethod")
	public void addItem(HttpSession session, String payMethodName, int isCountInThisMonthEx, String remark, String inOrEx) {
		SessionUser sessionUser = (SessionUser) session.getAttribute(Constants.SESSION_USER_KEY);
		int userId = sessionUser.getUserId();
		payMethodService.addPayMethod(userId, payMethodName, isCountInThisMonthEx, inOrEx, remark);
	}
	
	/**
	 * PayMethod的上移和下移
	 * @param session
	 * @param inOrEx
	 * @param upAndDown
	 * @return
	 */
	@RequestMapping(value = "/upAndDownPayMethod", produces = "application/json;charset=UTF-8")
	public @ResponseBody
    Map<String ,Object> upAndDownPayMethod(HttpSession session, int payMethodId, String inOrEx, String upAndDown) {
		Map<String, Object> map = new HashMap<String, Object>();
		SessionUser sessionUser = (SessionUser) session.getAttribute(Constants.SESSION_USER_KEY);
		int userId = sessionUser.getUserId();
		String msg = payMethodService.upAndDownPayMethod(userId, payMethodId, inOrEx, upAndDown);
		map.put("info", msg);
		return map;
	}
}
