app.controller("brandController", function ($scope, $controller, brandService) {//依赖注入service

    $controller("baseController",{$scope:$scope});//继承baseController控制器

    //分页显示
    $scope.findPage = function (pageNum, pageSize) {
        brandService.findPage(pageNum, pageSize).success(function (response) {
            $scope.list = response.rows;
            $scope.paginationConf.totalItems = response.total;
        })
    };

    //条件查询
    $scope.search = function (pageNum, pageSize) {
        brandService.search(pageNum, pageSize, $scope.searchEntity).success(function (response) {
            $scope.list = response.rows;
            $scope.paginationConf.totalItems = response.total;
        })
    };

    //添加品牌
    $scope.save = function () {
        var resultObj = null;
        if ($scope.entity.id != null) {
            resultObj = brandService.update($scope.entity);
        } else {
            resultObj = brandService.add($scope.entity);
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
        brandService.findOne(id).success(function (response) {
            //将response赋值给entity来回显数据
            $scope.entity = response;
        })
    };


    //删除品牌
    $scope.del = function () {
        if ($scope.Ids.length > 0) {
            if (window.confirm("确定要删除吗？")) {
                brandService.del($scope.Ids).success(function (response) {
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
        brandService.findAll().success(function (response) {
            $scope.list = response;
        })
    };


//复选框选中

    $scope.selectKey = function ($event, id) {
        // $event用于判断复选框的勾选状态
        if ($event.target.checked) {
            $scope.Ids.push(id)
        } else {
            var index = $scope.Ids.indexOf(id);
            $scope.Ids.splice(index, 1)
        }
    }
});

