package com.xiutao.controller.about;

import com.xiutao.pojo.user.SessionUser;
import com.xiutao.util.Constants;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/aboutController")
public class AboutController {

	@RequestMapping("/showAbout")
	public String showBorrows(Model model, HttpSession session) {
		SessionUser sessionUser= (SessionUser) session.getAttribute(Constants.SESSION_USER_KEY);
		model.addAttribute("sessionUser", sessionUser);
		return "pages/about";
	}
}
