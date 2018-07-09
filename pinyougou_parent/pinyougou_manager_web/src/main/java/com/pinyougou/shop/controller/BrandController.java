package com.pinyougou.shop.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.sellergoods.service.BrandService;
import entity.PageResult;
import entity.Result;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("brand")
public class BrandController {

    @Reference
    private BrandService service;

    @RequestMapping("findAll")
    public List<TbBrand> findAll() {
        return service.findAll();
    }

    @RequestMapping("findPage/{pageNum}/{pageSize}")
    public PageResult findPage(@PathVariable("pageNum") int pageNum, @PathVariable("pageSize") int pageSize) {
        return service.findPage(pageNum, pageSize);
    }

    @RequestMapping("search/{pageNum}/{pageSize}")
    public PageResult search(@PathVariable("pageNum") int pageNum, @PathVariable("pageSize") int pageSize,@RequestBody TbBrand brand) {
        return service.search(pageNum, pageSize , brand);
    }

    @RequestMapping("add")
    public Result add(@RequestBody TbBrand brand) {
        try {
            service.add(brand);
            return new Result(true, "添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "添加失败");
        }
    }

    @RequestMapping("findOne/{id}")
    public TbBrand findOne(@PathVariable("id") Long id){
       return service.findOne(id);
    }

    @RequestMapping("update")
    public Result update(@RequestBody TbBrand brand) {
        try {
            service.update(brand);
            return new Result(true,"修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"修改失败");
        }
    }
    @RequestMapping("del/{ids}")
    public Result del(@PathVariable Long[] ids) {
        try {
            service.del(ids);
            return new Result(true,"删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"删除失败");
        }
    }
}
