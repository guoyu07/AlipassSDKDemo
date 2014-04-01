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
 * EventTicket模板类数据绑定助手
 * <li>此处商戶可以根据自己的业务需求来处理：例如 调用后端service获取数据，然后组装等等</li>
 * 
 * @author junhua.pan
 * @version $Id: BoardingPassDataHelper.java, v 0.1 2013-6-19 上午11:04:45 junhua.pan Exp $
 */
public class EventTicketDataHelper {

    public static AlipassModel warpData(AlipassModel alipassData, HashMap<PicName, File> picMap,
                                        PlatformModel platform, String serialNumber, Context context) {

        // 基础信息
        EVoucherInfoModel voucher = new EVoucherInfoModel();
        voucher.setTitle("生化危机5：惩罚");
        voucher.setStartDate("2013-07-08 15:15:00");
        voucher.setEndDate("2113-07-08 17:30:00");
        voucher.setType(PassType.eventTicket);
        voucher.setProduct(ProductType.movie);

        //eInfo节点数据封装
        EInfoModel einfo = setEinfo(context);
        voucher.setEinfo(einfo);

        //operation区域封装
        List<OperationModel> operations = setOperation();
        voucher.setOperation(operations);

        // 商户信息
        MerchantModel merchant = new MerchantModel();
        merchant.setMerName("网票网");
        merchant.setMerTel("400-678-2005");
        merchant.setMerInfo("http://www.wangpiao.com");

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
        app.setLabel("淘宝电影票");
        app.setMessage("提供最新的电影票信息");
        app.setApp(getAppDetailModel());

        alipassData.setAppInfo(app);

        StyleModel style = new StyleModel();
        style.setBackgroundColor("#f808ec");
        alipassData.setStyle(style);

        return alipassData;
    }

    private static EInfoModel setEinfo(Context context) {

        // EINfo信息
        // 头部
        List<EInfoUnitModel> headFields = new ArrayList<EInfoUnitModel>();

        EInfoUnitModel head = new EInfoUnitModel();
        head.setKey("date");
        head.setLabel("日期");
        head.setValue("2013-07-08");

        headFields.add(head);

        // 主要区域
        List<EInfoUnitModel> primaryFields = new ArrayList<EInfoUnitModel>();

        EInfoUnitModel time = new EInfoUnitModel();
        time.setKey("time");
        time.setLabel("入场时间");
        time.setValue("15:15");

        primaryFields.add(time);

        // 次要区域
        List<EInfoUnitModel> secondaryFields = new ArrayList<EInfoUnitModel>();

        EInfoUnitModel theatre = new EInfoUnitModel();
        theatre.setKey("theatre");
        theatre.setLabel("影院名称");
        theatre.setValue("杭州UME影城");

        secondaryFields.add(theatre);

        // 辅助区域
        List<EInfoUnitModel> auxiliaryFields = new ArrayList<EInfoUnitModel>();

        EInfoUnitModel auditorium = new EInfoUnitModel();
        auditorium.setKey("auditorium");
        auditorium.setLabel("影厅");
        auditorium.setValue("8号厅");

        EInfoUnitModel seat = new EInfoUnitModel();
        seat.setKey("seat");
        seat.setLabel("座位");
        seat.setValue("H5 H6");

        EInfoUnitModel quantity = new EInfoUnitModel();
        quantity.setKey("quantity");
        quantity.setLabel("张数");
        quantity.setValue("2");

        auxiliaryFields.add(auditorium);
        auxiliaryFields.add(seat);
        auxiliaryFields.add(quantity);

        // 背部区域
        List<EInfoUnitModel> backFields = new ArrayList<EInfoUnitModel>();
        EInfoUnitModel addr = new EInfoUnitModel();
        addr.setKey("addr");
        addr.setLabel("影院地址");
        addr.setValue("杭州市西湖区文二西路551号西城广场4楼");
        addr.setType("map");

        EInfoUnitModel sno = new EInfoUnitModel();
        sno.setKey("sno");
        sno.setLabel("序列号");
        sno.setValue("837458694837\n837458694838");

        EInfoUnitModel serviceTel = new EInfoUnitModel();
        serviceTel.setKey("serviceTel");
        serviceTel.setLabel("客服电话");
        serviceTel.setValue("400406506");
        serviceTel.setType("tel");

        backFields.add(addr);
        backFields.add(seat);
        backFields.add(sno);
        backFields.add(serviceTel);

        EInfoModel einfo = new EInfoModel();

        SharedPreferences settings = context.getSharedPreferences(Constant.ALIPASSDEMO_SETTING, 0);
        int index = settings.getInt(Constant.EVENT_TICKET_SETTING, 0);
        settings.edit().putInt(Constant.EVENT_TICKET_SETTING, ++index).commit();

        einfo.setLogoText("【" + index + "】生化危机5：惩罚");//pass 抬头显示的文字
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

        operationApp.setOpLabel("预定最新电影票");
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
        t1.setLabel("序列号");
        t1.setValue("948594854852");

        TextMessageModel t2 = new TextMessageModel();
        t2.setLabel("验证码");
        t2.setValue("4958");

        messages.add(t1);
        messages.add(t2);

        operationText.setTextMessage(messages);

        // 其他格式的Format，Message为普通的String字符串
        OperationModel operationBarcde = new OperationModel();
        operationBarcde.setOpLabel("兑换码:12987312367891237");
        operationBarcde.setOpMessageEncoding("UTF-8");
        operationBarcde.setOpFormat(OperationFormatType.barcode.getTypeName());
        operationBarcde.setOpMessage("12987312367891237");

        OperationModel operationQrcode = new OperationModel();
        operationQrcode.setOpLabel("订单号:948594854852");
        operationQrcode.setOpMessageEncoding("UTF-8");
        operationQrcode.setOpFormat(OperationFormatType.qrcode.getTypeName());
        operationQrcode.setOpMessage("948594854852");

        OperationModel operationImg = new OperationModel();
        operationImg.setOpLabel("QRCODE109192387123");
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
