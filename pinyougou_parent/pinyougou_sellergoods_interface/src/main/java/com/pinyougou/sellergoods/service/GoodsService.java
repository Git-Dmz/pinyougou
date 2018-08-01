package com.pinyougou.sellergoods.service;

import com.pinyougou.pojo.TbGoods;
import entity.PageResult;
import entityGroup.Goods;

import java.util.List;

public interface GoodsService {
    /**
     * 返回全部列表
     * @return
     */
    public List<TbGoods> findAll();


    /**
     * 返回分页列表
     * @return
     */
    public PageResult findPage(int pageNum, int pageSize);


    /**
     * 增加
     */
    public void add(Goods goods);


    /**
     * 修改
     */
    public void update(TbGoods goods);


    /**
     * 根据ID获取实体
     * @param id
     * @return
     */
    public Goods findOne(Long id);


    /**
     * 批量删除
     * @param ids
     */
    public void del(Long[] ids);

    /**
     * 分页
     * @param pageNum 当前页 码
     * @param pageSize 每页记录数
     * @return
     */
    PageResult search(TbGoods goods, int pageNum, int pageSize);

    void updateAuditStatus(Long[] ids, String status);

    void updateMarketable(Long[] ids, String market);

    void updateIsDelete(Long[] ids, String del);

    List<Goods> findAllGoods();
}
