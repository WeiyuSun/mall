<template>
    <div>

        <el-tree :data="categories" :props="defaultProps" :expand-on-click-node="false" show-checkbox node-key="catId"
            :default-expanded-keys="expandedKey" draggable :allow-drop="allowDrop" @node-drop="handleDrop">
            <span class="custom-tree-node" slot-scope="{ node, data }">
                <span>{{  node.label  }}</span>
                <span>
                    <el-button v-if="node.level <= 2" type="text" size="mini" @click="() => append(data)">
                        Add
                    </el-button>

                    <el-button v-if="node.level <= 3" type="text" size="mini" @click="() => edit(data)">
                        Edit
                    </el-button>

                    <el-button v-if="node.childNodes.length == 0" type="text" size="mini"
                        @click="() => remove(node, data)">
                        Delete
                    </el-button>
                </span>
            </span>
        </el-tree>

        <!-- a form for add/edit category -->
        <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="40%">
            <el-form label-position="left">
                <el-form-item label="Category name">
                    <el-input v-model="category.name"></el-input>
                </el-form-item>

                <el-form-item label="Icon">
                    <el-input v-model="category.icon"></el-input>
                </el-form-item>

                <el-form-item label="Product unit">
                    <el-input v-model="category.productUnit"></el-input>
                </el-form-item>
            </el-form>

            <span slot="footer" class="dialog-footer">
                <el-button @click="dialogVisible = false">
                    Cancel
                </el-button>

                <el-button type="primary" @click="submiteData">
                    Submite
                </el-button>
            </span>

        </el-dialog>
    </div>


</template>

<style>
.el-table .root {
    background: rgba(130, 255, 255, 0.286);
}

.el-table .secondLevel {
    background: rgba(200, 255, 255, 0.286);
}

.el-table .thirdLevel {
    background: rgba(250, 255, 255, 0.286);
}
</style>

<script>
//这里可以导入其他文件(比如:组件，工具 js，第三方插件 js，json文件，图片文件等等)
//例如:import 《组件名称》 from '《组件路径》';

import { SSL_OP_NO_TLSv1_1 } from 'constants';
import { Slider } from 'element-ui';
import { pid } from 'process';

export default {
    //import 引入的组件需要注入到对象中才能使用

    components: {},
    props: {},
    data() {
        return {
            pCid: [],
            updateNodes: [],
            maxLevel: 0,
            category: {
                catId: null,
                name: null,
                parentCid: null,
                catLevel: null,
                showStatus: 1,
                sort: 0,
                productUnit: null,
                productCount: 0,
                icon: null
            },

            dialogTitle: "",
            dialogType: "",
            dialogVisible: false,
            categories: [],
            expandedKey: [],
            defaultProps: {
                children: "children",
                label: "name"
            }
        }
    },
    methods: {

        resetCat() {
            this.category.name = null;
            this.category.catId = null;
            this.category.parentCid = null;
            this.category.catLevel = null;
            this.category.showStatus = 1;
            this.category.sort = 0;
            this.category.productUnit = null;
            this.category.productCount = 0;
            this.category.icon = null;
        },

        setCat(data) {
            this.category.name = data.name;
            this.category.catId = data.catId;
            this.category.parentCid = data.parentCid;
            this.category.catLevel = data.catLevel;
            this.category.showStatus = data.showStatus;
            this.category.sort = data.sort;
            this.category.productUnit = data.productUnit;
            this.category.productCount = data.productCount;
            this.category.icon = data.icon;
        },

        submiteData() {
            if (this.dialogType === 'edit') {
                this.editCategory();
            } else if (this.dialogType === 'append') {
                this.addCategory();
            }
        },

        // add a new category to database
        addCategory() {
            this.$http({
                url: this.$http.adornUrl('/product/category/save'),
                method: 'post',
                data: this.$http.adornData(this.category, false)
            }).then(({ data }) => {
                this.expandedKey = [this.category.parentCid];

                this.dialogVisible = false;
                this.getCategories();


                // reset variable value
                this.resetCat();
                console.log("expend key", this.expandedKey);
            });
        },

        editCategory() {
            this.$http({

                url: this.$http.adornUrl('/product/category/update'),
                method: 'post',
                data: this.$http.adornData({
                    catId: this.category.catId,
                    name: this.category.name,
                    icon: this.category.icon,
                    productUnit: this.category.productUnit
                }, false)
            }).then(({ data }) => {
                this.expandedKey = [this.category.parentCid];

                this.dialogVisible = false;
                this.getCategories();

                // reset variable value
                this.resetCat();
            });
        },

        getCategories() {
            this.$http({
                url: this.$http.adornUrl('/product/category/list/tree'),
                method: 'get'
            }).then(({ data }) => {
                this.categories = data.data;
            })
        },

        append(currCat) {
            this.dialogTitle = 'Add a new category'
            this.dialogType = 'append';
            this.dialogVisible = true;
            this.category.parentCid = currCat.catId;
            this.category.catLevel = currCat.catLevel * 1 + 1;
        },

        edit(data) {
            this.dialogTitle = 'Edit category'
            this.dialogType = 'edit'
            this.dialogVisible = true;

            this.$http({
                url: this.$http.adornUrl(`/product/category/info/${data.catId}`),
                method: 'get',
            }).then(({ data }) => {
                this.setCat(data.data);
            })
        },

        remove(node, data) {
            var ids = [data.catId];

            this.$confirm(`This operation will delete \'${data.name}\', do you want to continue?`, 'Warning', {
                confirmButtonText: 'Delete',
                cancelButtonText: 'Cancel',
                type: 'warning'
            }).then(() => {
                this.$http({
                    url: this.$http.adornUrl('/product/category/delete'),
                    method: 'post',
                    data: this.$http.adornData(ids, false)
                }).then(({ data }) => {
                    this.expandedKey = [node.parent.data.catId];
                    console.log("success to delete")
                    this.getCategories()
                });

                this.$message({
                    type: 'info',
                    message: 'Success to delete'
                });
            }).catch(() => {
                this.$message({
                    type: 'info',
                    message: 'Caceled'
                });
            });
        },

        countNodeLevel(node) {

            if (node.children != null && node.children.length > 0) {
                for (let i = 0; i < node.children.length; i++) {
                    if (node.children[i].catLevel > this.maxLevel) {
                        this.maxLevel = node.children[i].catLevel;
                    }

                    this.countNodeLevel(node.children[i]);
                }
            }
        },

        allowDrop(draggingNode, dropNode, type) {

            this.countNodeLevel(draggingNode.data);

            let deep = this.maxLevel - draggingNode.data.catLevel + 1

            if (type == "inner") {
                return deep + dropNode.length <= 3
            }

            return deep + dropNode.parent.level <= 3;
        },

        handleDrop(draggingNode, dropNode, dropType, ev) {
            console.log("handleDrop: ", draggingNode, dropNode, dropType);
            //1、当前节点最新的父节点id
            let pCid = 0;
            let siblings = null;
            if (dropType == "before" || dropType == "after") {
                pCid =
                    dropNode.parent.data.catId == undefined
                        ? 0
                        : dropNode.parent.data.catId;
                siblings = dropNode.parent.childNodes;
            } else {
                pCid = dropNode.data.catId;
                siblings = dropNode.childNodes;
            }

            this.pCid.push(pCid);


            //2、当前拖拽节点的最新顺序，
            for (let i = 0; i < siblings.length; i++) {
                if (siblings[i].data.catId == draggingNode.data.catId) {
                    //如果遍历的是当前正在拖拽的节点
                    let catLevel = draggingNode.level;
                    if (siblings[i].level != draggingNode.level) {
                        //当前节点的层级发生变化
                        catLevel = siblings[i].level;
                        //修改他子节点的层级
                        this.updateChildNodeLevel(siblings[i]);
                    }
                    this.updateNodes.push({
                        catId: siblings[i].data.catId,
                        sort: i,
                        parentCid: pCid,
                        catLevel: catLevel
                    });
                } else {
                    this.updateNodes.push({ catId: siblings[i].data.catId, sort: i });
                }
            }

            //3、当前拖拽节点的最新层级
            // console.log("updateNodes", this.updateNodes);
            this.$http({
                url: this.$http.adornUrl('/product/category/update/sort'),
                method: 'post',
                data: this.$http.adornData(this.updateNodes, false)
            }).then(({ data }) => {
                this.$message({
                    type: 'info',
                    message: 'Success to Update'
                });

                this.updateNodes = [];
                this.maxLevel = 0;
                this.resetCat();
                this.getCategories();
                this.expandedKey = [pCid]
            });
        },

        updateChildNodeLevel(node) {
            if (node.childNodes.length > 0) {
                for (let i = 0; i < node.childNodes.length; i++) {
                    var cNode = node.childNodes[i].data;
                    this.updateNodes.push({
                        catId: cNode.catId,
                        catLevel: node.childNodes[i].level
                    });
                    this.updateChildNodeLevel(node.childNodes[i]);
                }
            }
        },

        tableRowClassName({ row }) {
            if (row.catLevel === 1) {
                return 'root'
            } else if (row.catLevel === 2) {
                return 'secondLevel'
            } else if (row.catLevel === 3) {
                return 'thirdLevel'
            }
            return '';
        },

        getRowKeys(row) {

            return row.catId;
        },
        rowClik(row) {
            console.log(row);
            this.expandedKey = [row.catId];
            console.log(this.expandedKey)
        },
    },
    //计算属性 类似于data概念
    computed: {},
    //监控 data 中的数据变化
    watch: {},
    //生命周期 - 创建完成(可以访问当前 this 实例)
    created() {
        this.getCategories();
    },
    //生命周期 - 挂载完成(可以访问 DOM 元素)
    mounted() {
    },
    beforeCreate() { }, //生命周期 - 创建之前
    beforeMount() { }, //生命周期 - 挂载之前
    beforeUpdate() { }, //生命周期 - 更新之前
    updated() { }, //生命周期 - 更新之后
    beforeDestroy() { }, //生命周期 - 销毁之前
    destroyed() { }, //生命周期 - 销毁完成
    activated() { }, //如果页面有 keep-alive 缓存功能，这个函数会触发
}
</script>
<style lang='scss' scoped>
//@import url(); 引入公共 css 类
</style>
