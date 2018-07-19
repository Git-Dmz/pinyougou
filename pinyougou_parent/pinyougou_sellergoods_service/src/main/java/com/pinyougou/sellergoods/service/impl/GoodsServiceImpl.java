package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.*;
import com.pinyougou.pojo.*;
import com.pinyougou.sellergoods.service.GoodsService;
import entity.PageResult;
import entityGroup.Goods;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private TbGoodsMapper goodsMapper;
    @Autowired
    private TbGoodsDescMapper tbGoodsDescMapper;
    @Autowired
    private TbItemMapper itemMapper;
    @Autowired
    private TbItemCatMapper itemCatMapper;
    @Autowired
    private BrandMapper brandMapper;
    @Autowired
    private TbSellerMapper sellerMapper;

    /**
     * 增加
     */
    @Override
    public void add(Goods goods) {
        TbGoods tbGoods = goods.getTbGoods();

        tbGoods.setAuditStatus("0");
        tbGoods.setIsMarketable("0");
        goodsMapper.insert(tbGoods);

        TbGoodsDesc tbGoodsDesc = goods.getTbGoodsDesc();
        tbGoodsDesc.setGoodsId(tbGoods.getId());
        tbGoodsDescMapper.insert(tbGoodsDesc);

        //判断有没有启动规格
        if (("1").equals(tbGoods.getIsEnableSpec())) {
            List<TbItem> tbItems = goods.getTbItemList();
            for (TbItem tbItem : tbItems) {
                //商品标题
                //格式要求：【例如】小米6X 网络4G 机身内存16G
                String title = tbGoods.getGoodsName();
                Map<String, String> map = JSON.parseObject(tbItem.getSpec(), Map.class);
                for (String key : map.keySet()) {
                    String value = map.get(key);
                    title += " " + key + value;
                }
                //存入商品标题
                tbItem.setTitle(title);

                tbItem = createTbItem(tbItem, tbGoods, tbGoodsDesc);

                itemMapper.insert(tbItem);
            }
        } else {
            TbItem tbItem = new TbItem();
            tbItem.setTitle(tbGoods.getGoodsName());
            tbItem = createTbItem(tbItem, tbGoods, tbGoodsDesc);
            tbItem.setPrice(tbGoods.getPrice());
            tbItem.setIsDefault("1");
            tbItem.setNum(9999);
            tbItem.setSpec("{}");
            itemMapper.insert(tbItem);
        }
    }

    private TbItem createTbItem(TbItem tbItem, TbGoods tbGoods, TbGoodsDesc tbGoodsDesc) {
        //副标题
        String caption = tbGoods.getCaption();
        tbItem.setSellPoint(caption);

        //图片 默认使用第一张
        String itemImages = tbGoodsDesc.getItemImages();
        List<Map> maps = JSON.parseArray(itemImages, Map.class);
        if (maps != null && maps.size() > 0) {
            tbItem.setImage(maps.get(0).get("url").toString());
        }

        //分类ID 取的是第三级的分类ID
        Long category3Id = tbGoods.getCategory3Id();
        tbItem.setCategoryid(category3Id);

        //创建时间
        tbItem.setCreateTime(new Date());
        //更新时间
        tbItem.setUpdateTime(new Date());

        //goodsId
        tbItem.setGoodsId(tbGoods.getId());

        //sellerId
        tbItem.setSellerId(tbGoods.getSellerId());

        //分类名称
        TbItemCat tbItemCat = itemCatMapper.selectByPrimaryKey(tbGoods.getCategory3Id());
        tbItem.setCategory(tbItemCat.getName());

        //品牌名称
        TbBrand brand = brandMapper.findOne(tbGoods.getBrandId());
        tbItem.setBrand(brand.getName());

        //商家名称
        TbSeller seller = sellerMapper.selectByPrimaryKey(tbGoods.getSellerId());
        tbItem.setSeller(seller.getNickName());

        return tbItem;
    }

    /**
     * 根据ID获取实体
     *
     * @param id
     * @return
     */
    @Override
    public TbGoods findOne(Long id) {
        return goodsMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询全部
     */
    @Override
    public List<TbGoods> findAll() {
        return goodsMapper.selectByExample(null);
    }

    /**
     * 按分页查询
     */
    @Override
    public PageResult findPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<TbGoods> page=   (Page<TbGoods>) goodsMapper.selectByExample(null);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 修改
     */
    @Override
    public void update(TbGoods goods){
        goodsMapper.updateByPrimaryKey(goods);
    }

    /**
     * 批量删除
     */
    @Override
    public void del(Long[] ids) {
        for(Long id:ids){
            goodsMapper.deleteByPrimaryKey(id);
        }
    }

    @Override
    public PageResult search(TbGoods goods, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        TbGoodsExample example=new TbGoodsExample();
        TbGoodsExample.Criteria criteria = example.createCriteria();

        if(goods!=null){
            if(goods.getSellerId()!=null && goods.getSellerId().length()>0){
                criteria.andSellerIdEqualTo(goods.getSellerId());
            }
            if(goods.getGoodsName()!=null && goods.getGoodsName().length()>0){
                criteria.andGoodsNameLike("%"+goods.getGoodsName()+"%");
            }
            if(goods.getAuditStatus()!=null && goods.getAuditStatus().length()>0){
                criteria.andAuditStatusLike("%"+goods.getAuditStatus()+"%");
            }
            if(goods.getIsMarketable()!=null && goods.getIsMarketable().length()>0){
                criteria.andIsMarketableLike(goods.getIsMarketable());
            }
            if(goods.getCaption()!=null && goods.getCaption().length()>0){
                criteria.andCaptionLike("%"+goods.getCaption()+"%");
            }
            if(goods.getSmallPic()!=null && goods.getSmallPic().length()>0){
                criteria.andSmallPicLike("%"+goods.getSmallPic()+"%");
            }
            if(goods.getIsEnableSpec()!=null && goods.getIsEnableSpec().length()>0){
                criteria.andIsEnableSpecLike("%"+goods.getIsEnableSpec()+"%");
            }
            if(goods.getIsDelete()!=null && goods.getIsDelete().length()>0){
                criteria.andIsDeleteLike("%"+goods.getIsDelete()+"%");
            }

        }

        Page<TbGoods> page= (Page<TbGoods>)goodsMapper.selectByExample(example);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public void updateAuditStatus(Long[] ids, String status) {
        //审核状态：0=未审核，1=审核通过，2=审核未通过，3=关闭
        for (Long id : ids) {
            TbGoods tbGoods = goodsMapper.selectByPrimaryKey(id);
            tbGoods.setAuditStatus(status);
            goodsMapper.updateByPrimaryKey(tbGoods);
        }
    }

    @Override
    public void updateMarketable(Long[] ids, String market) {
        //上下架状态：0=未上架，1=上架，2=下架
        for (Long id : ids) {
            TbGoods tbGoods = goodsMapper.selectByPrimaryKey(id);
            tbGoods.setIsMarketable(market);
            goodsMapper.updateByPrimaryKey(tbGoods);
        }
    }

    @Override
    public void updateIsDelete(Long[] ids, String del) {
        //删除状态：0=未删除，1=删除
        for (Long id : ids) {
            TbGoods tbGoods = goodsMapper.selectByPrimaryKey(id);
            tbGoods.setIsDelete(del);
            goodsMapper.updateByPrimaryKey(tbGoods);
        }
    }

}
