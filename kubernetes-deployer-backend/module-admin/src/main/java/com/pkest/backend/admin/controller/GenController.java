package com.pkest.backend.admin.controller;

import com.pkest.backend.admin.repository.BaseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by wuzhonggui on 2018/4/9.
 * QQ: 2731429978
 * Email: pk8est@qq.com
 */
@Controller
public abstract class GenController {
    private static final Logger logger = LoggerFactory.getLogger(GenController.class);

    abstract public BaseRepository getRepository();

    @GetMapping("/index")
    @ResponseBody
    public Object index(){
        logger.info("store: {}", getRepository().store(1L));
        logger.info("store2: {}", getRepository().store2(1L));
        return "index";
    }
}
