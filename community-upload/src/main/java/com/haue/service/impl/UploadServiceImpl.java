package com.haue.service.impl;

import com.google.gson.Gson;
import com.haue.enums.AppHttpCodeEnum;
import com.haue.exception.SystemException;
import com.haue.service.UploadService;
import com.haue.utils.PathUtils;
import com.haue.utils.ResponseResult;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
@Data
@ConfigurationProperties(prefix = "oss")
public class UploadServiceImpl implements UploadService {

    private String accessKey;
    private String secretKey;
    private String bucket;

    /**
     * 上传图片
     * @param img
     * @return
     */
    @Override
    public ResponseResult upload(MultipartFile img) {
        String filename = img.getOriginalFilename();
        if (!filename.endsWith(".png") && !filename.endsWith(".jpg")){
            new SystemException(AppHttpCodeEnum.FILE_TYPE_ERROR);
        }
        String filePath = PathUtils.generateFilePath(filename);
        String url = uploadOss(img,filePath);
        return ResponseResult.okResult(url);
    }

    /**
     * 上传到七牛云服务器
     * @param imgFile
     * @param filePath
     * @return 返回图片链接
     */
    private String uploadOss(MultipartFile imgFile, String filePath){
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.autoRegion());
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;// 指定分片上传版本

        UploadManager uploadManager = new UploadManager(cfg);

        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = filePath;

        try {
            InputStream inputStream = imgFile.getInputStream();
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);
            try {
                Response response = uploadManager.put(inputStream,key,upToken,null, null);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println(putRet.key);
                System.out.println(putRet.hash);
                return "http://rs6c4ew2k.hn-bkt.clouddn.com/"+key;
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    //ignore
                }
            }
        } catch (Exception ex) {
            //ignore
        }
        return "异常";
    }
}
