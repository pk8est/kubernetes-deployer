<template>
  <div class="app-container calendar-list-container">
    <div class="app-container">
      <c-data-grid
        :dataLoadHandler="dataLoadHandler"
        :searchQueryHandler="searchQueryHandler"
        :formatRowData="formatRowData"
        :attributes="attributes"
        :paginations="paginations"
        :events="events"
        :columns="columns"
        @handle-update="handleUpdate"
      >

     <!-- <template slot="tableBody">
        <c-td key="expand"  type="expand" label="详细" ></c-td>
        <c-td key="selection"  type="selection" ></c-td>
        <c-td key="index"  type="index" label="序号" ></c-td>
        <c-td key="managerId"  prop="managerId" label="ID" ></c-td>
        <c-td key="udb"  prop="udb" label="UDB" ></c-td>
        <c-td key="realname" prop="realname" label="Name" >
            <el-button @click="clickHandler" size="mini" slot-scope="scope">{{ scope.row.realname }}</el-button>
        </c-td>
      </template> -->


      <el-button slot="test" @click="clickHandler" size="mini" slot-scope="scope">{{ scope.row.realname }}</el-button>

      <template slot="realname" scope="{ $row }">
          <el-button  size="mini">{{$row.realname}}</el-button>
      </template>

      <template slot="hander" scope="{ $row, $index }">
          <el-button type="primary" size="mini" @click="handleUpdate($row, $index)">{{$t('table.edit')}}</el-button>
      </template>

      <!-- </template> -->
          <!-- <template slot="table-body">
            <el-table-column
                prop='id'
                label='主键'
            >
              <template slot-scope="{row}">{{row.id}}</template>
            </el-table-column>
            <el-table-column >
              <template slot-scope="{}">IDDDDDDDDDD</template>
            </el-table-column>
          </template> -->
      </c-data-grid>
    </div>

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form :rules="rules" ref="dataForm" :model="temp" label-position="left" label-width="70px" style='width: 400px; margin-left:50px;'>
        <el-form-item :label="$t('table.date')" prop="timestamp">
          <el-date-picker v-model="temp.timestamp" type="datetime" placeholder="Please pick a date">
          </el-date-picker>
        </el-form-item>
        <el-form-item :label="$t('table.title')" prop="title">
          <el-input v-model="temp.title"></el-input>
        </el-form-item>
        <el-form-item :label="$t('table.importance')">
          <el-rate style="margin-top:8px;" v-model="temp.importance" :colors="['#99A9BF', '#F7BA2A', '#FF9900']" :max='3'></el-rate>
        </el-form-item>
        <el-form-item :label="$t('table.remark')">
          <el-input type="textarea" :autosize="{ minRows: 2, maxRows: 4}" placeholder="Please input" v-model="temp.remark">
          </el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">{{$t('table.cancel')}}</el-button>
        <el-button v-if="dialogStatus=='create'" type="primary" @click="createData">{{$t('table.confirm')}}</el-button>
        <el-button v-else type="primary" @click="updateData">{{$t('table.confirm')}}</el-button>
      </div>
    </el-dialog>
  </div>
</template>


<script>
import { createElement } from 'vue'
import CDataGrid from '../component/CDataGrid0'
import CTd from '../component/CTd2'
import { getList } from '@/api/user'
import { parseTime } from '@/utils/index'

export default {
  components: { CDataGrid, CTd },
  data() {
    return {
      attributes: {
          //style: "width: 100%",
          //height: "250",
          //setting: {prop: "hander"},
      },
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: 'Edit',
        create: 'Create'
      },
      rules: {
        type: [{ required: true, message: 'type is required', trigger: 'change' }],
        timestamp: [{ type: 'date', required: true, message: 'timestamp is required', trigger: 'change' }],
        title: [{ required: true, message: 'title is required', trigger: 'blur' }]
      },
      paginations: {},
      dataLoadHandler: getList,
      searchQueryHandler: this.getSearchQuery,
      formatRowData: this.showRowData,
      events: {
          'cell-click': (count) => console.info('cell-click', count),
          'cell-click.once': () => console.info('cell-click.once'),
          'cell-dblclick': () => console.info('cell-dblclick'),
          'handle-update': (row) => {
            console.info(5555)
          },
          //'sort-change': () => console.info('sort-change'),
      },
      statusArr: {
        '1' : { name: "正常", label: 'info' },
        '0' : { name: "未启用", label: '' },
        '-1': { name: "禁用", label: '' }
      },
      temp: {
        id: undefined,
        udb: '',
        realname: '',
        ctime: new Date().getTime(),
        lastLogin: new Date().getTime(),
        status: 1
      },
      columns: [
        {type: 'selection'},
        {type: 'expand'},
        {type: 'index'},
        {prop: 'managerId', label: 'ID', sortable: 'custom', fixed: true},
        {prop: 'realname', label: '名字'},
        {prop: 'ctime', label: '创建时间', template: `{{$value | parseTime('{y}-{m}-{d} {h}:{i}')}}`},
        {prop: 'status', label: '状态'},
        /*{prop: 'ctime', label: '创建时间', template: `{{$value | parseTime('{y}-{m}-{d} {h}:{i}')}}`},
        {prop: 'lastLogin', label: '最后登陆时间', template: `{{$value | parseTime('{y}-{m}-{d} {h}:{i}')}}`},
        {prop: 'status', label: '状态'},
        {prop: 'hander', label: '操作', fixed: "right", template: `<el-button type="primary" size="mini" @click="$emit('handle-update', $row)">{{$t('table.edit')}}</el-button>`}
        */
        // {prop: 'id', label: 'ID', sortable: 'custom', fixed: true},
        // {prop: 'create_id', label: '创建人ID', value: '<div>123456</div>'},
        // {prop: 'name', label: '名字', sortable: true},
        // {prop: 'email', label: '邮箱', noDisplay: true, value: (value, index, row) => {
        //   //console.info(this, value, index, row)
        //   return parseTime(value)
        // }},
        // /*{prop: 'email', label: '邮箱2', render: (value, index, row) => {
        //     return `<div>${index}.${value}</div>`
        // }},*/
        // {prop: 'status', label: '状态', template: `status: {{$value}}, index: {{$index}}`},
        // {prop: 'created_at', label: '创建时间', template: '123{{$row.created_at}}'},
        // {prop: 'login_at', label: '最后登陆'},
        // {prop: 'updated_at', label: '最后更新'},
        // {prop: 'deleted_at', label: '删除时间'},
        // {prop: 'remark', label: '备注'},
        // {prop: 'hander', label: '操作', fixed: "right", template: `<a href="/data/web">查看</a>`}
      ]
    }
  },
  methods: {
    getSearchQuery(query){
      return Object.assign(query, {
          sex: 'sex',
          email: 'email',
      })
    },
    getStatus(key){
      if(this.statusArr.hasOwnProperty(key)){
        return this.statusArr[key]
      }else{
        return {}
      }
    },
    showRowData(data){
      // var status = this.getStatus(data.status)
      // data.status = status.name
      // data.created_at = parseTime(data.created_at)
      // data.login_at = parseTime(data.login_at)
      // data.deleted_at = parseTime(data.deleted_at)
      // data.hander = '<a>查看</a>'
      return data
    },
    handleUpdate(row, index) {
      console.info(row, index)
      this.temp = Object.assign({}, row) // copy obj
      this.temp.timestamp = new Date(this.temp.timestamp)
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    updateData(){

    },
    clickHandler(){
      console.info("clickHandler")
    }
  }
}
</script>
