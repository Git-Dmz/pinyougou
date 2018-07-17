app.service("specificationService", function ($http) {

    //显示规格数据
    this.findSpecList = function () {
        return $http.get("../specification/findSpecList")
    };

    //分页显示
    this.findPage = function (pageNum, pageSize) {
        return $http.get("../specification/findPage/" + pageNum + "/" + pageSize);
    };

    //条件查询
    this.search = function (pageNum, pageSize, searchEntity) {
        return $http.post("../specification/search/" + pageNum + "/" + pageSize, searchEntity)
    };

    //添加品牌
    this.add = function (entity) {
        return $http.post("../specification/add", entity)
    };

    //修改品牌
    this.update = function (entity) {
        return $http.post("../specification/update", entity)
    };

    //在编辑窗口回显要修改的数据
    this.findOne = function (id) {
        return $http.get("../specification/findOne/" + id)
    };

    //删除品牌
    this.del = function (Ids) {
        return $http.get("../specification/del/" + Ids)
    };

    //查询所有
    this.findAll = function () {
        return $http.get("../specification/findAll")
    }
});