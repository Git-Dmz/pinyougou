app.service("goodsService", function ($http) {

    this.add = function (entity) {
        return $http.post('../goods/add', entity);
    }

    //查询实体
    this.findOne=function(id){
        return $http.get('../goods/findOne/'+id);
    }

});