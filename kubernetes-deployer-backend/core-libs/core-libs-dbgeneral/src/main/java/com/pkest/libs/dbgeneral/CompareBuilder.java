package com.pkest.libs.dbgeneral;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.*;

/**
 * Created by wuzhonggui on 2017/7/25.
 * QQ: 2731429978
 * Email: pk8est@qq.com
 */
public class CompareBuilder extends LinkedHashMap<CompareBuilder.CompareField, Object> implements Cloneable{

    private Boolean groupStart = false;
    private Pageable pageable;
    private Sort sortable;
    private String groupBy;
    private String having;

    public enum OperatorEnum{
        EQ("="), NEQ("<>"), GT(">"), LT("<"), EGT(">="), ELT("<="), IN("IN"), NOTIN("NOT IN"), BETWEEN("BETWEEN"), NOTBETWEEN("NOT BETWEEN"),
        BITAND("&"), BITOR("|"), BITNOTOR("^"), BITNOT("^"), AND("&&"),OR("||"),NOT("NOT"),REGEXP("REGEXP"),
        LIKE("LIKE"), NOTLIKE("NOT LIKE"), NULL("IS NULL"), NOTNULL("IS NOT NULL"), LMOVE("<<"), RMOVE(">>"), EMPTY("");

        private final String value;
        OperatorEnum(String value){
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public String toString(){
            return getValue();
        }
    }
    public enum CompareFieldValueType{
        SIMPLE,LIST,MAP,
    }

    public String getSortDescribe(){
        Sort.Order sort = null;
        List<String> sortList = Lists.newArrayList();
        Iterator<Sort.Order> iterator = getSortable() != null ? getSortable().iterator() : null;
        if(iterator != null){
            while (iterator.hasNext()){
                sort = iterator.next();
                sortList.add(sort.getProperty() + " " + sort.getDirection());
            }
        }
        if(sortList.size() > 0){
            return "ORDER BY " + Joiner.on(",").skipNulls().join(sortList);
        }else{
            return "";
        }
    }

    public String getPageDescribe(){
        if(getPageable() != null && getPageable().getPageSize() > 0){
            return "LIMIT " + getPageable().getOffset() + "," + getPageable().getPageSize();
        }else{
            return "";
        }
    }

    public Pageable getPageable() {
        return pageable;
    }

    public void setPageable(Pageable pageable) {
        this.pageable = pageable;
    }

    public Sort getSortable() {
        if(sortable != null){
            return sortable;
        }else{
            return getPageable() != null ? getPageable().getSort() : null;
        }
    }

    public void setSortable(Sort sortable) {
        this.sortable = sortable;
    }

    public String getGroupBy() {
        return StringUtils.isNotBlank(groupBy) ? "GROUP BY " + groupBy : "";
    }

    public String getHaving() {
        return StringUtils.isNotBlank(having) ? "HAVING " + having : "";
    }

    public CompareBuilder groupBy(String groupBy){
        this.groupBy = groupBy;
        return this;
    }

    public CompareBuilder having(String having){
        this.having = having;
        return this;
    }

    public CompareBuilder(){
        addCondition("1=1");
    }

    public CompareBuilder(String field, Object value){
        this(field, value, null, null);
    }

    public CompareBuilder(String field, Map<String, Object> param){
        addCondition(field, param);
    }

    public CompareBuilder(String field, Object value, Pageable pageable){
        this(field, value, pageable, null);
    }

    public CompareBuilder(String field, Object value, Sort sort){
        this(field, value, null, sort);
    }

    public CompareBuilder(String field, Object value, Pageable pageable, Sort sort){
        filter(field, value);
        setPageable(pageable);
        setSortable(sort);
    }

    public CompareBuilder(Map<String, String> conditions){
        for(Map.Entry<String, String> entry: conditions.entrySet()){
            filter(entry.getKey(), entry.getValue());
        }
    }

    public CompareBuilder(String field, OperatorEnum operator, Object value){
        filter(field, operator, value);
    }

    public CompareBuilder(String field, List value){
        in(field, value);
    }

    public CompareBuilder startGroup(){
        return startGroup(true);
    }

    public CompareBuilder startGroup(Boolean and){
        addStatementCondition("(", and);
        groupStart = false;
        return this;
    }

    public CompareBuilder endGroup(){
        return addStatementCondition(")", null);
    }

    public CompareBuilder filter(String field, Object value){
        return compare(field, OperatorEnum.EQ, value);
    }

    public CompareBuilder filter(String field, Object value, Boolean and){
        return compare(field, OperatorEnum.EQ, value, and);
    }

    public CompareBuilder filter(String field, OperatorEnum operator, Object value) {
        return filter(field, operator, value, true);
    }

    public CompareBuilder filter(String field, OperatorEnum operator, Object value, Boolean and){
        return where(field, value, operator, and, false);
    }

    public CompareBuilder compare(String field, Object value){
        return compare(field, OperatorEnum.EQ, value);
    }

    public CompareBuilder compare(String field, Object value, Boolean and){
        return compare(field, OperatorEnum.EQ, value, and);
    }

    public CompareBuilder compare(String field, OperatorEnum operator, Object value){
        return compare(field, operator, value, true);
    }

    public CompareBuilder compare(String field, OperatorEnum operator, Object value, Boolean and){
        return where(field, value, operator, and, true);
    }

    public CompareBuilder in(String field, List value){
        return in(field, value, true);
    }

    public CompareBuilder in(String field, List value, Boolean and){
        return in(field, value, and, true);
    }

    public CompareBuilder in(String field, List value, Boolean and, Boolean allowEmpey){
        return addInCondition(field, value, true, and, allowEmpey);
    }

    public CompareBuilder notIn(String field, List value){
        return in(field, value, true);
    }

    public CompareBuilder notIn(String field, List value, Boolean and){
        return notIn(field, value, and, true);
    }

    public CompareBuilder notIn(String field, List value, Boolean and, Boolean allowEmpey){
        return addInCondition(field, value, false, and, allowEmpey);
    }


    public CompareBuilder like(String field, Object value){
        return like(field, value, true);
    }

    public CompareBuilder like(String field, Object value, Boolean and){
        return like(field, value, and, true);
    }

    public CompareBuilder like(String field, Object value, Boolean and, Boolean allowEmpey){
        return addSearchCondition(field, value, and, true, true, true, allowEmpey);
    }

    public CompareBuilder rLike(String field, Object value){
        return rLike(field, value, true);
    }

    public CompareBuilder rLike(String field, Object value, Boolean and){
        return rLike(field, value, and, true);
    }

    public CompareBuilder rLike(String field, Object value, Boolean and, Boolean allowEmpey){
        return addSearchCondition(field, value, and, true, false, true, allowEmpey);
    }

    public CompareBuilder lLike(String field, Object value){
        return lLike(field, value, true);
    }

    public CompareBuilder lLike(String field, Object value, Boolean and){
        return lLike(field, value, and, true);
    }

    public CompareBuilder lLike(String field, Object value, Boolean and, Boolean allowEmpey){
        return addSearchCondition(field, value, and, true, true, false, allowEmpey);
    }

    public CompareBuilder notLike(String field, Object value){
        return notLike(field, value, true);
    }

    public CompareBuilder notLike(String field, Object value, Boolean and){
        return notLike(field, value, and, true);
    }

    public CompareBuilder notLike(String field, Object value, Boolean and, Boolean allowEmpey){
        return addSearchCondition(field, value, and, false, true, true, allowEmpey);
    }

    public CompareBuilder notRLike(String field, Object value){
        return notRLike(field, value, true);
    }

    public CompareBuilder notRLike(String field, Object value, Boolean and){
        return notRLike(field, value, and, true);
    }

    public CompareBuilder notRLike(String field, Object value, Boolean and, Boolean allowEmpey){
        return addSearchCondition(field, value, and, false, false, true, allowEmpey);
    }

    public CompareBuilder notLLike(String field, Object value){
        return notLLike(field, value, true);
    }

    public CompareBuilder notLLike(String field, Object value, Boolean and){
        return notLLike(field, value, and, true);
    }

    public CompareBuilder notLLike(String field, Object value, Boolean and, Boolean allowEmpey){
        return addSearchCondition(field, value, and, false, true, false, allowEmpey);
    }

    public CompareBuilder between(String field, Object start, Object end){
        return between(field, start, end, true);
    }

    public CompareBuilder between(String field, Object start, Object end, Boolean and){
        return addFieldCondition(field, OperatorEnum.BETWEEN, Lists.newArrayList(start, end), and);
    }

    public CompareBuilder notBetween(String field, Object start, Object end){
        return notBetween(field, start, end, true);
    }

    public CompareBuilder notBetween(String field, Object start, Object end, Boolean and){
        return addFieldCondition(field, OperatorEnum.NOTBETWEEN, Lists.newArrayList(start, end), and);
    }

    public CompareBuilder where(String field, Object value, OperatorEnum operator, Boolean and, Boolean allowEmpey){
        if(value instanceof List){
            return addInCondition(field, (List)value, operator.equals(OperatorEnum.NOTIN) ? false : true, and, allowEmpey);
        }else if (value instanceof Arrays){
            return addInCondition(field, Arrays.asList((List)value), operator.equals(OperatorEnum.NOTIN) ? false : true, and, allowEmpey);
        }
        String val = value == null ? "" : value.toString();
        if(allowEmpey == true && StringUtils.isEmpty(val)){
            return this;
        }
        return addFieldCondition(field, operator == null ? OperatorEnum.EQ : operator, val, and);
    }

    private CompareBuilder addInCondition(String field, List value, Boolean in, Boolean and, Boolean allowEmpey) {
        value = value == null ? Lists.newArrayList() : value;
        int len = value.size();
        if(allowEmpey == true && len == 0){
            return this;
        }else if(len == 0){
            return addFieldCondition("0", OperatorEnum.EQ, "1", and);
        }else if(len == 1){
            if(value.get(0) == null){
                return addFieldCondition(field, in ? OperatorEnum.NULL : OperatorEnum.NOTNULL, "", and);
            }else{
                return addFieldCondition(field, in ? OperatorEnum.EQ : OperatorEnum.NEQ, value.get(0).toString(), and);
            }
        }else{
            addFieldCondition(field, in ? OperatorEnum.IN : OperatorEnum.NOTIN, value, and);
        }
        return this;
    }

    private CompareBuilder addSearchCondition(String field, Object value, Boolean and, Boolean like, Boolean left, Boolean right, Boolean allowEmpey) {
        String val = value == null ? "" : value.toString();
        if(allowEmpey == true && StringUtils.isEmpty(val)){
            return this;
        }
        val = escape(val);
        if(left){
            val = "%" + val;
        }
        if(right){
            val = val + "%";
        }
        return addFieldCondition(field, like ? OperatorEnum.LIKE : OperatorEnum.NOTLIKE, val, and);
    }

    public CompareBuilder addCondition(String field){
        return addStatementCondition(field, true);
    }

    public CompareBuilder addCondition(String field, Boolean and){
        return addStatementCondition(field, and);
    }

    public CompareBuilder addCondition(String field, Map<String, Object> param ){
        return addCondition(field, param, true);
    }

    public CompareBuilder addCondition(String field, Map<String, Object> param, Boolean and){
        if(param != null){
            for(Map.Entry<String, Object> entry: param.entrySet()){
                field = field.replace("#{"+entry.getKey()+"}", "#{__item."+entry.getKey()+"}");
            }
        }
        return addFieldCondition(field, OperatorEnum.EMPTY, param, and);
    }

    public CompareBuilder addStatementCondition(String field, Boolean and) {
        put(getCompareField(field, OperatorEnum.EMPTY, and, CompareFieldValueType.SIMPLE, true), null);
        return this;
    }

    public CompareBuilder addFieldCondition(String field, OperatorEnum operator, List value, Boolean and) {
        put(getCompareField(field, operator, and, CompareFieldValueType.LIST, false), value);
        return this;
    }

    public CompareBuilder addFieldCondition(String field, OperatorEnum operator, Map value, Boolean and) {
        put(getCompareField(field, operator, and, CompareFieldValueType.MAP, false), value);
        return this;
    }

    private CompareBuilder addFieldCondition(String field, OperatorEnum operator, String value, Boolean and) {
        put(getCompareField(field, operator, and, CompareFieldValueType.SIMPLE, false), value);
        return this;
    }

    private CompareField getCompareField(String field, OperatorEnum operator, Boolean and, CompareFieldValueType type, Boolean isStatement){
        String connector = "";
        if(groupStart == false) {
            groupStart = true;
        }else{
            if(and == null){
                connector = "";
            }else if(and == true){
                connector = "AND";
            }else{
                connector = "OR";
            }
        }
        return new CompareBuilder.CompareField(field, operator, connector, type, isStatement);
    }

    private String escape(String value) {
        return value.replace("\\", "\\\\").replace("_", "\\_").replace("%", "\\%");
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        for(Map.Entry<CompareField, Object> entry: this.entrySet()){
            CompareBuilder.CompareField field = entry.getKey();
            sb.append(field.getConnector() != null ? field.getConnector() : "").append(" ").
                    append(field.getField() != null ? field.getField() : "").append(" ").
                    append(field.getOperator() != null ? field.getOperator().getValue() : "").append(" ").
                    append(entry.getValue() != null ? entry.getValue() : "").append(" ");
        }
        return sb.toString();
    }

    public class CompareField{
        private String field;
        private OperatorEnum operator;
        private String connector;
        private CompareFieldValueType type;

        private Boolean isStatement = false;

        public CompareField(String field, OperatorEnum operator, String connector, CompareFieldValueType type, Boolean isStatement){
            this.field = field;
            this.operator = operator == null ? OperatorEnum.EMPTY : operator;
            this.connector = connector;
            this.type = type;
            this.isStatement = isStatement;
        }

        public String getField() {
            return field;
        }

        public OperatorEnum getOperator() {
            return operator;
        }

        public String getConnector() {
            return connector;
        }

        public Boolean isSimple() {
            return CompareFieldValueType.SIMPLE.equals(type);
        }
        public Boolean isList() {
            return CompareFieldValueType.LIST.equals(type);
        }

        public Boolean isMap() {
            return CompareFieldValueType.MAP.equals(type);
        }

        public Boolean isStatement() {
            return isStatement;
        }
    }
}


