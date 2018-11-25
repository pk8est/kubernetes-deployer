package com.pkest.libs.dbgeneral;

import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.List;

/**
 * Created by wuzhonggui on 2018/4/19.
 * QQ: 2731429978
 * Email: pk8est@qq.com
 */
public class TableCluster{

    private List<String> list = Lists.newArrayList();

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public TableCluster(String ...set){
        this.list = Arrays.asList(set);
    }

    public TableCluster(List<String> filter, List<String> all){
        this(filter, all, null);
    }
    public TableCluster(List<String> filter, List<String> all, String prefix){
        this.list = getFilterTable(filter, all, prefix);
    }

    public int size(){
        return list.size();
    }

    List<String> getFilterTable(List<String> filter, List<String> all, String prefix){
        List<String> data = Lists.newArrayList();
        for(String table: filter){
            table = prefix == null ? table : prefix + table;
            if(all.contains(table)){
                data.add(table);
            }
        }
        return data;
    }

}
