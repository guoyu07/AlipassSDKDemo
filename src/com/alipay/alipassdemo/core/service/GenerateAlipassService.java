/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.alipay.alipassdemo.core.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Random;

import android.content.Context;
import android.util.Log;

import com.alipay.alipass.sdk.enums.PassType;
import com.alipay.alipass.sdk.enums.PicName;
import com.alipay.alipass.sdk.enums.Result;
import com.alipay.alipass.sdk.jsonmodel.AlipassModel;
import com.alipay.alipass.sdk.jsonmodel.PlatformModel;
import com.alipay.alipass.sdk.jsonmodel.ResponseModel;
import com.alipay.alipass.sdk.service.AlipassConfig;
import com.alipay.alipass.sdk.service.AlipassGenerateService;
import com.alipay.alipass.sdk.service.impl.AlipassGenerateServiceImpl;
import com.alipay.alipass.sdk.utils.FileUtils;
import com.alipay.alipassdemo.biz.util.FileUtil;
import com.alipay.alipassdemo.core.helper.BoardingPassDataHelper;
import com.alipay.alipassdemo.core.helper.CouponDataHelper;
import com.alipay.alipassdemo.core.helper.EventTicketDataHelper;

/**
 * Alipass SDK测试
 * 
 * @author junhua.pan
 * @version $Id: GenerateAlipassService.java,v 0.1 2013-5-4 下午9:22:40 junhua.pan
 *          Exp $
 */
public class GenerateAlipassService {

    /** alipass环境变量配置接口 */
    private static final AlipassConfigImpl      alipassConfig = AlipassConfigImpl.getInstance();

    /** alipassSDK 接口 */
    private static final AlipassGenerateService mAGService    = AlipassGenerateServiceImpl
                                                                  .getInstance(alipassConfig);

    /**
     * 生成alipass文件，并保存到sdcard
     * 
     * @param context
     * @param passType
     */
    public static void generateAlipass(Context context, PassType passType) {
        try {
            // 组装pass数据
            AlipassModel alipassData = warpAlipassData(context, passType);

            PlatformModel pm = alipassData.getPlatform();
            pm.setChannelID(alipassConfig.getConfig().getProperty(AlipassConfig.OPENAPI_APP_ID,
                null));
            alipassData.setPlatform(pm);

            // 生产alipass文件
            ResponseModel responseModel = mAGService.generatePass(alipassData);
            if (Result.SUCCESS.equals(responseModel.getResult())) {

                FileUtil.saveAlipassToSdcard(context, responseModel.getAlipass(), passType);

                Log.i("AlipassSdkDemo --> generateAlipass", "generateAlipass success!");
            }

        } catch (IOException e) {
            Log.e("AlipassSdkDemo --> generateAlipass error", e.getMessage());
        }
    }

    private static AlipassModel warpAlipassData(Context context, PassType passType)
                                                                                   throws IOException {

        String cacheDir = context.getCacheDir().getPath();
        Log.i("AlipassSdkDemo --> generateAlipass cacheDir:", "" + cacheDir);

        // For Example: Atention please, this just only for example
        String logoPng = "pic/logo_" + passType.name() + ".png";
        String iconPng = "pic/icon_" + passType.name() + ".png";
        String stripPng = "pic/strip_" + passType.name() + ".png";

        HashMap<PicName, File> picMap = new HashMap<PicName, File>();

        setPassPic(context, logoPng, "logo", picMap);
        setPassPic(context, iconPng, "icon", picMap);
        if (passType != PassType.boardingPass) {
            setPassPic(context, stripPng, "strip", picMap);
        }

        // 平台信息
        PlatformModel platform = new PlatformModel();

        String channelID = alipassConfig.getConfig()
            .getProperty(AlipassConfig.OPENAPI_APP_ID, null);
        String webserviceUrl = alipassConfig.getConfig().getProperty(
            AlipassConfig.ALIPASS_CALLBACK_URL_KEY, null);

        // channelID
        platform.setChannelID(channelID == null ? "" : channelID.trim());
        platform.setWebServiceUrl(webserviceUrl == null ? "" : webserviceUrl.trim());

        // 根据不同的卡券类型，组装不同的alipass生成请求
        AlipassModel alipassModel = new AlipassModel();

        // 最后一个参数表示卡券类型
        // TODO 请注意：商户使用时，请只用外部交易号作为serialNumber，便于查找与更新
        String serialNumber = generateNumber(8);

        alipassModel = choosePassType(alipassModel, picMap, platform, serialNumber, passType,
            context);

        // 设置alipass存储目录，此处不能省略
        alipassModel.setCacheDir(cacheDir);

        return alipassModel;
    }

    /**
     * 组装不同类型的pass数据
     * 
     * @param alipassModel
     * @param picMap
     * @param platform
     * @param serialNumber
     * @param passType
     * @return
     */
    private static AlipassModel choosePassType(AlipassModel alipassModel,
                                               HashMap<PicName, File> picMap,
                                               PlatformModel platform, String serialNumber,
                                               PassType passType, Context context) {

        if (passType.equals(PassType.boardingPass)) {

            return BoardingPassDataHelper.warpData(alipassModel, picMap, platform, serialNumber,
                context);

        } else if (passType.equals(PassType.coupon)) {

            return CouponDataHelper.warpData(alipassModel, picMap, platform, serialNumber, context);

        } else if (passType.equals(PassType.eventTicket)) {

            return EventTicketDataHelper.warpData(alipassModel, picMap, platform, serialNumber,
                context);
        }
        return null;
    }

    /**
     * 此demo采用的随机数字：生成随机Serialnumber
     * 
     * 请注意：商户使用时，请只用外部交易号最为serialNumber，便于查找与更新
     * 
     * @param codeLength
     * @return
     */
    @Deprecated
    private static String generateNumber(int codeLength) {
        // 10个数字
        final int maxNum = 8;
        int i; // 生成的随机数
        int count = 0; // 生成的密码的长度
        char[] str = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

        StringBuffer pwd = new StringBuffer("");
        Random r = new Random();
        while (count < codeLength) {
            // 生成随机数，取绝对值，防止生成负数
            i = Math.abs(r.nextInt(maxNum)); // 生成的数最大为36-1

            if (i >= 0 && i < str.length) {
                pwd.append(str[i]);
                count++;
            }
        }
        return pwd.toString();
    }

    private static void setPassPic(Context context, String picPath, String picFileName,
                                   HashMap<PicName, File> picMap) {
        InputStream input = null;
        try {

            input = context.getAssets().open(picPath);
            String stripPath = context.getCacheDir().getPath() + "/AlipassPic/" + picFileName
                               + ".png";
            FileUtils.saveFile(input, stripPath);

            File pngFile = new File(stripPath);

            picMap.put(PicName.valueOf(picFileName), pngFile);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
