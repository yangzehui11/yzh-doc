package com.yzh.controller;


import com.yzh.service.trsImgService.TrsImgServiceFactory;
import com.yzh.utils.QiniuUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.poi.hslf.model.Slide;
import org.apache.poi.hslf.usermodel.SlideShow;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
public class DocController extends BaseController {
    @Autowired
    private QiniuUtils qiniuCloudUtil;
    @Autowired
    private TrsImgServiceFactory factory;

    @RequestMapping(method = RequestMethod.POST, value = "/upload")
    public Map upload(@RequestParam("file")MultipartFile file) throws IOException {
//application/octet-stream
        String houzui=file.getOriginalFilename().split("\\.")[1];
        FileInputStream fileImg = null;
        try {
            File f= File.createTempFile("tmp", null);
            file.transferTo(f);
            f.deleteOnExit();
            Map map=new HashMap<String,Object>();
            fileImg = new FileInputStream(f);
            return factory.creator(houzui).toImage(fileImg);

            /*if(houzui.equals("ppt")){
//                doPPT2003toImage(fileImg,map);
            }else if (houzui.equals("pptx")){
//                doPPT2007toImage(fileImg,map);
            }else if(houzui.equals("pdf")){
                PDFToImg(fileImg,map);
            } else  if(houzui.equals("jpg")||houzui.equals("jpeg")){
                String s=qiniuCloudUtil.upload(fileImg);
                ArrayList list=new ArrayList<String>();
                list.add(s);
                map.put("arr",list);
                *//*map.put("windth",);
                map.put("height",pgsize.height);*//*
            }
            return map;*/
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