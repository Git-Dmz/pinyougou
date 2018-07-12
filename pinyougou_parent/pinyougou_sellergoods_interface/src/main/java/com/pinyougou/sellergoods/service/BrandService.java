package com.pinyougou.sellergoods.service;

import com.pinyougou.pojo.TbBrand;
import entity.PageResult;
import entity.Result;
import sun.jvm.hotspot.debugger.Page;

import java.util.List;
import java.util.Map;

public interface BrandService {
    List<TbBrand> findAll();

    PageResult findPage(int pageNum, int pageSize);

    void add(TbBrand brand);

    TbBrand findOne(Long id);

    void update(TbBrand brand);

    void del(Long[] ids);

    PageResult search(int pageNum, int pageSize, TbBrand brand);

    List<Map> findBrandList();

}
