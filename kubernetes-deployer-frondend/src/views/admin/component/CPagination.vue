<template>
    <div class="demonstration">
        <el-pagination
            v-if="total > 0"
            :page-sizes="pageSizes"
            :page-size="pageSize"
            :layout="layout"
            :total="total"
            :current-page="currentPage"
            @current-change="$emit('current-change', $event)"
            @size-change="$emit('size-change', $event)">
        </el-pagination>
    </div>
</template>

<script>
export default{
  name: 'CPagination',
  props:{
    currentPage: {
      default: 1
    },
    total: {
      default: 0
    },
    pageSize: {
      default: 20
    },
    pageSizes: {
      type: Array,
      default: () => [10, 20, 50, 100]
    },
    layout: {
      default: 'total, sizes, prev, pager, next, jumper'
    }
  },
  data(){
    return {

    }
  },
  methods: {

  }
}
</script>
