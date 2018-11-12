package com.pkest.backend.admin.controller;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.aliyuncs.exceptions.ClientException;
import com.pkest.backend.admin.util.AliyunUtils;
import com.pkest.libs.aliyun.model.cs.HYDescribeClustersRequest;
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
    private AliyunUtils aliyunUtils;

    @GetMapping("/index")
    @ResponseBody
    public Object index(){
        try {
            return aliyunUtils.getClient().describeClusters(new HYDescribeClustersRequest()).getList();
        } catch (ClientException e) {
            logger.error("", e);
        }
        return null;
    }
}
