<script>
export default{
  name: 'CTd',
  props:{},
  data(){
    return {
    }
  },
  render: function (createElement) {
    const node = this.$attrs.node
    delete this.$attrs.node
    return node
  }
}
</script>
