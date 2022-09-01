<template>
  <el-dialog :title="!dataForm.id ? 'Add new brand' : 'Edit brand'" :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()"
      label-width="140px">
      <el-form-item label="name" prop="name">
        <el-input v-model="dataForm.name" placeholder="Brand Name"></el-input>
      </el-form-item>
      <el-form-item label="Logo" prop="logo">
        <!-- <el-input v-model="dataForm.logo" placeholder="Logo address"></el-input> -->
        <single-upload v-model="dataForm.logo">

        </single-upload>
      </el-form-item>
      <el-form-item label="Description" prop="descript">
        <el-input v-model="dataForm.descript" placeholder="Optional"></el-input>
      </el-form-item>
      <el-form-item label="Status" prop="showStatus">
        <!-- <el-input v-model="dataForm.showStatus" placeholder="Status"></el-input> -->
        <el-switch v-model="dataForm.showStatus" active-color="#13ce66" inactive-color="#ff4949" :active-value="1"
          :inactive-value="0"></el-switch>
      </el-form-item>
      <el-form-item label="Index" prop="firstLetter">
        <el-input v-model="dataForm.firstLetter" placeholder="The character of search index"></el-input>
      </el-form-item>
      <el-form-item label="Sort" prop="sort">
        <el-input v-model="dataForm.sort" placeholder="Sort"></el-input>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">Cancel</el-button>
      <el-button type="primary" @click="dataFormSubmit()">Submite</el-button>
    </span>
  </el-dialog>
</template>

<script>
import singleUpload from "@/components/upload/singleUpload"
import { fdatasyncSync } from "fs"
export default {
  components: { singleUpload },
  data() {
    return {
      visible: false,
      dataForm: {
        brandId: 0,
        name: '',
        logo: '',
        descript: '',
        showStatus: 1,
        firstLetter: '',
        sort: 0
      },
      dataRule: {
        name: [{
          validator: (rule, value, callback) => {
            // TODO: do not allowed special characters
            if (value.length === 0) {
              callback(new Error("Name cannot be empty"))
            } else if (value.length > 50) {
              callback(new Error("Name length cannot over 50 letters"))
            } else if (/^[^\s]+(\s+[^\s]+)*$/.test(value) === false) {
              callback(new Error("Cannot start or end with space"));
            } else {
              callback();
            }
          }, trigger: 'blur'
        }],

        firstLetter: [{
          validator: (rule, value, callback) => {
            if (value.length !== 1) {
              callback(new Error("Index is exactly one character"))
            } else if (/^[A-Za-z]+$/.test(value) === false) {
              callback(new Error("Index should be from a-z or A-Z"))
            } else {
              callback();
            }
          }, trigger: 'blur'
        }],

        sort: [
          {
            validator: (rule, value, callback) => {
              //let int_value = Number(value);
              if (/^[0-9]+?$/.test(value) === false || value > 100) {
                callback(new Error("Sort value should be an integer between 0 and 100"))
              } else {
                callback();
              }
            }, trigger: 'blur'
          }
        ],

        descript: [
          {
            validator: (rule, value, callback) => {
              if (/^[^\s]+(\s+[^\s]+)*$/.test(value) === false && value !== "") {
                callback(new Error("Cannot start or end with space"));
              } else if(value.length > 1000){ 
                callback(new Error("Length cannot over 1000"))
              } else {
                callback();
              }
            }, trigger: 'blur'
          }
        ]
      }
    }
  },
  methods: {
    init(id) {
      this.dataForm.brandId = id || 0
      this.visible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].resetFields()
        if (this.dataForm.brandId) {
          this.$http({
            url: this.$http.adornUrl(`/product/brand/info/${this.dataForm.brandId}`),
            method: 'get',
            params: this.$http.adornParams()
          }).then(({ data }) => {
            if (data && data.code === 0) {
              this.dataForm.name = data.brand.name
              this.dataForm.logo = data.brand.logo
              this.dataForm.descript = data.brand.descript
              this.dataForm.showStatus = data.brand.showStatus
              this.dataForm.firstLetter = data.brand.firstLetter
              this.dataForm.sort = data.brand.sort
            }
          })
        }
      })
    },
    // 表单提交
    dataFormSubmit() {
      this.$refs['dataForm'].validate((valid) => {
        
        if (valid) {
          console.log("dataform", this.dataForm);
          this.$http({
            url: this.$http.adornUrl(`/product/brand/${!this.dataForm.brandId ? 'save' : 'update'}`),
            method: 'post',
            data: this.$http.adornData({
              'brandId': this.dataForm.brandId || undefined,
              'name': this.dataForm.name,
              'logo': this.dataForm.logo,
              'descript': this.dataForm.descript,
              'showStatus': this.dataForm.showStatus,
              'firstLetter': this.dataForm.firstLetter,
              'sort': this.dataForm.sort
            })
          }).then(({ data }) => {
            if (data && data.code === 0) {
              this.$message({
                message: '操作成功',
                type: 'success',
                duration: 1500,
                onClose: () => {
                  this.visible = false
                  this.$emit('refreshDataList')
                }
              })
            } else {
              this.$message.error(data.msg)
            }
          })
        }
      })
    }
  }
}
</script>
