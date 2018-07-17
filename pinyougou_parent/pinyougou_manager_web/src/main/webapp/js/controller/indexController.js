app.controller("indexController", function ($scope, userService) {

    $scope.findUserName = function () {
        userService.findUserName().success(function (response) {

            $scope.username = JSON.parse(response);

        })
    }
});