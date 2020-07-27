package com.yzh.service.trsImgService;

import java.io.FileInputStream;
import java.util.Map;
/*抽象策略类*/
public interface ITrsImgService {
    public Map<String,Object> toImage(FileInputStream is);
}
