<template>
<div>
    <div class="pull-right">
        <el-popover
          placement="bottom"
          ref="settingPopover"
          width="50"
          trigger="click">
          <div class="dndList-list" :style="{width:50}">
                <draggable :list="columns" class="dragArea" :options="{group:'setting'}" @start="dragging=true" @end="dragging=false">
                  <el-checkbox v-show="column.prop" :label="column.label" name="setting" v-for="(column, key) in columnsList" :checked="!column.noDisplay" :key="'setting-'+key" :style="{margin: '8px 0', display: 'block'}" @change="isDisplay(key, $event)"></el-checkbox>
                </draggable>
                <el-button slot="footer" >重置</el-button>
          </div>
        </el-popover>
        <el-button v-popover:settingPopover>设置</el-button>
    </div>
<el-table
  :data="tableData"
  v-loading.body="listLoading"
  element-loading-text="拼命加载中"
  v-on="$listeners"
>
<slot>
  <c-td
    v-if="!column.noDisplay"
    v-for="(column, key) in columnsList"
    v-bind="column"
    :key="key"
    :index="key"
    v-on="$listeners"
    >
      <template v-if="typeof $scopedSlots[column.prop] !== 'undefined'" slot-scope="scope">
          <slot :name="column.prop" v-bind="scope"></slot>
      </template>
  </c-td>
</slot>
 </el-table>
</div>
</template>

<script>
import Vue from 'vue'
import CTd from './CTd2'
import draggable from 'vuedraggable'
export default{
 name: 'CTable',
 components: { draggable, CTd },
 props:{
    tableData: {},
    listLoading: {},
    events: {},
    columns: {
        default: []
    },
    mColumns: []
 },
 data(){
    return {
        dragging: false
    }
 },
 mounted(){
    //this.$slots.tableBody = this.$slots.default
    //console.info(this.$slots)
    const setting = true;
    this.$props.columns.forEach((column)=>{
        if(column.hasOwnProperty('component')){
            this.$options.components['__component_' + column.prop + '__'] = column.component
        }else if(column.hasOwnProperty('template')){
            if(!column.template.trim().startsWith('<')){
                column.template = '<div>' + column.template + '</div>'
            }
            const component = {
              props: ['$row', '$value', '$index', '$scope', '$column'],
              template: column.template
            }
            this.$options.components['__template_' + column.prop + '__'] = component
        }
    })
 },
 computed: {
    columnsList: {
        get(){
            return this.columns.filter(v => {
                v.noDisplay = v.noDisplay ? v.noDisplay : false;
                return v
            })
        },
        set(value){

        }
    },
    settings: {
      get(){
        return this.columns.filter(v => {
            return v
        })
      },
      set(value) {
         this.$store.commit(this.$props.columns, value)
      }
    }
  },
 methods: {
    isDisplay (key, $event){
        let column = this.columns[key]
        column.noDisplay = !$event;
        Vue.set(this.columns, key, column)
    }
 }
}
</script>
