package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.mapper.TbGoodsDescMapper;
import com.pinyougou.mapper.TbGoodsMapper;
import com.pinyougou.pojo.TbGoods;
import com.pinyougou.pojo.TbGoodsDesc;
import com.pinyougou.sellergoods.service.GoodsService;
import entityGroup.Goods;
import org.springframework.beans.factory.annotation.Autowired;
@Service
public class GoodsServiceImpl implements GoodsService{

    @Autowired
    private TbGoodsMapper tbGoodsMapper;
    @Autowired
    private TbGoodsDescMapper tbGoodsDescMapper;

    @Override
    public void add(Goods goods) {
        TbGoods tbGoods = goods.getTbGoods();

        tbGoods.setAuditStatus("0");
        tbGoodsMapper.insert(tbGoods);

        TbGoodsDesc tbGoodsDesc = goods.getTbGoodsDesc();
        tbGoodsDesc.setGoodsId(tbGoods.getId());
        tbGoodsDescMapper.insert(tbGoodsDesc);
    }

    @Override
    public TbGoods findOne(Long id) {
        return tbGoodsMapper.selectByPrimaryKey(id);
    }
}
