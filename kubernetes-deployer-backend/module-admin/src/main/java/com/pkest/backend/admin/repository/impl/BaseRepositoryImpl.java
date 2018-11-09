package com.pkest.backend.admin.repository.impl;

import com.pkest.backend.admin.repository.BaseRepository;
import org.apache.log4j.Logger;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.List;

/**
 * Created by wuzhonggui on 2018/4/9.
 * QQ: 2731429978
 * Email: pk8est@qq.com
 */
@Resource
@SuppressWarnings("unused")
public class BaseRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID>
        implements BaseRepository<T, ID> {

    @SuppressWarnings("unused")
    private Logger logger = Logger.getLogger(BaseRepositoryImpl.class);

    private Class domainClass;

    private final EntityManager entityManager;

    public BaseRepositoryImpl(Class<T> domainClass, EntityManager em) {
        super(domainClass, em);
        this.entityManager = em;
        this.domainClass = domainClass;
    }

    public BaseRepositoryImpl(JpaEntityInformation<T, ID> entityInformation, EntityManager em) {
        super(entityInformation, em);
        this.entityManager = em;
        this.domainClass = entityInformation.getJavaType();

    }

   @SuppressWarnings("unchecked")
   public long store(Long id){
       List<T> list = entityManager.createNativeQuery(
               "SELECT * FROM projects WHERE 1=1 AND id>=:id",
               domainClass)
               .setParameter("id", id)
               .setMaxResults(8)
               .getResultList();
       return 0L;
   }

    @SuppressWarnings("unchecked")
    public long store2(Number id){

        /*Query query1 = entityManager.createQuery("FROM projects WHERE id>=:id");
        query1.setParameter("id", id);
        List<T> fields = query1.getResultList();
        System.err.println(fields);

        List<T> list = entityManager.createNativeQuery("SELECT * FROM projects WHERE 1=1 AND id>=:id", domainClass)
               .setParameter("id", id)
               .setMaxResults(8)
               .getResultList();
        System.err.println("list: "+ list);*/

        Predicate p1 = null;
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<T> query = cb.createQuery(domainClass);
    Root<T> root = query.from(domainClass);
        p1 = cb.equal(root.<String>get("id"), id);
        query.where(p1);
        List<T> list = entityManager.createQuery(query).getResultList();
        System.err.println(list);
        return 0L;
        }
        }
