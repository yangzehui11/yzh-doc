package com.yzh.service.trsImgService.impl;

import com.yzh.service.trsImgService.ITrsImgService;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.util.Map;

/**
 * @program: yzh-doc
 * @description:
 * @author: yzh
 * @create: 2020-07-27 17:48
 **/
/*策略实体类*/
@Component(value = "pdf")
public class PdfService implements ITrsImgService {
    @Override
    public Map<String, Object> toImage(FileInputStream is) {
        return null;
    }
}
