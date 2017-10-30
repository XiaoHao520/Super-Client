package com.superschool.tools;

import android.content.Context;
import android.provider.ContactsContract;
import android.util.Log;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.CosXmlServiceConfig;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.model.CosXmlResultListener;
import com.tencent.cos.xml.model.object.PutObjectRequest;
import com.tencent.cos.xml.model.object.PutObjectResult;
import com.tencent.cos.xml.model.tag.CORSConfiguration;
import com.tencent.cos.xml.sign.CosXmlCredentialProvider;
import com.tencent.cos.xml.sign.CosXmlLocalCredentialProvider;
import com.tencent.cos.xml.utils.MD5Utils;
import com.tencent.qcloud.network.QCloudProgressListener;
import com.tencent.qcloud.network.exception.QCloudException;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.SimpleFormatter;


/**
 * Created by XIAOHAO-PC on 2017-10-29.
 */

/*
super-1252119503.cos.ap-guangzhou.myqcloud.com
super-1252119503.cos.guangzhou.myqcloud.com*/
public class FileUpload {
   private static String buket="super";
    private static String appid = "1252119503";
    private static String region = "ap-guangzhou";
    private static String secretId = "AKID30lLo6m6qQo00pf1WqyufFcnmZ9XigV8";
    private static String secretKey = "Yt43ua5OVdhaHl4B7qNkwgIix1wfDZSP";
    private static List<String>cosUrls;

    public static String uploadSingle(String filepath, Context context) throws Exception {
        Date date;
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        date = new Date();
        String[] filenames = filepath.split("/");
        String filename = filenames[filenames.length - 1];
        String cosName = MD5Utils.getMD5FromString(format.format(date)) + filename;
        //创建 CosXmlServiceConfig 对象，根据需要修改默认的配置参数
        long keyDuration = 600;
        long signDuration = 600;
        CosXmlServiceConfig cosXmlServiceConfig = new CosXmlServiceConfig(appid, region);
        //创建获取签名类
        CosXmlCredentialProvider cosXmlCredentialProvider = new CosXmlLocalCredentialProvider(secretId, secretKey, keyDuration);

        //创建 CosXmlService 对象，实现对象存储服务各项操作.
        CosXmlService cosXmlService = new CosXmlService(context, cosXmlServiceConfig, cosXmlCredentialProvider);
        PutObjectRequest putObjectRequest = new PutObjectRequest();
        putObjectRequest.setBucket("super");
        putObjectRequest.setCosPath(cosName);
        putObjectRequest.setSrcPath(filepath);
        putObjectRequest.setSign(signDuration, null, null);

        putObjectRequest.setProgressListener(new QCloudProgressListener() {
            @Override
            public void onProgress(long progress, long max) {
                float result = (float) (progress * 100.0 / max);
                Log.w("TEST", "progress =" + (long) result + "%");
            }
        });

//使用同步方法上传
        try {
            final PutObjectResult putObjectResult = cosXmlService.putObject(putObjectRequest);

            //上传失败， 返回httpCode 不在【200，300）之内；
            if (putObjectResult.getHttpCode() >= 300 || putObjectResult.getHttpCode() < 200) {
                StringBuilder stringBuilder = new StringBuilder("Error\n");
                stringBuilder.append(putObjectResult.error.code)
                        .append(putObjectResult.error.message)
                        .append(putObjectResult.error.resource)
                        .append(putObjectResult.error.requestId)
                        .append(putObjectResult.error.traceId);
                Log.w("TEST", stringBuilder.toString());
            }

            //上传成功
            if (putObjectResult.getHttpCode() >= 200 && putObjectResult.getHttpCode() < 300) {
                //上传成功后，则可以拼接访问此文件的地址，格式为：bucket-appid.region.myqcloud.com.cosPath;

                String url="http://"+buket+"-"+appid+".cosgz.myqcloud.com/"+cosName;
                Log.w("TEST", "accessUrl =" + putObjectResult.accessUrl);

                  return url;
            }

        } catch (QCloudException e) {

            //抛出异常
            Log.w("TEST", "exception =" + e.getExceptionType() + "; " + e.getDetailMessage());
        }


        return  null;
    }



      /*


*/


    public static List<String> uploadMulti(ArrayList<String> files, Context context) throws Exception {
        cosUrls=new ArrayList<String>();
        for (int i = 0; i < files.size(); i++) {
            cosUrls.add(uploadSingle(files.get(i), context));
        }

        return cosUrls;

    }
}
