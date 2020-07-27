package com.yzh.service.trsImgService.impl;

import com.yzh.service.trsImgService.ITrsImgService;
import com.yzh.utils.QiniuUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.util.Map;

/**
 * @program: yzh-doc
 * @description:
 * @author: yzh
 * @create: 2020-07-27 18:08
 **/
@Service
public class TrsImgService implements ITrsImgService {
    @Autowired
    public QiniuUtils qiniuUtils;
    public static final Logger logger = LoggerFactory.getLogger(TrsImgService.class);

    @Override
    public Map<String, Object> toImage(FileInputStream is) {
        return null;
    }
}
