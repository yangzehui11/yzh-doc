package com.yzh.service.trsImgService;

import com.yzh.service.trsImgService.impl.PdfService;
import com.yzh.service.trsImgService.impl.PptService;
import com.yzh.service.trsImgService.impl.PptxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: yzh-doc
 * @description:
 * @author: yzh
 * @create: 2020-07-27 19:16
 **/
/*策略工厂类*/
@Component
public class TrsImgServiceFactory {
    @Autowired
    private PptService pptService;
    @Autowired
    private PptxService pptxService;
    @Autowired
    private PdfService pdfService;

    private Map<String, ITrsImgService> map = new HashMap<>();
    private TrsImgServiceFactory(){
        map.put("ppt", pptService);
        map.put("pptx", pptxService);
        map.put("pdf", pdfService);
    }

    public ITrsImgService creator(String type) {
        return map.get(type);
    }
}
