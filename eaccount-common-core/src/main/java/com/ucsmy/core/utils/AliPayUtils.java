/*
package com.ucsmy.core.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayDataDataserviceBillDownloadurlQueryRequest;
import com.alipay.api.response.AlipayDataDataserviceBillDownloadurlQueryResponse;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.Map;

*/
/**
 * 文件操作相关工具类
 *
 * @author ucs_guichang
 * @since 2017/9/15
 *//*


public class AliPayUtils {
    public static String SERVER_URL = "https://openapi.alipaydev.com/gateway.do";
    public static String APP_ID = "2016080900199540";
    public static String APP_PRIVATE_KEY = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQC/cW2gT3GYq+gcU2M1IOC6L/+BVKlMS4L+Ogs0elBELti+tvACHfeOt+gcujzIMTniDG8BXPQZW/XqpI5eAoyK41snr+GKtzYk5eTYnWpGap7SBeaTBV9kP9yp8WnN3kL3nnyYQVG+LUR0GocziHK/a8scm3g59CYbKG++/kxIoaazaGc3LRavtxiG9PbjrF2D38A+srfClIprSd/gGRfyzVrNTJJOMdnBmo4LN/JkDFFt6nJpQ49j4VZgyhnlVUFHOFColt7KJDT1GNp6082zIZtA8qpbFe43JAmBOQFbPOOcL3+YDnhtf/sJuDLwAvRpp0QaRHPDiaic+2i+theVAgMBAAECggEBALt9oxe1tlMHSTM0l5yYrn6qM1m+SG/oJEpHHmIuyB1+o9dj4nqwTpr/kdtQqeaGD2JLOwvitU2Aur8qmiOH64y6TbP4lLL965EHdbCPul1sXk7iaIbkZCASS1JElJKCcqOAHBYhIqVWQAxVTsQxhe/i6fECciSP8CH44Df1ZVBbDvg0gBqOavn4OOylurm8nIwhdOEZSIEaEvx8uGoXtyrkdBVW/fZ+S2FpjWgWyl1tJiP7V57D6KAMAl0gYVxr2GCBPAGA+afqLifOXCQ14YZsEFHXaUf5pHVnX5E+Vr0a5IVw5lLOPhfWCu4V9XQTcBR0BTAu96HywHgIqMXPjQECgYEA3+iYZojwzyMu+pxvbpVnVkIoCAw5gXc61gfpMR9QtNNZgyxVJc9AcPI73iFeQm8GjF9Xe/YWYZHU3PrRM6jSjMu9EhFrBhcfU9Mz0zTlHqr5dUaZdwSsj4MNjVb2a6IPfxIiIRYA2ufSuxaiSU3jvafgQbeopOfYxYWlW4yXlq0CgYEA2uGlHeFj611Lcpz1Pus6ohKY3DYeTI6J9y2MskyzMfEbdeL48TZd265TPJ9T6wjOvuTlXgwxxrWJQgBFCdvW9KHsqQ62MCgC6Kei1b3FkCCj3Jjm1DCGW1teGWgTIyiwsFHvlXB0GeBX8q7k4y4BE29XjBWhGwUArhSWaESf6YkCgYAvdJqoUKnJHQnd773mUCAAtjDJ0910RRLhPff8g0iVqp20IXdCdjSQO6bYo3XY2rMmmSZvjW6c602UMEyaiFNnp97Ar5RhTBFFsOOxBerxnwcUBgyMnx77o6kBpcbAdS6Xnf5ByOoRe0S8hkWLB8kW7zemdgh2OvVEl9i+GIQgWQKBgBvAIgQQ0G9Fy8wDNGC+yBdGTlUVE3BXXgyVnNH6QXnifL3Mgg1q/ClfB7SW6rfQzQ2MzbvHxgPQfk9840PluHIZmXyxnc2rNPs0TcPVpJeqW7wbMqDSaiZpBhwogsyhWw1vxfVnZYDmXaAqJmDbM024fxwGnf9j+fsYJx2fHbspAoGBAKDumrxaUcZ5hqkut94ggH1ojvd7IHfxtDXohJyvddmb/u3rq0rfpGytAe63J4HsS8smgA+zgn0kM4musBjUFNQt0zKVPB8rOhfslE70gAWBgY7V453TiKoF6sTBjt/jk4M1BcFk58s+sgFNXNoGuIKfU9mzQLpb335Gwz4fBy7J";
    public static String FORMAT = "json";
    public static String CHARSET = "UTF-8";  //GBK
    public static String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAoAVfDl5x3g3kHCXjDnS5yNqoWob5dnB3jlom6BENG83MdNl39b6vnLZzdIQB1mOGdwkUHhEqWz3WqqMAm/IpeV5GcDkzqeLJSWeV0kgwNNmDSVZd3jyss+fwzKh3d9An5Cw9sTCvqhGQPw/PoLV3Nso22+RDsaN20XCKnDe+yKO2riq1/clS7Ie5lDmcVrZbcHiAo1XVhst2AE4amJPpOhvDMyGN2L0YBOh9+WkPjV3V1yXYPKJ8U9qQAHBjUMRwPTaHusSX1HUCbrYRRdnLH0LcCORZgTGnm7gohuHI0GBTGKPe9T/ZylYCpxBPn+Px52d8KgfVIP6qNgqwl3MbBQIDAQAB";
    public static String SIGN_TYPE = "RSA2";

    public static String TRADEFILEDIR = "tradeFile";


    public static AlipayClient newInstance() {
        return new DefaultAlipayClient(SERVER_URL, APP_ID, APP_PRIVATE_KEY, FORMAT, CHARSET, ALIPAY_PUBLIC_KEY, SIGN_TYPE);
    }

    */
/**
     * 请求下载地址
     *
     * @param yyyyMMdd 请求时间（如2017-09-13）
     * @return 下载文件的url
     *//*

    public static String queryDownloadUrl(String yyyyMMdd) throws AlipayApiException {
        AlipayDataDataserviceBillDownloadurlQueryRequest request = new AlipayDataDataserviceBillDownloadurlQueryRequest();//创建API对应的request类
        //设置业务参数
        JSONObject json = new JSONObject();
        json.put("bill_type", "trade");
        json.put("bill_date", yyyyMMdd);
        request.setBizContent(json.toString());
        AlipayDataDataserviceBillDownloadurlQueryResponse response = newInstance().execute(request);
        String body = response.getBody();
        //System.out.println(body);
        Map<String, Object> map1 = (Map<String, Object>) JSON.parse(body);
        Map<String, String> map2 = (Map<String, String>) map1.get("alipay_data_dataservice_bill_downloadurl_query_response");
        // TODO 验证签名
        String sign = map1.get("sign").toString();
        System.out.println(sign);

        return map2.get("bill_download_url");
    }

    */
/**
     * 从网络Url中下载文件
     *
     * @param urlStr   下载文件的url
     * @param fileName 文件名
     * @param yyyyMMdd 日期
     * @return 下载文件的ur
     *//*

    public static String downLoadFromUrl(String urlStr, String fileName, String yyyyMMdd) throws IOException {
        String savePath = getSavePath(yyyyMMdd);
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.connect();
        urlStr = conn.getHeaderField("Location");//获取重定向后的url

        url = new URL(urlStr);
        conn = (HttpURLConnection) url.openConnection();
        //设置超时间为3秒
        conn.setConnectTimeout(3 * 1000);
        //防止屏蔽程序抓取而返回403错误
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        conn.connect();
        //得到输入流
        InputStream inputStream = conn.getInputStream();
        //获取自己数组
        byte[] getData = readInputStream(inputStream);

        //文件保存位置
        File saveDir = new File(savePath);
        File file = new File(saveDir + File.separator + fileName);
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(getData);
        if (fos != null) {
            fos.close();
        }
        if (inputStream != null) {
            inputStream.close();
        }
        return file.getPath();
    }

    */
/**
     * 先创建日期文件夹，如果发现存在则创建子文件夹以+1方式递增(如 2017-09-13/1、2017-09-13/2),
     * 并将原压缩文件和解压文件移动至子文件夹
     *
     * @param yyyyMMdd
     * @return 创建的日期目录
     *//*

    private static String getSavePath(String yyyyMMdd) {
        String baseDateDir = classPath(TRADEFILEDIR + File.separator + yyyyMMdd);
        File dateDir = new File(baseDateDir);
        if (dateDir.exists()) {
            int i = 0;
            File subDir ;
            while ( (subDir = new File(baseDateDir + File.separator + (++i))).exists() ) {
                System.out.println(i);
            }
            subDir.mkdir();// 创建子文件夹 1
            File[] files = dateDir.listFiles();
            for (File fTemp : files) {
                if (fTemp.isFile()) {// 如果是文件则移动至子文件夹
                    fTemp.renameTo(new File(subDir.getPath() + File.separator + fTemp.getName()));
                }
            }
        } else dateDir.mkdirs();
        return baseDateDir;
    }


    */
/**
     * 从输入流中获取字节数组
     *
     * @param inputStream
     * @return
     * @throws IOException
     *//*

    private static byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }

    */
/**
     * 解压文件
     *
     * @param zipFilePath 如 C:/Users/ucs_guichang/Desktop/zip/1505463694769.zip
     * @param yyyyMMdd    生成账单时间
     * @return 解压的目录
     * @throws Exception
     *//*

    public static String unZip(String zipFilePath, String yyyyMMdd) throws IOException {
        String destDir = classPath(TRADEFILEDIR + File.separator + yyyyMMdd);
        ZipFile zipFile = new ZipFile(zipFilePath, "GBK");
        Enumeration<?> emu = zipFile.getEntries();
        BufferedInputStream bis;
        FileOutputStream fos;
        BufferedOutputStream bos;
        File file, parentFile;
        ZipEntry entry;
        byte[] cache = new byte[1024];
        while (emu.hasMoreElements()) {
            entry = (ZipEntry) emu.nextElement();
            if (entry.isDirectory()) {
                new File(destDir + entry.getName()).mkdirs();
                continue;
            }
            bis = new BufferedInputStream(zipFile.getInputStream(entry));
            file = new File(destDir + File.separator + entry.getName());
            parentFile = file.getParentFile();
            if (parentFile != null && (!parentFile.exists())) {
                parentFile.mkdirs();
            }
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos, 1024);
            int nRead;
            while ((nRead = bis.read(cache, 0, 1024)) != -1) {
                fos.write(cache, 0, nRead);
            }
            bos.flush();
            bos.close();
            fos.close();
            bis.close();
        }
        zipFile.close();
        return destDir;
    }

    public static String classPath(String dirPath) {
        return AliPayUtils.class.getClassLoader().getResource("").getPath() + dirPath;
    }

    public static void main(String[] args) {
        System.out.println(classPath(TRADEFILEDIR));
    }


}*/
