app.controller("sellerController",function ($scope, sellerService) {

    //初始化entity
    $scope.entity={};

    $scope.save=function () {
        sellerService.add($scope.entity).success(function (response) {
            if (response.success){
                alert("审核已提交，请耐心等待。。。");
                window.location.href="/register.html";
            }else {
                alert(response.message);
            }
        })
    }
});