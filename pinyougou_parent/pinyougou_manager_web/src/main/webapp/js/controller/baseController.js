app.controller("baseController", function ($scope) {
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

    //初始化searchEntity
    $scope.searchEntity = {};
    //初始化Ids
    $scope.Ids = [];

    $scope.reloadList = function () {
        $scope.search($scope.paginationConf.currentPage, $scope.paginationConf.itemsPerPage);
    };

    //复选框选中
    $scope.selectKey = function ($event, id) {
        // $event用于判断复选框的勾选状态
        if ($event.target.checked) {
            $scope.Ids.push(id)//向数组中放数据
        } else {
            var index = $scope.Ids.indexOf(id);
            $scope.Ids.splice(index, 1)//从数组中移除数据
        }
    }
});
