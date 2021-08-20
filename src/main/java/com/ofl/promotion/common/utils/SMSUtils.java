package com.ofl.promotion.common.utils;

import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @Author Mr.quan
 * @Date 2021/8/16 23:23
 */
public class SMSUtils {

    // 短信应用SDK AppID
    private final static int APPID = 1400562193;
    // 短信应用SDK AppKey
    private final static String APPKEY = "7c8d4c5984e24e88c1834231a8c2ceef";
    // 短信模板ID，需要在短信应用中申请
    private final static int TEMPLATEID = 1084039;
    // 签名，使用的是签名内容，而不是签名ID
    private final static String SMSSIGN = "APP推广平台";

    //生成二维码
    public static String sendSMS(HttpServletRequest request, String phoneNumber) {
        //随机生成六位验证码的工具类
        String code = String.valueOf(Math.random()*1000000);;
        try {
            //参数，一定要对应短信模板中的参数顺序和个数，
            String[] params = {code};
            //创建ssender对象
            SmsSingleSender ssender = new SmsSingleSender(APPID, APPKEY);
            //发送
            SmsSingleSenderResult result = ssender.sendWithParam("86", phoneNumber,TEMPLATEID, params, SMSSIGN, "", "");
            if(result.result != 0){
                return null;
            }

        } catch (HTTPException e) {
            // HTTP响应码错误
            e.printStackTrace();
        } catch (JSONException e) {
            // json解析错误
            e.printStackTrace();
        } catch (IOException e) {
            // 网络IO错误
            e.printStackTrace();
        }catch (Exception e) {
            // 网络IO错误
            e.printStackTrace();
        }
        return code;
    }
}