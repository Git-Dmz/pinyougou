app.controller("goodsController", function ($scope, $controller, goodsService, uploadService, typeTemplateService, itemCatService) {

    $controller('baseController', {$scope: $scope});//继承

    $scope.status = ["未审核", "审核通过", "审核未通过", "关闭"];
    $scope.maketStatus = ["未上架", "上架", "下架"];
    $scope.entity = {tbGoodsDesc: {ItemImages: [], specificationItems: []}, tbGoods: {isEnableSpec: '0'}};

    // [{"attributeName":"主要材质","attributeValue":["蚕丝","羊毛"]
    $scope.updateSpecificationItems = function ($event, specName, optionName) {
        if ($event.target.checked) {
            var specObject = selectObjectFromList($scope.entity.tbGoodsDesc.specificationItems, specName);
            if (specObject != null) {
                specObject.attributeValue.push(optionName);
            } else {
                $scope.entity.tbGoodsDesc.specificationItems.push({
                    attributeName: specName,
                    attributeValue: [optionName]
                })
            }
        } else {
            var specObject = selectObjectFromList($scope.entity.tbGoodsDesc.specificationItems, specName);
            var index = $scope.entity.tbGoodsDesc.specificationItems.indexOf(optionName);
            specObject.attributeValue.splice(index, 1);
            if (specObject.attributeValue.length == 0) {
                var index = $scope.entity.tbGoodsDesc.specificationItems.indexOf(specObject);
                $scope.entity.tbGoodsDesc.specificationItems.splice(index, 1);
            }
        }
        createItemList();
    };

    function createItemList() {
        // [{"attributeName":"主要材质","attributeValue":["蚕丝","羊毛"]},
        // {"attributeName":"风格","attributeValue":["商务正装"]}]
        var specItems = $scope.entity.tbGoodsDesc.specificationItems;

        //初始化$scope.entity.tbItemList 格式是页面上显示的每一行的格式
        $scope.entity.tbItemList = [{spec: {}, price: 0, num: 9999, status: '1', isDefault: '0'}];

        //循环遍历specItems
        for (var i = 0; i < specItems.length; i++) {
            //调用addColumn函数 目的是给每一行数据添加值
            $scope.entity.tbItemList = addColumn($scope.entity.tbItemList, specItems[i].attributeName, specItems[i].attributeValue);
        }
    }

    function addColumn(itemList, attributeName, attributeValue) {
        //定义一个空的数组 用于存储数据
        var newList = [];
        for (var i = 0; i < itemList.length; i++) {
            for (var j = 0; j < attributeValue.length; j++) {
                //克隆当前itemList，不然引用的是同一个对象，改变数据的话会一起改变，使用深克隆可以避免
                var newItem = JSON.parse(JSON.stringify(itemList[i]));//深克隆：JSON.parse(JSON.stringify(克隆的数据))

                //[{spec:{},price:0,num:9999,status:'1',isDefault:'0'}]
                //{"attributeName":"风格","attributeValue":["商务正装"]}
                //{spec:{"attributeName":"风格","attributeValue":["商务正装"]},price:0,num:9999,status:'1',isDefault:'0'}
                //赋值动作把“第二条规格属性”插入到了“第一条spec对象中”
                newItem.spec[attributeName] = attributeValue[j];

                //添加到数组中
                newList.push(newItem);
            }
        }

        //newList数据格式为：
        //【参考】[{spec:{"attributeName":"风格","attributeValue":["商务正装"]},price:0,num:9999,status:'1',isDefault:'0'}]
        return newList;
    }

    function selectObjectFromList(specificationItems, specName) {
        //循环遍历判断 attributeName是否等同于specName
        for (var i = 0; i < specificationItems.length; i++) {
            if (specificationItems[i].attributeName == specName) {
                return specificationItems[i];
            }
        }
        return null;
    }

    //上传图片
    $scope.uploadFile = function () {
        uploadService.uploadFile().success(function (response) {
            if (response.success) {
                $scope.image.url = response.message;
            } else {
                alert(response.message);
            }
        })
    };

    //添加一项商品图片
    $scope.addImageToItemImages = function () {
        $scope.entity.tbGoodsDesc.ItemImages.push($scope.image);
    };
    //删除一项商品图片
    $scope.delItemImageToImages = function () {
        $scope.entity.tbGoodsDesc.ItemImages.splice($scope.image);
    };

    $scope.save = function () {
        $scope.entity.tbGoodsDesc.introduction = editor.html();
        goodsService.add($scope.entity).success(function (response) {
            if (response.success) {
                location.href = "goods.html";
            } else {
                alert(response.message);
            }
        })
    };

    $scope.findItemCategoty1List = function () {
        itemCatService.findByParentId(0).success(function (response) {
            $scope.itemCategoty1List = response;
        })
    };

    $scope.$watch("entity.tbGoods.category1Id", function (newValue, oldValue) {
        itemCatService.findByParentId(newValue).success(function (response) {
            $scope.itemCategoty2List = response;
            $scope.itemCategoty3List = [];
            $scope.entity.tbGoodsDesc.specificationItems = [];
        })
    });

    $scope.$watch("entity.tbGoods.category2Id", function (newValue, oldValue) {
        itemCatService.findByParentId(newValue).success(function (response) {
            $scope.itemCategoty3List = response;
            $scope.entity.tbGoodsDesc.specificationItems = [];
        })
    });

    $scope.$watch("entity.tbGoods.category3Id", function (newValue, oldValue) {
        itemCatService.findOne(newValue).success(function (response) {
            $scope.entity.tbGoods.typeTemplateId = response.typeId;

        })
    });

    $scope.$watch("entity.tbGoods.typeTemplateId", function (newValue, oldValue) {
        typeTemplateService.findOne(newValue).success(function (response) {
            //品牌列表
            $scope.brandList = JSON.parse(response.brandIds);
            //扩展属性
            $scope.entity.tbGoodsDesc.customAttributeItems = JSON.parse(response.customAttributeItems);
            //规格显示
            typeTemplateService.findSpecList(newValue).success(function (response) {
                $scope.specList = response;
            });
            //初始化规格项
            $scope.entity.tbGoodsDesc.specificationItems = [];
        })
    });


});