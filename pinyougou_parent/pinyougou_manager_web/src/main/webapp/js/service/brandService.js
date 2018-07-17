app.service("brandService", function ($http) {

    //显示品牌数据
    this.findBrandList = function () {
        return $http.get("../brand/findBrandList")
    };

    //分页显示
    this.findPage = function (pageNum, pageSize) {
        return $http.get("../brand/findPage/" + pageNum + "/" + pageSize);
    };

    //条件查询
    this.search = function (pageNum, pageSize, searchEntity) {
        return $http.post("../brand/search/" + pageNum + "/" + pageSize, searchEntity)
    };

    //添加品牌
    this.add = function (entity) {
        return $http.post("../brand/add", entity)
    };

    //修改品牌
    this.update = function (entity) {
        return $http.post("../brand/update", entity)
    };

    //在编辑窗口回显要修改的数据
    this.findOne = function (id) {
        return $http.get("../brand/findOne/" + id)
    };

    //删除品牌
    this.del = function (Ids) {
        return $http.get("../brand/del/" + Ids)
    };

    //查询所有
    this.findAll = function () {
        return $http.get("../brand/findAll")
    }
});