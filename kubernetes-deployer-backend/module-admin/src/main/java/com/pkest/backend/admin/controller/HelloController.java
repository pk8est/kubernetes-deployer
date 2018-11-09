package com.pkest.backend.admin.controller;

import com.pkest.backend.admin.entity.Projects;
import com.pkest.backend.admin.repository.BaseRepository;
import com.pkest.backend.admin.repository.ProjectsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
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
@RequestMapping("/projects")
public class HelloController extends GenController{
    private static final Logger logger = LoggerFactory.getLogger(HelloController.class);

    //@Autowired
    //private VideoRepository videoRepository;

    @Resource
    private ProjectsRepository repository;

    @Override
    public BaseRepository getRepository() {
        return repository;
    }

    @RequestMapping(value = "/sayHello",method = RequestMethod.GET)
    @ResponseBody
    public Page sayHello(Projects entity, Pageable pageable){
        logger.debug("Hello，Spring Boot！");
        logger.info("Hello，Spring Boot！");
        /*Map<String, Object> info = videoRepository.get(1759L);
        logger.info("{}", info);*/

        //jpaVideoRepository.store();
        logger.info("{}", repository.store(5L));
        logger.info("{}", repository.count());
        Page<Projects> page = repository.findAll(pageable);
        logger.info("{}; {}; {}; {}, {}", page.getTotalElements(), page.getTotalPages(), page.getContent(), page.getNumber(), page.getSize());
        return page ;
    }

    @ResponseBody
    @RequestMapping("/{id}")
    Object show(@PathVariable("id") Projects projects, Model model) {
        System.err.println(projects);
        model.addAttribute("projects", projects);
        return projects;
    }

}
