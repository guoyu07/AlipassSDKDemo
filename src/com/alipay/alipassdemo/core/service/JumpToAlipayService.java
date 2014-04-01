package com.alipay.alipassdemo.core.service;

import android.content.Context;

import com.alipay.alipass.sdk.enums.PassType;
import com.alipay.alipass.sdk.jsonmodel.BaseModel;
import com.alipay.alipass.sdk.service.AlipassGenerateService;
import com.alipay.alipass.sdk.service.impl.AlipassGenerateServiceImpl;
import com.alipay.alipassdemo.biz.Constant;

public class JumpToAlipayService {
	/**
	 * 回调scheme，当支付宝客户端添加pass完成后，会通过该方式通知商户客户端，该scheme请自定义
	 */
	public static final String SOURCEID = "alipayalipassdemo://alipay";

	/**
	 * alipass环境变量配置接口
	 */
	private static final AlipassConfigImpl alipassConfig = AlipassConfigImpl
			.getInstance();

	/**
	 * 跳转到支付宝钱包
	 * 
	 * @param context
	 * @param passPath
	 *            alipass文件路径
	 */
	public static BaseModel jumpToAlipay(Context context, String passPath,
			PassType passType) {
		AlipassGenerateService mAGService = AlipassGenerateServiceImpl
				.getInstance(alipassConfig);
		return mAGService.jumpToAlipay(context, SOURCEID, Constant.PCP_SCHEME
				+ passPath, passType);
	}

}
