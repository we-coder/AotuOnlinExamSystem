var treeData = [{
    "id": 1,
    "name": "必体验",
    "child": []
}, {
    "id": 2,
    "name": "休闲生活",
    "child": [{
        "id": 15,
        "level": 2,
        "name": "热门城市",
        "child": [{
            "id": 5,
            "name": "乌鲁木齐",
            "child": []
        }, {
            "id": 6,
            "name": "北京",
            "child": []
        }, {
            "id": 7,
            "name": "南京",
            "child": []
        }, {
            "id": 8,
            "name": "昌吉",
            "child": []
        }, {
            "id": 9,
            "name": "西安",
            "child": []
        }]
    }, {
        "id": 16,
        "name": "主题",
        "child": [{
            "id": 10,
            "name": "城市经典",
            "child": []
        }, {
            "id": 11,
            "name": "四季美食",
            "child": []
        }, {
            "id": 12,
            "name": "周末减压",
            "child": []
        }, {
            "id": 13,
            "name": "亲子成长",
            "child": []
        }, {
            "id": 14,
            "name": "聚会团建",
            "child": []
        }]
    }]
}, {
    "id": 3,
    "name": "慢旅",
    "child": []
}, {
    "id": 4,
    "name": "酒店民宿",
    "child": []
}];

Vue.component('item', {
    template: '#treeTemplate',
    props: ['tree'],
    data: function() {
        return {
            form: '',
            layer: "",
        }
    },
    mounted() {
        var that = this;
        layui.use(['form', 'layer'], function() {
            that.form = layui.form;
            that.layer = layui.layer;
        });
    },
    methods: {
        toggle: function(i) {
            this.tree[i].open = !this.tree[i].open;
            this.$set(this.tree, i, this.tree[i]);
        },
        isFolder: function(data) {
            return data.child && data.child.length
        },
        changeCheck(i) {
            var that = this;
            console.log(that.tree)
        },

        //添加节点
        addNode(i) {
            var that = this;
            var addLayer = that.layer.open({
                type: 1,
                content: $("#addNodelayer"),
                area: ['480px', '430px'],
                title: "添加标签",
                success: function() {

                    that.form.on("submit(addDataBtn)", function(data) {
                        layer.msg(JSON.stringify(data.field));
                        var postdata = JSON.parse(JSON.stringify(data.field));

                        var Pdata = {
                            child: [],
                            name: postdata.name,
                            id: that.tree[i].id,
                        }

                        that.tree[i].child.push(Pdata);
                        that.tree[i].open = true;
                        that.layer.close(addLayer);
                        layer.msg('添加成功！');
                        return false;
                    })
                }
            });
        },
        //编辑节点
        editNode(i) {
            var that = this;
            var addLayer = that.layer.open({
                type: 1,
                content: $("#editNodelayer"),
                area: ['480px', '430px'],
                title: "修改标签",
                success: function() {
                    vm.$data.name = that.tree[i].name;

                    that.form.on("submit(editDataBtn)", function(data) {
                        layer.msg(JSON.stringify(data.field));
                        var postdata = JSON.parse(JSON.stringify(data.field));
                        that.tree[i].name = postdata.className;
                        that.layer.close(addLayer);
                        layer.msg('添加成功！');
                        return false;
                    })

                }
            });
        },
        //删除节点
        delNode(i) {
            var that = this;
            that.layer.confirm('确定删除该分类？', {
                btn: ["确定", '取消'],
            }, function(index, layero) {
                that.tree.splice(i, 1);
                layer.close(index);
                layer.msg('删除成功！');
            }, function(index) {
                //按钮【按钮二】的回调
            });

        }
    },
});
var vm = new Vue({
    el: '#vm',
    data: {
        treeData: treeData,
        name: "",
    },
    mounted() {
        var that = this;
        // 异步获取 数据
        that.treeAjax();
    },
    methods: {
        treeAjax() {
            var that = this;
            // var url = "../../../../../https@www.easy-mock.com/mock/5b85fc814951ae08c662f44e/index/index"
            // $.ajax({
            //     type: "get",
            //     url: url,
            //     dataType: "json",
            //     success: function(res) {
            //         that.treeData = JSON.parse(JSON.stringify(res.data));
            //     }
            // });
        }
    },
    created: function() {
        function _addOpen(data) {
            for (var i = 0; i < data.length; i++) {
                data[i]['open'] = false;
                if (data[i].child.length > 0) {
                    _addOpen(data[i].child);
                }
            }
        }
        _addOpen(this.treeData);
    }
});