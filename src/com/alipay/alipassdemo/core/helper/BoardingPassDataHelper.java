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
import com.alipay.alipass.sdk.jsonmodel.TextMessageModel;
import com.alipay.alipassdemo.biz.Constant;

/**
 * BoardingPass模板类数据绑定助手
 * <li>此处商戶可以根据自己的业务需求来处理：例如 调用后端service获取数据，然后组装等等</li>
 * 
 * @author junhua.pan
 * @version $Id: BoardingPassDataHelper.java, v 0.1 2013-6-19 上午11:04:45 junhua.pan Exp $
 */
public class BoardingPassDataHelper {

    public static AlipassModel warpData(AlipassModel alipassData, HashMap<PicName, File> picMap,
                                        PlatformModel platform, String serialNumber, Context context) {

        // 基础信息
        EVoucherInfoModel voucher = new EVoucherInfoModel();

        voucher.setTitle("中国国航机票");
        voucher.setStartDate("2013-07-07 15:15:00");
        voucher.setEndDate("2113-07-07 18:30:00");
        voucher.setType(PassType.boardingPass);
        voucher.setProduct(ProductType.air);

        //eInfo节点数据封装
        EInfoModel einfo = setEinfo(context);
        voucher.setEinfo(einfo);

        //operation区域封装
        List<OperationModel> operations = setOperation();
        voucher.setOperation(operations);

        // 商户信息
        MerchantModel merchant = new MerchantModel();
        merchant.setMerName("国航票务");
        merchant.setMerTel("95583");
        merchant.setMerInfo("http://www.airchina.com");

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
        app.setLabel("国航app");
        app.setMessage("提供优质，快捷机票服务");
        app.setApp(getAppDetailModel());

        alipassData.setAppInfo(app);

        return alipassData;
    }

    private static EInfoModel setEinfo(Context context) {
        // EINfo信息
        // 头部
        List<EInfoUnitModel> headFields = new ArrayList<EInfoUnitModel>();

        EInfoUnitModel head = new EInfoUnitModel();
        head.setKey("fltDate");
        head.setLabel("航班日期");
        head.setValue("2013-07-07");

        headFields.add(head);

        // 主要区域
        List<EInfoUnitModel> primaryFields = new ArrayList<EInfoUnitModel>();

        EInfoUnitModel from = new EInfoUnitModel();
        from.setKey("from");
        from.setLabel("乌鲁木齐地窝堡机场");
        from.setValue("UIN");

        EInfoUnitModel to = new EInfoUnitModel();
        to.setKey("to");
        to.setLabel("上海虹桥机场T2");
        to.setValue("SHA");

        primaryFields.add(from);
        primaryFields.add(to);

        // 次要区域
        List<EInfoUnitModel> secondaryFields = new ArrayList<EInfoUnitModel>();

        EInfoUnitModel startTime = new EInfoUnitModel();
        startTime.setKey("startTime");
        startTime.setLabel("计划起飞时间");
        startTime.setValue("15:15");

        EInfoUnitModel fltNo = new EInfoUnitModel();
        fltNo.setKey("fltNo");
        fltNo.setLabel("航班号");
        fltNo.setValue("CA1157");

        EInfoUnitModel arriveTime = new EInfoUnitModel();
        arriveTime.setKey("arriveTime");
        arriveTime.setLabel("计划到达时间");
        arriveTime.setValue("18:30");

        secondaryFields.add(startTime);
        secondaryFields.add(fltNo);
        secondaryFields.add(arriveTime);

        // 辅助区域
        List<EInfoUnitModel> auxiliaryFields = new ArrayList<EInfoUnitModel>();

        EInfoUnitModel pepole = new EInfoUnitModel();
        pepole.setKey("passenger");
        pepole.setLabel("乘机人");
        pepole.setValue("张三、李四、王五、赵六");

        EInfoUnitModel pNo = new EInfoUnitModel();
        pNo.setKey("passengerSize");
        pNo.setLabel("乘机人数");
        pNo.setValue("4");

        auxiliaryFields.add(pepole);
        auxiliaryFields.add(pNo);

        // 背部区域
        List<EInfoUnitModel> backFields = new ArrayList<EInfoUnitModel>();

        EInfoUnitModel passport = new EInfoUnitModel();
        passport.setKey("passport");
        passport.setLabel("乘机人");
        passport.setValue("张三、李四、王五、赵六");

        EInfoUnitModel residence = new EInfoUnitModel();
        residence.setKey("residence");
        residence.setLabel("代理商");
        residence.setValue("国航票务");

        EInfoUnitModel terms = new EInfoUnitModel();
        terms.setKey("terms");
        terms.setLabel("特殊规定");
        terms.setValue("支付价与票面价不符，行程单与实际支付价格不一致，差价不退还，若订单生成，则表示您已同意此规则！");

        EInfoUnitModel annouce = new EInfoUnitModel();
        annouce.setKey("annouce");
        annouce.setLabel("声明内容");
        annouce.setValue("该航班状态信息仅供参考，如航班状态与实际情况不符，请以该航班实际情况为准");

        EInfoUnitModel serviceTel = new EInfoUnitModel();
        serviceTel.setKey("serviceTel");
        serviceTel.setLabel("国航销售服务热线");
        serviceTel.setValue("95583");
        serviceTel.setType("tel");

        backFields.add(passport);
        backFields.add(residence);
        backFields.add(terms);
        backFields.add(annouce);
        backFields.add(serviceTel);

        EInfoModel einfo = new EInfoModel();
        SharedPreferences settings = context.getSharedPreferences(Constant.ALIPASSDEMO_SETTING, 0);
        int index = settings.getInt(Constant.BOARDINGPASS_SETTING, 0);
        settings.edit().putInt(Constant.BOARDINGPASS_SETTING, ++index).commit();

        einfo.setLogoText("【" + index + "】中国国际航空公司");//pass 抬头显示的文字
        einfo.setSecondLogoText(null);
        einfo.setHeadFields(headFields);
        einfo.setPrimaryFields(primaryFields);
        einfo.setSecondaryFields(secondaryFields);
        einfo.setAuxiliaryFields(auxiliaryFields);
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

        operationApp.setOpLabel("在线值机");
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
        t1.setLabel("机票订单");
        t1.setValue("CA201306150078");

        messages.add(t1);

        operationText.setTextMessage(messages);

        // 其他格式的Format，Message为普通的String字符串
        OperationModel operationBarcde = new OperationModel();
        operationBarcde.setOpLabel("机票订单：CA201306150078");
        operationBarcde.setOpMessageEncoding("UTF-8");
        operationBarcde.setOpFormat(OperationFormatType.barcode.getTypeName());
        operationBarcde.setOpMessage("CA201306150078");

        OperationModel operationQrcode = new OperationModel();
        operationQrcode.setOpLabel("机票订单：CA201306150078");
        operationQrcode.setOpMessageEncoding("UTF-8");
        operationQrcode.setOpFormat(OperationFormatType.qrcode.getTypeName());
        operationQrcode.setOpMessage("CA201306150078");
        
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
        appDetailModel.setAndroid_appid("com.rytong.airchina");
        appDetailModel.setAndroid_launch("com.rytong.airchina");
        appDetailModel
            .setAndroid_download("http://m.airchina.com/dl/airchina?target=android/android1.5");

        appDetailModel.setIos_appid("361964929");
        appDetailModel.setIos_launch("AirChinaiPhone://");
        appDetailModel.setIos_download("http://itunes.apple.com/cn/app/id361964929?mt=8");

        return appDetailModel;
    }
}
