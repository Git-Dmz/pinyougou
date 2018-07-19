//控制层
app.controller('itemCatController', function ($scope, typeTemplateService, itemCatService) {

    //分页控件设置
    $scope.paginationConf = {
        currentPage: 1, //当前页
        totalItems: 10, //总条数
        itemsPerPage: 10, //每页显示条数
        perPageOptions: [10, 20, 30, 40, 50],
        onChange: function () {
            $scope.reloadList();//重新加载
        }
    };
    //初始化
    $scope.id = 0;
    $scope.grade = 1;
    $scope.entity1 = null;
    $scope.entity2 = null;
    $scope.parentId = 0;    //要保存的数据的父ID

    $scope.reloadList = function () {
        // $scope.search($scope.paginationConf.currentPage, $scope.paginationConf.itemsPerPage);
        $scope.findParentId($scope.paginationConf.currentPage, $scope.paginationConf.itemsPerPage, $scope.id);
    };

    $scope.findByParentId = function (id) {
        $scope.id = id;
        $scope.findParentId($scope.paginationConf.currentPage, $scope.paginationConf.itemsPerPage, $scope.id);
    };

    //面包屑导航
    $scope.setGrade = function (grade, pojo) {
        $scope.grade = grade;
        $scope.parentId = pojo.id;

        if ($scope.grade == 1) {
            $scope.entity1 = null;
            $scope.entity2 = null;
        }

        if ($scope.grade == 2) {
            $scope.entity1 = pojo;
            $scope.entity2 = null;  //如果$scope.grade等于2 面包屑第二层赋null
        }

        if ($scope.grade == 3) {
            $scope.entity2 = pojo;  //如果$scope.grade等于3 则隐藏下级按钮
        }

    };

    //查询下级列表
    $scope.findParentId = function (pageNum, pageSize, id) {
        $scope.id = id;
        itemCatService.findParentId(pageNum, pageSize, $scope.id).success(function (response) {
            $scope.list = response.rows;
            $scope.paginationConf.totalItems = response.total;
        })
    };

    //读取列表数据绑定到表单中
    $scope.findAll = function () {
        itemCatService.findAll().success(
            function (response) {
                $scope.list = response;

            }
        );
    };

    //分页
    $scope.findPage = function (page, rows) {
        itemCatService.findPage(page, rows).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    };

    //查询实体
    $scope.findOne = function (id) {
        itemCatService.findOne(id).success(
            function (response) {
                $scope.entity = response;
            }
        );
    };

    //保存
    $scope.save = function () {
        var serviceObject;//服务层对象
        $scope.entity['parentId']=$scope.parentId;
        if ($scope.entity.id != null) {//如果有ID
            serviceObject = itemCatService.update($scope.entity); //修改
        } else {
            serviceObject = itemCatService.add($scope.entity);//增加
        }
        serviceObject.success(
            function (response) {
                if (response.success) {
                    //重新查询
                    $scope.findByParentId($scope.parentId);//重新加载
                } else {
                    alert(response.message);
                }
            }
        );
    };

    //批量删除
    $scope.del = function () {
        //获取选中的复选框
        itemCatService.del($scope.Ids).success(
            function (response) {
                if (response.success) {
                    $scope.reloadList();//刷新列表
                }
            }
        );
    }

    $scope.searchEntity = {};//定义搜索对象

    //搜索
    $scope.search = function (pageNum, pageSize) {
        itemCatService.search(pageNum, pageSize, $scope.searchEntity).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    };

    //查询商品类型模板
    $scope.findTypeTemplate = function () {
        typeTemplateService.findAll().success(function (response) {
            $scope.TbTypeTemplate = response;
        })
    }

});	
