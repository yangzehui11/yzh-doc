package com.yzh.controller;


import com.yzh.service.trsImgService.ITrsImgService;
import com.yzh.utils.QiniuUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
@CrossOrigin
@RestController
public class DocController {
    @Autowired
    private QiniuUtils qiniuCloudUtil;

    @Autowired
    private ApplicationContext applicationContext;
    @RequestMapping(method = RequestMethod.POST, value = "/upload")
    public Map upload(@RequestParam("file")MultipartFile file, HttpServletResponse response) throws IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        String[] arr = file.getOriginalFilename().split("\\.");
        String houzui = arr[arr.length - 1];
        FileInputStream fileImg = null;
        try {
            File f= File.createTempFile("tmp", null);
            file.transferTo(f);
            f.deleteOnExit();
            Map map=new HashMap<String,Object>();
            fileImg = new FileInputStream(f);
            ITrsImgService cart = (ITrsImgService) applicationContext.getBean(houzui);
            return cart.toImage(fileImg);

        } catch (Exception e) {
            e.printStackTrace();
            return  null;
        }finally {
            fileImg.close();
        }

    }
    /**
     * PDF转图片 当前是根据页码一页一页转，需要改成一次转完
     * imgType:转换后的图片类型 jpg,png
     */
    //          os,pdf文件流,上传到的map集合
    public boolean PDFToImg(FileInputStream is, Map<String,Object> map) throws IOException {
        PDDocument pdDocument = null;
        /* dpi越大转换后越清晰，相对转换速度越慢 */
        int dpi = 100;
        try {
            ArrayList list=new ArrayList<String>();
            pdDocument=PDDocument.load(is);
            PDFRenderer renderer = new PDFRenderer(pdDocument);
            int pages = pdDocument.getNumberOfPages();
            BufferedImage image=null;
            for (int i = 0; i <pages ; i++) {
                //BufferedImage转InputStream
                ByteArrayOutputStream os = null;
                InputStream input = null;
                try {
                    image = renderer.renderImageWithDPI(i,dpi);
                    os = new ByteArrayOutputStream();
                    ImageIO.write(image, "png", os);
                    input = new ByteArrayInputStream(os.toByteArray());
                    String s=qiniuCloudUtil.upload(input);
                    list.add(s);

                } catch (IOException e) {
                    return false;
                } finally {
                    //关闭流
                    if (os != null) {
                        os.close();
                    }
                    if (input != null) {
                        input.close();
                    }
                }
            }
            map.put("arr",list);
            map.put("windth",image.getWidth());
            map.put("height",image.getHeight());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (pdDocument != null) {
                pdDocument.close();
            }
        }

    }




}