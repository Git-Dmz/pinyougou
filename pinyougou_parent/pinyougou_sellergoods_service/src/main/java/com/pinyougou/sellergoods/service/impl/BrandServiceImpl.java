package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.BrandMapper;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.sellergoods.service.BrandService;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class BrandServiceImpl implements BrandService {
    @Autowired
    private BrandMapper mapper;

    @Override
    public List<TbBrand> findAll() {
        return mapper.findAll();
    }

    @Override
    public PageResult findPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page page = (Page) mapper.findAll();
        return new PageResult(page.getTotal(), page.getResult());

    }

    @Override
    public void add(TbBrand brand) {
        mapper.add(brand);
    }

    @Override
    public TbBrand findOne(Long id) {
        return mapper.findOne(id);
    }

    @Override
    public void update(TbBrand brand) {
        mapper.update(brand);
    }

    @Override
    public void del(Long[] ids) {
        for (Long id : ids) {
            mapper.del(id);
        }
    }

    @Override
    public PageResult search(int pageNum, int pageSize, TbBrand brand) {
        PageHelper.startPage(pageNum, pageSize);
        Page search = (Page) mapper.search(brand);
        return new PageResult(search.getTotal(), search.getResult());
    }
}
