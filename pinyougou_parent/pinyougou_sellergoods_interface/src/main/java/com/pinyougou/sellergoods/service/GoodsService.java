package com.pinyougou.sellergoods.service;

import com.pinyougou.pojo.TbGoods;
import entityGroup.Goods;

public interface GoodsService {
    void add(Goods goods);

    TbGoods findOne(Long id);
}
