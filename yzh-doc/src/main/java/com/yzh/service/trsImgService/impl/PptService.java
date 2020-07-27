package com.yzh.service.trsImgService.impl;

import org.apache.poi.hslf.model.Slide;
import org.apache.poi.hslf.usermodel.SlideShow;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: yzh-doc
 * @description:
 * @author: yzh
 * @create: 2020-07-27 17:46
 **/
/*策略实体类*/
@Component
public class PptService extends TrsImgService {
    @Override
    public Map<String, Object> toImage(FileInputStream is) {
        HashMap<String, Object> map = new HashMap<>();
        try {
            ArrayList list=new ArrayList<String>();
            //is = new FileInputStream(pptFile);
            SlideShow ppt = new SlideShow(is);
            //及时关闭掉 输入流
            //is.close();
            Dimension pgsize = ppt.getPageSize();
            Slide[] slide = ppt.getSlides();

            for (int i = 0; i < slide.length; i++) {

                //根据幻灯片大小生成
                // 图片开始
                BufferedImage img = new BufferedImage(pgsize.width,pgsize.height, BufferedImage.TYPE_INT_RGB);
                //BufferedImage img = new BufferedImage(400,300, BufferedImage.TYPE_INT_RGB);
                //图片结束
                Graphics2D graphics = img.createGraphics();
                graphics.setPaint(Color.white);
                graphics.fill(new Rectangle2D.Float(0, 0, pgsize.width,pgsize.height));
                slide[i].draw(graphics);
                //BufferedImage转InputStream
                ByteArrayOutputStream os = null;
                InputStream input = null;
                try {
                    os = new ByteArrayOutputStream();
                    ImageIO.write(img, "png", os);
                    input = new ByteArrayInputStream(os.toByteArray());
                    String s=qiniuUtils.upload(input);//上传到七牛云
                    list.add(s);
                } catch (IOException e) {
                    logger.error("上传七牛云失败",e);
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
            map.put("windth",pgsize.width);
            map.put("height",pgsize.height);
        } catch (Exception e) {
            logger.error("转换成图片 发生异常！",e);
        }
        return map;

    }
}
