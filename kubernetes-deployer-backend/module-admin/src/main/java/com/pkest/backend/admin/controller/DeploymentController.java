package com.pkest.backend.admin.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableMap;
import com.pkest.backend.admin.model.DeploymentRequest;
import com.pkest.backend.admin.service.ClusterService;
import com.pkest.common.util.GsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by wuzhonggui on 2018/4/9.
 * QQ: 2731429978
 * Email: pk8est@qq.com
 */
@Controller
@RequestMapping("/deployment")
public class DeploymentController {
    private static final Logger logger = LoggerFactory.getLogger(DeploymentController.class);

    @Resource
    @JsonProperty(value = "Name")
    private ClusterService clusterService;

    @ResponseBody
    @RequestMapping(value = "/create", method = RequestMethod.POST,  produces = "application/json;charset=UTF-8")
    public Object create(@RequestBody DeploymentRequest deploymentRequest) throws Exception {
        System.err.println(GsonUtils.getGson().toJson(deploymentRequest));
        clusterService.createDeployment(deploymentRequest);
        return ImmutableMap.of("code", "0", "message", "成功");
        //return clusterService.getClusterList();
    }
}
