package com.alipay.alipassdemo.biz;

import com.alipay.alipass.sdk.enums.PassType;

public class AlipassBean {

	/**
	 * alipass文件名
	 */
	private String name;
	/**
	 * alipass文件路径
	 */
	private String path;
	
	private PassType passType;
	
	public AlipassBean(){
		
	}
	
	public AlipassBean(String name, String path, PassType passType){
		this.name = name;
		this.path = path;
		this.passType = passType;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getPath() {
		return path;
	}
	
	public void setPath(String path) {
		this.path = path;
	}

	public PassType getPassType() {
		return passType;
	}

	public void setPassType(PassType passType) {
		this.passType = passType;
	}
	
}
