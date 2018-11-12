<template>
    <el-table-column v-if="noTemplate" v-bind="$attrs"></el-table-column>
    <el-table-column v-else v-bind="$attrs" v-on="$listeners">
        <template slot-scope="scope">
            <slot v-bind="scope">
              <template v-if="$attrs.value">
                {{
                  typeof $attrs.value == 'function' ?
                  $attrs.value(scope.row[$attrs.prop], scope.$index, scope.row) : $attrs.value
                }}
              </template>
              <c-td v-else-if="$attrs.node" v-bind="$attrs"></c-td>
              <!-- <template v-else-if="$attrs.node">{{ renderChild( $createElement ) }}</template> -->

              <component
                v-else-if="$attrs.render"
                :is="renderTemplate($attrs.render(scope.row[$attrs.prop], scope.$index, scope.row))"
                v-on="$listeners"
              ></component>
              <component
                v-else-if="$attrs.component"
                :is="renderTemplate($attrs.component)"
                v-on="$listeners"
              ></component>
              <component
                v-else-if="$attrs.template"
                :is="renderTemplate($attrs.template)"
                v-on="$listeners"
              ></component>
            </slot>
        </template>
    </el-table-column>
</template>

<script>
import Vue from 'vue'
import CTd from './CTd'
export default{
  name: 'CTd2',
  components: { CTd },
  props:{
    index: {}
  },
  data(){
    return {
    }
  },
  mounted(){

  },
  computed: {
    noTemplate: function () {
      //console.info(this.$attrs)
      return !this.$attrs.hasOwnProperty("value")
          && !this.$slots.default
          && Object.keys(this.$scopedSlots).length == 0
          && !this.$attrs.hasOwnProperty("render")
          && !this.$attrs.hasOwnProperty("component")
          && !this.$attrs.hasOwnProperty("template")
          && !this.$attrs.hasOwnProperty("html")
    }
  },
  render: function (createElement) {
    console.info("createElement")
  },
  created() {
    /*this.createElement((h) => {
      console.info(h)
      return h(this.$attrs.vNode)
    })*/
  },
  createElement(){
    console.info(12313)
  },
  methods: {
    renderTemplate(template){
      console.info(template)
      return Vue.compile(template);
    },
    renderChild(h){
       const node = this.$attrs.node
       delete this.$attrs.node
      return node
    },
    compile () {
        if (this.column.render) {
            const $parent = this.content;
            const template = this.column.render(this.row, this.column, this.index);
            const cell = document.createElement('div');
            cell.innerHTML = template;
            const _oldParentChildLen = $parent.$children.length;
            $parent.$compile(cell);    // todo 这里无法触发 ready 钩子
            const _newParentChildLen = $parent.$children.length;
            if (_oldParentChildLen !== _newParentChildLen) {    // if render normal html node, do not tag
                this.uid = $parent.$children[$parent.$children.length - 1]._uid;    // tag it, and delete when data or columns update
            }
            this.$el.innerHTML = '';
            this.$el.appendChild(cell);
        }
    },
  }
}
</script>
