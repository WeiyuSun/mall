package com.product;

import com.aliyun.oss.*;

import java.io.*;

import com.product.entity.BrandEntity;
import com.product.service.BrandService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileInputStream;
import java.io.InputStream;

@RunWith(SpringRunner.class)
@SpringBootTest
class MallProductApplicationTests {

    @Autowired
    private BrandService brandService;

//    @Autowired
//    private OSS ossClient;

    @Test
    void contextLoads() {
        BrandEntity brand = new BrandEntity();
        brand.setName("huawei");
        boolean result = brandService.save(brand);
        System.out.println("the result is " + result);
    }
//
//    @Test
//    public void testOssFileUpload() {
////        // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
////        String endpoint = "https://oss-us-west-1.aliyuncs.com";
////        // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
////        String accessKeyId = "LTAI5tPBG3ur4kNADjhaADcK";
////        String accessKeySecret = "6jZ2WMO3uod2DSRgXfA2L16gjKgRtm";
//        // 填写Bucket名称，例如examplebucket。
//        String bucketName = "weiyu-mall";
//        // 填写Object完整路径，完整路径中不能包含Bucket名称，例如exampledir/exampleobject.txt。
//        String objectName = "WechatIMG18.jpeg";
//        // 填写本地文件的完整路径，例如D:\\localpath\\examplefile.txt。
//        // 如果未指定本地路径，则默认从示例程序所属项目对应本地路径中上传文件流。
//        String filePath = "/Users/weiyu/Desktop/WechatIMG18.jpeg";
//
//        // 创建OSSClient实例。
//        //OSS ossClient = new OSSClientBuilder().build(, accessKeyId, accessKeySecret);
//
//        try {
//            InputStream inputStream = new FileInputStream(filePath);
//            // 创建PutObject请求。
//            ossClient.putObject(bucketName, objectName, inputStream);
//        } catch (OSSException oe) {
//            System.out.println("Caught an OSSException, which means your request made it to OSS, "
//                    + "but was rejected with an error response for some reason.");
//            System.out.println("Error Message:" + oe.getErrorMessage());
//            System.out.println("Error Code:" + oe.getErrorCode());
//
//            System.out.println("Host ID:" + oe.getHostId());
//        } catch (ClientException | FileNotFoundException ce) {
//            System.out.println("Caught an ClientException, which means the client encountered "
//                    + "a serious internal problem while trying to communicate with OSS, "
//                    + "such as not being able to access the network.");
//            System.out.println("Error Message:" + ce.getMessage());
//        } finally {
//            if (ossClient != null) {
//                ossClient.shutdown();
//            }
//        }
//    }
}
