package com.ego.manage.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ego.manage.service.PicService;
import com.utils.FtpUtils;
import com.utils.IDUtils;
@Service
public class PicServiceImpl implements PicService{
	@Value("${ftpclient.host}")
	private String host;
	@Value("${ftpclient.port}")
	private int port;
	@Value("${ftpclient.username}")
	private String username;
	@Value("${ftpclient.password}")
	private String password;
	@Value("${ftpclient.basepath}")
	private String basePath;
	@Value("${ftpclient.filepath}")
	private String filePath; 
	@Override
	public Map<String, Object> upload(MultipartFile file) throws IOException {
		String name=IDUtils.genImageName()+file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
		boolean uploadFile = FtpUtils.uploadFile(host, port, username, password, basePath, filePath,name, file.getInputStream());
		Map<String,Object> map=new HashMap<String, Object>();
		if(uploadFile) {
			map.put("error", 0);
			map.put("url", "http://"+host+"/"+name);
		}else {
			map.put("error", 1);
			map.put("message", "图片上传失败");
		}
		return map;
	}

}
