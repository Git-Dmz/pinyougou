app.service("goodsService", function ($http) {

    this.add = function (entity) {
        return $http.post('../goods/add', entity);
    }
});