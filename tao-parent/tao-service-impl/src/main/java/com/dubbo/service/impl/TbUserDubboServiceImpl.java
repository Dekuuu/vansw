package com.dubbo.service.impl;

import java.util.List;

import javax.annotation.Resource;

import com.ego.dubbo.service.TbUserDubboService;
import com.ego.mapper.TbUserMapper;
import com.ego.pojo.TbUser;
import com.ego.pojo.TbUserExample;

public class TbUserDubboServiceImpl implements TbUserDubboService{
	@Resource
	private TbUserMapper tbUserMapper;
	/*
	 * 根据用户名和密码查询用户信息
	 */
	@Override
	public TbUser selByPass(TbUser user) {
		TbUserExample example=new TbUserExample();
		example.createCriteria().andUsernameEqualTo(user.getUsername()).andPasswordEqualTo(user.getPassword());
		List<TbUser> sel = tbUserMapper.selectByExample(example);
		if(sel!=null&&sel.size()>0) {
			return sel.get(0);
		}else{
			return null;
		}
	}

}
