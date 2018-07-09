package com.pinyougou.mapper;

import com.github.pagehelper.Page;
import com.pinyougou.pojo.TbBrand;
import entity.Result;

import java.util.List;

public interface BrandMapper {

    List<TbBrand> findAll();

    void add(TbBrand brand);

    TbBrand findOne(Long id);

    void update(TbBrand brand);

    void del(Long id);

    List<TbBrand> search(TbBrand brand);
}
