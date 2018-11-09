package com.pkest.libs.aliyun.model.cs;

import com.alibaba.fastjson.annotation.JSONField;
import com.aliyuncs.cr.model.v20160607.CreateRepoRequest;
import lombok.Getter;
import lombok.Setter;

/**
 * @author 360733598@qq.com
 * @date 2018/11/10 1:17
 */
@Getter
@Setter
public class HYCreateRepoRequest extends CreateRepoRequest{

    @JSONField(label = "form")
    private Repo repo;

    @Getter
    @Setter
    public static class Repo{
        @JSONField(label = "form")
        public String repoName;
        @JSONField(label = "form")
        public String repoNamespace;
        @JSONField(label = "form")
        public String summary;
        @JSONField(label = "form")
        public String repoType;
    }

    public HYCreateRepoRequest(){

    }

    public HYCreateRepoRequest(Repo repo){
        setRepo(repo);
    }

}
