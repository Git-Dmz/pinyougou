//控制层
app.controller('typeTemplateController', function ($scope, $controller, specificationService, brandService, typeTemplateService) {

    $controller('baseController', {$scope: $scope});//继承

    //初始化entity
    // $scope.entity = {customAttributeItems: []};

    //动态添加扩展属性栏
    $scope.addCustomAttributeItems = function () {
        $scope.entity.customAttributeItems.push({});
    };

    //动态删除扩展属性栏
    $scope.delCustomAttributeItems = function (index) {
        $scope.entity.customAttributeItems.splice(index, 1);
    };

    //
    $scope.arrayListToString = function (arratList) {
        arratList = JSON.parse(arratList);
        var str = "";
        for (var i = 0; i < arratList.length; i++) {
            if (i == arratList.length - 1) {
                str += arratList[i].text;
            } else {
                str += arratList[i].text + ",";
            }
        }
        return str;
    };

    //显示品牌数据
    $scope.findBrandList = function () {
        brandService.findBrandList().success(function (response) {
            $scope.brandList = {data: response};
        })
    };

    //显示规格数据
    $scope.findSpecList = function () {
        specificationService.findSpecList().success(function (response) {
            $scope.specList = {data: response};
        })
    };

    //读取列表数据绑定到表单中  
    $scope.findAll = function () {
        typeTemplateService.findAll().success(
            function (response) {
                $scope.list = response;
            }
        );
    };


    //分页
    $scope.findPage = function (pageNum, pageSize) {
        typeTemplateService.findPage(pageNum, pageSize).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    };

    //查询实体
    $scope.findOne = function (id) {
        typeTemplateService.findOne(id).success(
            function (response) {
                response.specIds = JSON.parse(response.specIds);
                response.brandIds = JSON.parse(response.brandIds);
                response.customAttributeItems = JSON.parse(response.customAttributeItems);
                $scope.entity = response;
            }
        );
    };

    //保存
    $scope.save = function () {
        var serviceObject;//服务层对象
        if ($scope.entity.id != null) {//如果有ID
            serviceObject = typeTemplateService.update($scope.entity); //修改
        } else {
            serviceObject = typeTemplateService.add($scope.entity);//增加
        }
        serviceObject.success(
            function (response) {
                if (response.success) {
                    //重新查询
                    $scope.reloadList();//重新加载
                } else {
                    alert(response.message);
                }
            }
        );
    };


    //批量删除
    $scope.del = function () {
        //获取选中的复选框
        typeTemplateService.del($scope.Ids).success(
            function (response) {
                if (response.success) {
                    window.confirm("确定要删除选中的数据吗？")
                    {
                        $scope.reloadList();//刷新列表
                        $scope.Ids = [];
                    }
                } else {
                    alert(response.message);
                }
            }
        );
    };

    $scope.search = function (pageNum, pageSize) {
        typeTemplateService.search(pageNum, pageSize, $scope.searchEntity).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    }

});	
