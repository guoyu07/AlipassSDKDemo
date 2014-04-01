package com.alipay.alipassdemo.core.service;

import java.util.Properties;

import com.alipay.alipass.sdk.service.AlipassConfig;

/**
 * 整个alipass需要的环境变量配置 <li>只需要修改静态常量值即可</li> <li>
 * 静态常量的值也可以是来自服务，需要重写setConfig方法：需注意prop中的key值不能改变</li>
 * 
 * @author linan.gln
 * 
 */
public class AlipassConfigImpl implements AlipassConfig {

    // ----------------------------------------基础参数配置--------------------------------------------------
    /** 支付宝开放平台对应的APPID, 上线时替换为新申请的APPID*/
    private static final String APP_ID               = "2013072900000659";

    /** APP_ID对应的私钥 */
    private static final String PRIVATE_KEY_DATA     = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALOi6l60SjkGbFwCdSdbmkVHz7MneoiPAtuSGNfr6vvl42B7tRgmVsWeA/Azzmf0RA9fUwAAOre9bNguF0m18V/tw8nbhNicbN9nRB+MVjSStp3PlDm7fCQNSr03cg1fRzqA/mQ3oOTWHCGIsY55OtTOJEs/EV9SFTzckLxbLSNxAgMBAAECgYEAo+iYf9d0DjptLztS2Jm9109iFi+EoAwV6HGTV5m9bSFjaosWP7Xj4SeAonEy+imo3Hn3Sv3qlfMy2MKdnzVzyrNJw927agJKLjzSYwOhqiK92qIabaeU9MoBja23OTAQT8exxLSAZjMB2o9N3r79HwIBGDTEZESu0c8FCGgz/70CQQDiOm3jUXMWRWlc36jOu7K/mrBid/N7dHBRFAjPdH86EBCAJdlClXY6s5TJsW1TmJyl7FiPTxgNTCPa+kVS4+q3AkEAy0bRopv/9VkmBAUGE8gXoNMM3KFktZ5CX9CyvWAltGVccS5zzepxouMYcpL43QLfJ4djx05g5hXBbii5SdxbFwJAHJJOkJgX1ec8UFt7AWkZQTOzNSx7Fst05/iEyFapPtcKjnS5RLyFqxJFJ1f+O8GZfycjZC4UFpHYMW7MVJSubwJADa5/j51GVgIhD+YyqYQvKvDpjbQDLkIvn3lRfnOJNiIYXbnNq49bBKaaT6Pawi1eTa/MJG7/EJ1oPnzZM8J0EwJBAK5WsIopuEFRWF4v5pitKhd6u5XW90LdI1K6JIi09HzAkgNGeCsscfA/Ncuo7HfoTjt587mL2uPL7KjsnIGpwJM=";

    /** 添加alipass成功之后，支付宝回调的商户的服务地址，如果不需要，可以为空 */
    private static final String ALIPASS_CALLBACK_URL = "";                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                // http://www.alipay.com/alipass/callback.do

    private static final Properties prop = new Properties();

    private Properties initConfig() {

        prop.setProperty(AlipassConfig.ALIPASS_CALLBACK_URL_KEY, ALIPASS_CALLBACK_URL);

        // ---- OpenApi Mode-----
        prop.setProperty(AlipassConfig.PRIVATE_KEY, PRIVATE_KEY_DATA);
        prop.setProperty(AlipassConfig.OPENAPI_APP_ID, APP_ID);

        return prop;
    }

    /**************** 单例模式 ******************/
    private AlipassConfigImpl() {
        initConfig();
    }

    private static AlipassConfigImpl alipassConfig = null;

    public static AlipassConfigImpl getInstance() {
        if (alipassConfig == null) {
            alipassConfig = new AlipassConfigImpl();
        }
        return alipassConfig;
    }
    
    @Override
    public Properties getConfig() {
        return prop;
    }

}
