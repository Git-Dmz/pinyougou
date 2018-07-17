app.service("userService",function ($http) {
    this.findUserName=function () {
        return $http.get("../user/findUserName");
    }
});