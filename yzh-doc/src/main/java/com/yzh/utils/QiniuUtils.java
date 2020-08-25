package com.yzh.utils;

import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.qiniu.util.Base64;
import com.qiniu.util.StringMap;
import com.qiniu.util.UrlSafeBase64;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
@Component
public class QiniuUtils {
    @Value("${qiniu.AK}")
    private String ACCESS_KEY;
    @Value("${qiniu.SK}")
    private String SECRET_KEY;
    @Value("${qiniu.bucketname}")// 要上传的空间
    private String bucketname;
    @Value("${qiniu.DOMAIN}")
    private String domain;//域名

    //private static final String style = "自定义的图片样式";
    private Auth getAuth() {
        return Auth.create(ACCESS_KEY, SECRET_KEY);// 密钥
    }
    public String getUpToken() {

        return getAuth().uploadToken(bucketname, null, 3600, new StringMap().put("insertOnly", 1));
    }

    // 普通上传
    public String upload(InputStream filePath) throws IOException {
        Configuration cfg = new Configuration(Zone.zone2());
        //上传对象
        UploadManager uploadManager = new UploadManager(cfg);
        try {
            // 调用put方法上传
            String token = getAuth().uploadToken(bucketname);

            Response res = uploadManager.put(filePath,null,token,null,null);

            if (res.isOK()) {
                Ret ret = res.jsonToObject(Ret.class);
                //如果不需要对图片进行样式处理，则使用以下方式即可
                return domain+ret.key;
            }
        } catch (QiniuException e) {
            Response r = e.response;
            // 请求失败时打印的异常的信息
            try {
                //把抛出的异常转换成另一个异常
                System.out.println(r.bodyString());
            } catch (QiniuException e1) {
                // ignore
            }
        }
        return null;
    }

    //base64方式上传
    public String put64image(byte[] base64, String key) throws Exception {
        String file64 = Base64.encodeToString(base64, 0);
        Integer l = base64.length;
        String url = "http://upload.qiniu.com/putb64/" + l + "/key/"+ UrlSafeBase64.encodeToString(key);
        //非华东空间需要根据注意事项 1 修改上传域名
        RequestBody rb = RequestBody.create(null, file64);
        Request request = new Request.Builder().
                url(url).
                addHeader("Content-Type", "application/octet-stream")
                .addHeader("Authorization", "UpToken " + getUpToken())
                .post(rb).build();
        //System.out.println(request.headers());
        OkHttpClient client = new OkHttpClient();
        okhttp3.Response response = client.newCall(request).execute();
        System.out.println(response);
        //如果不需要添加图片样式，使用以下方式
        return domain + key;
        //return DOMAIN + key + "?" + style;
    }


    class Ret {
        public long fsize;
        public String key;
        public String hash;
        public int width;
        public int height;
    }
}