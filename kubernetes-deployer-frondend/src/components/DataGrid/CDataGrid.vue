<script>
import Sortable from 'sortablejs'
import CTable from './CTable'
import CPagination from './CPagination'
export default {
    name: 'CDataGrid',
    components: { CTable, CPagination},
    props: {
        events: {
            default: {}
        },
        paginations: {
            default: {}
        },
        columns: {
            default: () => []
        },
        dataLoadHandler: {
        },
        searchQueryHandler: {
            default: () => (query) => query
        },
        formatRowData: {
            default: () => (data) => data
        },
        sortChangeHandler: undefined
    },
    data() {
        return {
            sort: '',
            listLoading: false,
            tableData: [],
            attrColumns: [],
            attrNodes: []
        }
    },
    computed: {
        mPaginations: function (){
            return Object.assign({
                page: 1,
                total: 0,
                currentPage: 1,
                pageSize: 5,
            }, this.$props.paginations)
        },
        mColumns: function(){
            return this.columns.concat(this.attrColumns)
        },
        mSortChangeHandler: function(){
            if(this.$props.sortChangeHandler !== undefined){
                return this.$props.sortChangeHandler
            }else{
                return ({column, prop, order}) => {
                    if(prop){
                        this.sort = prop + '.' + (order == 'descending' ? 'desc' : 'asc')
                    }else{
                        this.sort = ''
                    }
                    this.fetchData();
                }
            }
        }
    },
    created() {
      if(this.$slots.hasOwnProperty("tableBody")){
          this.$slots.tableBody.map((slot, index) => {
              if(slot.data != undefined && slot.data.hasOwnProperty("attrs")){
                  this.attrColumns.push(slot.data.attrs)
                  this.attrNodes.push(slot)
              }
          })
      }
      //delete this.attrNodes[1]
      //delete this.attrNodes[2]
      this.fetchData()
    },
    render(h){
      return h('div', {}, [
          this.renderTable(h),
          this.renderPagination(h)
      ])
    },
    methods: {
        renderTable(h){
          console.info(this.mColumns)
          // console.info(this.$scopedSlots)
          // console.info(this.$slots)
          return h(CTable, {
            props: {
              tableData: this.tableData,
              listLoading: this.listLoading,
              columns: this.mColumns
            },
            on: {
              "sort-change": this.mSortChangeHandler
            },
            slots: {
              tableBody: this.$scopedSlots.tableBody
            },
          }, this.attrNodes)
        },
        renderPagination(h){
          return h(CPagination, {
            props: { ...this.mPaginations },
            on: {
              "current-change": this.handleCurrentChange,
              "size-change": this.handleSizeChange
            },
          })
        },
        getDefaultSearchQuery(){
            return {
                sort: this.sort,
                page: this.mPaginations.currentPage - 1,
                size: this.mPaginations.pageSize,
            }
        },
        fetchData() {
            this.listLoading = true
            this.$emit("fetch-data-start")
            this.$props.dataLoadHandler(this.$props.searchQueryHandler(this.getDefaultSearchQuery()))
            .then(({ data }) => {
                this.tableData = data.data.map(item => this.$props.formatRowData(item))
                this.mPaginations.total = data.total;
                this.listLoading = false
                this.$emit("fetch-data-end", true)
            }).catch(() => {
                this.listLoading = false
                this.$emit("fetch-data-end", false)
            })
        },
        handleCurrentChange(currentPage) {
            this.$emit("current-change", currentPage)
            this.mPaginations.currentPage = currentPage
            this.fetchData();
        },
        handleSizeChange(pageSize) {
            this.$emit("size-page", pageSize)
            this.mPaginations.pageSize = pageSize
            this.fetchData();
        },
    }
}
</script>
