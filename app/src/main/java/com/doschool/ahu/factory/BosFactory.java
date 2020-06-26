package com.doschool.ahu.factory;


import com.baidubce.BceClientException;
import com.baidubce.BceServiceException;
import com.baidubce.Protocol;
import com.baidubce.auth.DefaultBceCredentials;
import com.baidubce.services.bos.BosClient;
import com.baidubce.services.bos.BosClientConfiguration;
import com.baidubce.services.bos.callback.BosProgressCallback;
import com.baidubce.services.bos.model.ObjectMetadata;
import com.baidubce.services.bos.model.PutObjectRequest;
import com.doschool.ahu.configfile.CodeConfig;
import com.orhanobut.logger.Logger;

import java.io.File;

/**
 * Created by X on 2018/9/20
 */
public class BosFactory {

    public static BosClient getClient(){
        BosClientConfiguration config=new BosClientConfiguration();
        // 设置HTTP最大连接数默认5
        config.setMaxConnections(9);
        // 设置TCP连接超时为默认30000毫秒
//        config.setConnectionTimeoutInMillis(20000);
        // 设置Socket传输数据超时的时间为默认30000毫秒
//        config.setSocketTimeoutInMillis(20000);
        config.setEndpoint(CodeConfig.BOS_HTTP);
        config.setProtocol(Protocol.HTTPS);
        config.setCredentials(new DefaultBceCredentials(CodeConfig.BOS_AK,CodeConfig.BOS_SK));
        return new BosClient(config);
    }

    public static void getRequest(BosClient client, String key, File file, final OnBosCallback onBosCallback){
        try {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType("text/plain");
            PutObjectRequest request = new PutObjectRequest(CodeConfig.BOS_BUCKET, key, file,objectMetadata);
            request.setProgressCallback(new BosProgressCallback<PutObjectRequest>() {

                @Override
                public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                    super.onProgress(request,currentSize, totalSize);
                    if (onBosCallback!=null){
                        onBosCallback.onProgress(request,currentSize,totalSize);
                    }
                }
            });
            String eTag=client.putObject(request).getETag();
        } catch (BceServiceException e) {
            Logger.d("异常啊！！！"+"Error ErrorCode: " + e.getErrorCode()
            +"----Error RequestId: " + e.getRequestId()
            +"----Error StatusCode: " + e.getStatusCode()
            +"-----Error Message: " + e.getMessage()
            +"-----Error ErrorType: " + e.getErrorType());
        }catch (BceClientException e){
            Logger.d("烦！！！"+"Error Message: " + e.getMessage());
        }
    }
}
