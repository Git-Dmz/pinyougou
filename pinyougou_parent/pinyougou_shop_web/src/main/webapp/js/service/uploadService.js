app.service("uploadService",function ($http) {

    this.uploadFile=function () {
        var formData=new FormData(); //html5的对象
        formData.append("file",file.files[0]);  //file.files[0] js   file看做document中的文件
        return $http({

     //     文件上传，通用规则：
     //     页面三要素： 表达的提交一定是post
     //     enctype一定是 multipart/form-data
     //     input的类型 file
            method:'post',
            url:'../upload/uploadFile',
            data:formData,
            headers: {'Content-Type':undefined},  // 相当于multipart/form-data
            transformRequest: angular.identity
        })
    }
});