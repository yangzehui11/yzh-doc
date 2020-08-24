package com.yzh.domain;

import com.yzh.service.trsImgService.ITrsImgService;
import com.yzh.service.trsImgService.impl.PdfService;
import com.yzh.service.trsImgService.impl.PptService;
import com.yzh.service.trsImgService.impl.PptxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @program: yzh-doc
 * @description:
 * @author: yzh
 * @create: 2020-07-27 19:04
 **/

public class TrsImgTypeEnum {
    /*@Autowired
    private PptService pptService;
    @Autowired
    private PptxService pptxService;
    @Autowired
    private PdfService pdfService;

    PPT("ppt",pptService),

    PPTX("pptx", pptService),

    PDF("pdf", pptService);
*/
    private String code;

    private ITrsImgService value;

    private TrsImgTypeEnum(String code,ITrsImgService value) {
        this.code = code;
    }
}
