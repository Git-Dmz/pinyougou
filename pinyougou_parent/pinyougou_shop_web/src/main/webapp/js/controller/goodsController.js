app.controller("goodsController", function ($scope, goodsService, uploadService, typeTemplateService, itemCatService) {

    $scope.entity = {tbGoodsDesc: {ItemImages: [], specificationItems: []}, tbGoods: {isEnableSpec: '0'}};

    // [{"attributeName":"网络制式","attributeValue":["移动3G","移动4G"]}]
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
    };

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
    $scope.delItemImageToImage = function () {
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
        })
    });

    $scope.$watch("entity.tbGoods.category2Id", function (newValue, oldValue) {
        itemCatService.findByParentId(newValue).success(function (response) {
            $scope.itemCategoty3List = response;
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
            typeTemplateService.findBySpecId(newValue).success(function (response) {
                $scope.specList = response;
            })
        })
    });
});