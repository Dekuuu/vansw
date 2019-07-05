package com.ego.password.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ego.pojo.TbUser;
import com.entity.EgoResult;

public interface TbUserService {
/*
 * 根据用户名和密码查询用户信息，登录
 */
	EgoResult login(TbUser user,HttpServletRequest req,HttpServletResponse resp);
	
	/*
	 * 根据UUID查询redis中的用户信息
	 */
	EgoResult selByToken(String token);
	
	/*
	 * 用户退出登录
	 */
	EgoResult logout(String token,HttpServletRequest req,HttpServletResponse resp);
}
