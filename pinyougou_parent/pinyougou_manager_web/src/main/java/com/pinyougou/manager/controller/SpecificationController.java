package com.pinyougou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbSpecification;
import com.pinyougou.sellergoods.service.SpecificationService;
import entity.PageResult;
import entity.Result;
import entityGroup.Specification;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("specification")
public class SpecificationController {

    @Reference
    private SpecificationService service;

    @RequestMapping("findSpecList")
    public List<Map> findSpecList() {
        return service.findSpecList();
    }


    @RequestMapping("findAll")
    public List<TbSpecification> findAll() {
        return service.findAll();
    }

    @RequestMapping("findPage/{pageNum}/{pageSize}")
    public PageResult findPage(@PathVariable("pageNum") int pageNum, @PathVariable("pageSize") int pageSize) {
        return service.findPage(pageNum, pageSize);
    }

    @RequestMapping("search/{pageNum}/{pageSize}")
    public PageResult search(@PathVariable("pageNum") int pageNum, @PathVariable("pageSize") int pageSize, @RequestBody TbSpecification specification) {
        return service.search(pageNum, pageSize, specification);
    }

    @RequestMapping("add")
    public Result add(@RequestBody Specification specification) {
        try {
            service.add(specification);
            return new Result(true, "添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "添加失败");
        }
    }

    @RequestMapping("findOne/{id}")
    public Specification findOne(@PathVariable("id") Long id) {
        return service.findOne(id);
    }

    @RequestMapping("update")
    public Result update(@RequestBody Specification specification) {
        try {
            service.update(specification);
            return new Result(true, "修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "修改失败");
        }
    }

    @RequestMapping("del/{ids}")
    public Result del(@PathVariable Long[] ids) {
        try {
            service.del(ids);
            return new Result(true, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "删除失败");
        }
    }
}
