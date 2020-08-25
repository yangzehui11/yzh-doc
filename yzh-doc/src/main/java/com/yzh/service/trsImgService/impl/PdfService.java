package com.yzh.service.trsImgService.impl;

import com.yzh.service.trsImgService.ITrsImgService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: yzh-doc
 * @description:
 * @author: yzh
 * @create: 2020-07-27 17:48
 **/
/*策略实体类*/
@Component(value = "pdf")
public class PdfService extends TrsImgService  {
    @Override
    public Map<String, Object> toImage(FileInputStream is) {
        HashMap<String, Object> map = new HashMap<>();
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
                    String s=qiniuUtils.upload(input);
                    list.add(s);

                } catch (IOException e) {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
}
