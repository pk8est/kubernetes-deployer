package com.pkest.backend.admin.controller;

import com.aliyuncs.exceptions.ClientException;
import com.pkest.backend.admin.service.ClusterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by wuzhonggui on 2018/4/9.
 * QQ: 2731429978
 * Email: pk8est@qq.com
 */
@Controller
@RequestMapping("/cluster")
public class ClusterController {
    private static final Logger logger = LoggerFactory.getLogger(ClusterController.class);

    @Resource
    private ClusterService clusterService;

    @GetMapping("/index")
    @ResponseBody
    public Object index() throws ClientException {
        return clusterService.getClusterList();
    }
}
