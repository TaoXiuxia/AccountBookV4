
// 登录
// 登录不对输入做校验，只注册才校验
function login() 
{
	var account = $("#account").val();
	var password = $("#password").val();
	var rememberMe = $("#rememberMe").is(':checked');
	$.ajax({
		type: "POST",
		url: "login.action",
		dataType:"json",  
		data: {
			"account":account,
			"password":password,
			"rememberMe":rememberMe
		},
		success: function(msg){
			if(msg.info=="登录成功"){
				window.location.href="../frameController/showframe";
			}else{
				layer.msg(msg.info);
			}
		},
		error: function () {
			alert("登录失败from前台");
		} 
	});
}

// 按 enter 键提交
$(document).keyup(function(event) {
	var code = event.keyCode;
	if (code == 13) {
		login();
	}
})


