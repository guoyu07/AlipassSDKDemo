/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.alipay.alipassdemo.core.helper;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;

import com.alipay.alipass.sdk.enums.OperationFormatType;
import com.alipay.alipass.sdk.enums.PassType;
import com.alipay.alipass.sdk.enums.PicName;
import com.alipay.alipass.sdk.enums.ProductType;
import com.alipay.alipass.sdk.jsonmodel.AlipassModel;
import com.alipay.alipass.sdk.jsonmodel.AppDetailModel;
import com.alipay.alipass.sdk.jsonmodel.AppInfoModel;
import com.alipay.alipass.sdk.jsonmodel.EInfoModel;
import com.alipay.alipass.sdk.jsonmodel.EInfoUnitModel;
import com.alipay.alipass.sdk.jsonmodel.EVoucherInfoModel;
import com.alipay.alipass.sdk.jsonmodel.FileInfoModel;
import com.alipay.alipass.sdk.jsonmodel.ImgMessageModel;
import com.alipay.alipass.sdk.jsonmodel.MerchantModel;
import com.alipay.alipass.sdk.jsonmodel.OperationModel;
import com.alipay.alipass.sdk.jsonmodel.PlatformModel;
import com.alipay.alipass.sdk.jsonmodel.StyleModel;
import com.alipay.alipass.sdk.jsonmodel.TextMessageModel;
import com.alipay.alipassdemo.biz.Constant;

/**
 * Coupon模板类数据绑定助手
 * <li>此处商戶可以根据自己的业务需求来处理：例如 调用后端service获取数据，然后组装等等</li>
 * 
 * @author junhua.pan
 * @version $Id: BoardingPassDataHelper.java, v 0.1 2013-6-19 上午11:04:45 junhua.pan Exp $
 */
public class CouponDataHelper {

    public static AlipassModel warpData(AlipassModel alipassData, HashMap<PicName, File> picMap,
                                        PlatformModel platform, String serialNumber, Context context) {

        // 基础信息
        EVoucherInfoModel voucher = new EVoucherInfoModel();
        voucher.setTitle("如家快捷酒店-99元重磅特惠");
        voucher.setStartDate("2013-06-16 00:00:00");
        voucher.setEndDate("2114-06-18 23:59:59");
        voucher.setType(PassType.coupon);
        voucher.setProduct(ProductType.free);

        //eInfo节点数据封装
        EInfoModel einfo = setEinfo(context);
        voucher.setEinfo(einfo);

        //operation区域封装
        List<OperationModel> operations = setOperation();
        voucher.setOperation(operations);

        // 商户信息
        MerchantModel merchant = new MerchantModel();
        merchant.setMerName("如家快捷酒店连锁");
        merchant.setMerTel("400-820-3333");
        merchant.setMerInfo("http://www.homeinns.com");

        // 文件信息
        FileInfoModel fileInfo = new FileInfoModel();
        fileInfo.setSerialNumber(serialNumber);
        fileInfo.setFormatVersion("2");

        // alipassModel
        alipassData.setFileInfo(fileInfo);
        alipassData.setPlatform(platform);
        alipassData.setMerchant(merchant);
        alipassData.setEvoucherInfo(voucher);
        alipassData.setPicMap(picMap);

        AppInfoModel app = new AppInfoModel();
        app.setLabel("淘宝券券");
        app.setMessage("用最省的钱，享受更品质的生活！");
        app.setApp(getAppDetailModel());

        alipassData.setAppInfo(app);

        StyleModel style = new StyleModel();
        style.setBackgroundColor("rgb(220,20,60)");
        alipassData.setStyle(style);

        return alipassData;
    }

    private static EInfoModel setEinfo(Context context) {
        // EINfo信息
        // 头部
        List<EInfoUnitModel> headFields = new ArrayList<EInfoUnitModel>();

        EInfoUnitModel head = new EInfoUnitModel();
        head.setKey("endDate");
        head.setLabel("截至日期");
        head.setValue("2014.06.18");

        headFields.add(head);

        // 主要区域  demo中使用了strip圖片，所以此区域不显示内容；如果没有strip图片，则需要增加primaryFields中的值

        // 次要区域
        List<EInfoUnitModel> secondaryFields = new ArrayList<EInfoUnitModel>();

        EInfoUnitModel validDate = new EInfoUnitModel();
        validDate.setKey("validDate");
        validDate.setLabel("");
        validDate.setValue("有效期至： 2014-06-18 23:59:59");

        secondaryFields.add(validDate);

        // 辅助区域  coupon模板不显示，所以不用設置

        // 背部区域
        List<EInfoUnitModel> backFields = new ArrayList<EInfoUnitModel>();

        EInfoUnitModel description = new EInfoUnitModel();
        description.setKey("description");
        description.setLabel("优惠内容");
        description
            .setValue("1、入住如家、莫泰酒店均有机会享受“99风暴”优惠（即99元、199元、299元等优惠价）\n2、房间有限，先订先得，不与其它优惠同享\n3、各酒店具体价格以官网公示为准");

        EInfoUnitModel shops = new EInfoUnitModel();
        shops.setKey("shops");
        shops.setLabel("可用门店");
        shops.setValue("全国大部分如家、莫泰酒店");

        EInfoUnitModel disclaimer = new EInfoUnitModel();
        disclaimer.setKey("disclaimer");
        disclaimer.setLabel("免责声明");
        disclaimer
            .setValue("99元重磅特惠\n除特殊注明外，本优惠不能与其他优惠同时享受； 本优惠最终解释权归商家所有，如有疑问请与商家联系。 提示：为了使您得到更好的服务，请在进店时出示本券。");

        EInfoUnitModel serviceTel = new EInfoUnitModel();
        serviceTel.setKey("serviceTel");
        serviceTel.setLabel("如家快捷酒店连锁订房热线");
        serviceTel.setValue("400-820-3333");
        serviceTel.setType("tel");

        backFields.add(description);
        backFields.add(shops);
        backFields.add(disclaimer);
        backFields.add(serviceTel);

        EInfoModel einfo = new EInfoModel();

        SharedPreferences settings = context.getSharedPreferences(Constant.ALIPASSDEMO_SETTING, 0);
        int index = settings.getInt(Constant.COUPON_SETTING, 0);
        settings.edit().putInt(Constant.COUPON_SETTING, ++index).commit();

        einfo.setLogoText("【" + index + "】优惠：如家快捷酒店-99风暴");//pass 抬头显示的文字
        einfo.setSecondLogoText(null);
        einfo.setHeadFields(headFields);
        einfo.setPrimaryFields(null);
        einfo.setSecondaryFields(secondaryFields);
        einfo.setAuxiliaryFields(null);
        einfo.setBackFields(backFields);

        return einfo;
    }

    /**
     * 设置操作区域operation信息
     * <li>1、如果Format类型为 app，则message类型为AppDetailModel</li>
     * <li>2、如果Format类型为Text，那么message类型为List<TextMessageModel></li>
     * <li>3、Format为其他类型，则message类型为String</li>
     * 
     * @return OperationModel
     */
    private static List<OperationModel> setOperation() {

        // Format类型为 app，则message类型为AppDetailModel
        OperationModel operationApp = new OperationModel();

        operationApp.setOpLabel("获取更多优惠信息");
        operationApp.setOpMessageEncoding("UTF-8");
        operationApp.setOpFormat(OperationFormatType.app.getTypeName());

        operationApp.setAppMessage(getAppDetailModel());

        // 2、如果Format类型为Text，那么message类型为List<TextMessageModel>
        OperationModel operationText = new OperationModel();
        operationText.setOpFormat(OperationFormatType.text.getTypeName());
        operationText.setOpLabel("");
        operationText.setOpMessageEncoding("UTF-8");

        List<TextMessageModel> messages = new ArrayList<TextMessageModel>();
        TextMessageModel t1 = new TextMessageModel();
        t1.setLabel("兑换码");
        t1.setValue("20130678904");

        TextMessageModel t2 = new TextMessageModel();
        t2.setLabel("验证码");
        t2.setValue("xte76123rt");

        messages.add(t1);
        messages.add(t2);

        operationText.setTextMessage(messages);

        // 其他格式的Format，Message为普通的String字符串
        OperationModel operationBarcde = new OperationModel();
        operationBarcde.setOpLabel("订单号：45612346579465");
        operationBarcde.setOpMessageEncoding("UTF-8");
        operationBarcde.setOpFormat(OperationFormatType.barcode.getTypeName());
        operationBarcde.setOpMessage("45612346579465");

        OperationModel operationQrcode = new OperationModel();
        operationQrcode.setOpLabel("订单号：45612346579465");
        operationQrcode.setOpMessageEncoding("UTF-8");
        operationQrcode.setOpFormat(OperationFormatType.qrcode.getTypeName());
        operationQrcode.setOpMessage("45612346579465");

        OperationModel operationImg = new OperationModel();
        operationImg.setOpLabel("45612346579465");
        operationImg.setOpMessageEncoding("UTF-8");
        operationImg.setOpFormat(OperationFormatType.img.getTypeName());
        ImgMessageModel imgMessage = new ImgMessageModel();
        imgMessage.setImg("http://lib.2weima.org/wp-content/uploads/2013/02/api-350x350.png");//二维码图片地址
        operationImg.setImgMessage(imgMessage);

        // 把所有的Operation放到集合中
        List<OperationModel> operations = new ArrayList<OperationModel>();
        operations.add(operationImg);
        operations.add(operationApp);
        operations.add(operationText);
        operations.add(operationBarcde);
        operations.add(operationQrcode);

        return operations;
    }

    /**
     * app信息
     * 
     * @return
     */
    private static AppDetailModel getAppDetailModel() {
        AppDetailModel appDetailModel = new AppDetailModel();
        appDetailModel.setAndroid_appid("com.taobao.ecoupon");
        appDetailModel.setAndroid_launch("com.taobao.ecoupon");
        appDetailModel
            .setAndroid_download("http://download.taobaocdn.com/freedom/17988/andriod/Ecoupon_2.0.1_taobao_wap.apk");

        appDetailModel.setIos_appid("583295537");
        appDetailModel.setIos_launch("taobaolife://");
        appDetailModel.setIos_download("https://itunes.apple.com/cn/app/id583295537");

        return appDetailModel;
    }
}
