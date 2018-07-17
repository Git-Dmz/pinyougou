app.controller("specificationController", function ($scope, $controller, specificationService) {//依赖注入service

    $controller("baseController",{$scope:$scope});//继承baseController控制器

    //初始化entity
    $scope.entity={specificationOptionList:[]};

    //动态添加规格项
    $scope.addSpecification=function () {
        $scope.entity.specificationOptionList.push({});
    };

    //动态删除规格项
    $scope.delSpecification=function (index) {
        $scope.entity.specificationOptionList.splice(index,1);
    };

    //分页显示
    $scope.findPage = function (pageNum, pageSize) {
        specificationService.findPage(pageNum, pageSize).success(function (response) {
            $scope.list = response.rows;
            $scope.paginationConf.totalItems = response.total;
        })
    };

    //条件查询
    $scope.search = function (pageNum, pageSize) {
        specificationService.search(pageNum, pageSize, $scope.searchEntity).success(function (response) {
            $scope.list = response.rows;
            $scope.paginationConf.totalItems = response.total;
        })
    };

    //添加品牌
    $scope.save = function () {
        var resultObj = null;
        if ($scope.entity.specification.id != null) {
            resultObj = specificationService.update($scope.entity);
        } else {
            resultObj = specificationService.add($scope.entity);
        }
        resultObj.success(function (response) {
            //需要的response： {"true"|"false":"添加成功"|"添加失败"}
            if (response.success) {
                $scope.reloadList();
            } else {
                alert(response.message);
            }
        })
    };

    //修改品牌
    $scope.findOne = function (id) {
        specificationService.findOne(id).success(function (response) {
            //将response赋值给entity来回显数据
            $scope.entity = response;
        })
    };

    //删除品牌
    $scope.del = function () {
        if ($scope.Ids.length > 0) {
            if (window.confirm("确定要删除吗？")) {
                specificationService.del($scope.Ids).success(function (response) {
                    if (response.success) {
                        $scope.reloadList();
                        $scope.Ids = [];
                    } else {
                        alert(response.message);
                    }
                })
            }
        }
    };

//查询所有
    $scope.findAll = function () {
        specificationService.findAll().success(function (response) {
            $scope.list = response;
        })
    };

});

