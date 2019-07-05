package com.ego.redis.service;

public interface JedisDao {
	/*
	 * redis设置值
	 */
	String set(String key,String value);
	
	/*
	 * redis判断值是否存在
	 */
	Boolean exists(String key);
	
	/*
	 * redis删除值
	 */
	Long del(String key);
	
	/*
	 * redis获取值
	 */
	String get(String key);
	
	/*
	 * 设置生命周期
	 */
	long expire(String key,int seconds);
}
