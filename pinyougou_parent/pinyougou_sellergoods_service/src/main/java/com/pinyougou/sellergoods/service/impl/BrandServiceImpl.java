package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.TbBrandMapper;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.pojo.TbBrandExample;
import com.pinyougou.sellergoods.service.BrandService;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

@Service
public class BrandServiceImpl implements BrandService {
    @Autowired
    private TbBrandMapper brandMapper;

    @Override
    public List<TbBrand> findAll() {

        return brandMapper.selectByExample(null);
    }

    @Override
    public PageResult findPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page page = (Page) brandMapper.selectByExample(null);
        return new PageResult(page.getTotal(), page.getResult());

    }

    @Override
    public void add(TbBrand brand) {
        brandMapper.insert(brand);
    }

    @Override
    public TbBrand findOne(Long id) {
        return brandMapper.selectByPrimaryKey(id);
    }

    @Override
    public void update(TbBrand brand) {
        brandMapper.updateByPrimaryKey(brand);
    }

    @Override
    public void del(Long[] ids) {
        for (Long id : ids) {
            brandMapper.deleteByPrimaryKey(id);
        }
    }

    @Override
    public PageResult search(int pageNum, int pageSize, TbBrand brand) {
        PageHelper.startPage(pageNum, pageSize);
        TbBrandExample example = new TbBrandExample();
        TbBrandExample.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotEmpty(brand.getName())) {
            criteria.andNameLike("%" + brand.getName() + "%");
        }
        if (StringUtils.isNotEmpty(brand.getFirstChar())) {
            criteria.andFirstCharEqualTo(brand.getFirstChar());
        }
        Page search = (Page) brandMapper.selectByExample(example);
        return new PageResult(search.getTotal(), search.getResult());
    }

    @Override
    public List<Map> findBrandList() {
        return brandMapper.findBrandList();
    }
}
