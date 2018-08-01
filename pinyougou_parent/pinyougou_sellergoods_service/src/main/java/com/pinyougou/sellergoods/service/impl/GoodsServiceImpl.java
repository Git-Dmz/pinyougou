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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import java.util.*;

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
        tbGoods.setIsDelete("0");
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
     * @return
     */
    @Override
    public Goods findOne(Long goodsId) {
        Goods goods = new Goods();
        TbGoods tbGoods = goodsMapper.selectByPrimaryKey(goodsId);
        Map map = new HashMap();
        String category1 = itemCatMapper.selectByPrimaryKey(tbGoods.getCategory1Id()).getName();
        String category2 = itemCatMapper.selectByPrimaryKey(tbGoods.getCategory2Id()).getName();
        String category3 = itemCatMapper.selectByPrimaryKey(tbGoods.getCategory3Id()).getName();
        map.put("category1", category1);
        map.put("category2", category2);
        map.put("category3", category3);
        goods.setCatMap(map);
        goods.setTbGoods(tbGoods);
        TbGoodsDesc tbGoodsDesc = tbGoodsDescMapper.selectByPrimaryKey(goodsId);
        goods.setTbGoodsDesc(tbGoodsDesc);
        TbItemExample example = new TbItemExample();
        example.createCriteria().andGoodsIdEqualTo(goodsId);
        List<TbItem> itemList = itemMapper.selectByExample(example);
        goods.setTbItemList(itemList);
        return goods;
    }

    /**
     * 查询全部Goods
     */
    @Override
    public List<Goods> findAllGoods() {
        List<Goods> goodsList = new ArrayList<>();
        List<TbGoods> tbGoods = goodsMapper.selectByExample(null);
        Map map = new HashMap();
        //节省栈内存
        Goods good = null;
        for (TbGoods tbGood : tbGoods) {
            //new对象需要写在循环内，不然引用的是同一个地址 会覆盖掉之前所有循环的数据
            good = new Goods();
            String category1 = itemCatMapper.selectByPrimaryKey(tbGood.getCategory1Id()).getName();
            String category2 = itemCatMapper.selectByPrimaryKey(tbGood.getCategory2Id()).getName();
            String category3 = itemCatMapper.selectByPrimaryKey(tbGood.getCategory3Id()).getName();
            map.put("category1", category1);
            map.put("category2", category2);
            map.put("category3", category3);
            good.setCatMap(map);
            good.setTbGoods(tbGood);

            TbGoodsDesc tbGoodsDesc = tbGoodsDescMapper.selectByPrimaryKey(tbGood.getId());
            good.setTbGoodsDesc(tbGoodsDesc);

            TbItemExample example = new TbItemExample();
            example.createCriteria().andGoodsIdEqualTo(tbGoodsDesc.getGoodsId());
            List<TbItem> itemList = itemMapper.selectByExample(example);
            good.setTbItemList(itemList);

            goodsList.add(good);
        }
        return goodsList;

        /**
         * 讲师生成所有静态文件的方式
         List<Goods> goodsList = new ArrayList<>();
         List<TbGoods> tbGoods = goodsMapper.selectByExample(null);
         for (TbGoods tbGood : tbGoods) {
         goodsList.add(findOne(tbGood.getId()));
         }
         goodsList.add(good);
         return goodsList;
         */


    }

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
        Page<TbGoods> page = (Page<TbGoods>) goodsMapper.selectByExample(null);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 修改
     */
    @Override
    public void update(TbGoods goods) {
        goodsMapper.updateByPrimaryKey(goods);
    }

    /**
     * 批量删除
     */
    @Override
    public void del(Long[] ids) {
        for (Long id : ids) {
            goodsMapper.deleteByPrimaryKey(id);
        }
    }

    @Override
    public PageResult search(TbGoods goods, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        TbGoodsExample example = new TbGoodsExample();
        TbGoodsExample.Criteria criteria = example.createCriteria();

        if (goods != null) {
            if (goods.getSellerId() != null && goods.getSellerId().length() > 0) {
                criteria.andSellerIdEqualTo(goods.getSellerId());
            }
            if (goods.getGoodsName() != null && goods.getGoodsName().length() > 0) {
                criteria.andGoodsNameLike("%" + goods.getGoodsName() + "%");
            }
            if (goods.getAuditStatus() != null && goods.getAuditStatus().length() > 0) {
                criteria.andAuditStatusEqualTo(goods.getAuditStatus());
            }
            if (goods.getIsMarketable() != null && goods.getIsMarketable().length() > 0) {
                criteria.andIsMarketableLike(goods.getIsMarketable());
            }
            if (goods.getCaption() != null && goods.getCaption().length() > 0) {
                criteria.andCaptionLike("%" + goods.getCaption() + "%");
            }
            if (goods.getSmallPic() != null && goods.getSmallPic().length() > 0) {
                criteria.andSmallPicLike("%" + goods.getSmallPic() + "%");
            }
            if (goods.getIsEnableSpec() != null && goods.getIsEnableSpec().length() > 0) {
                criteria.andIsEnableSpecLike("%" + goods.getIsEnableSpec() + "%");
            }
            if (goods.getIsDelete() != null && goods.getIsDelete().length() > 0) {
                criteria.andIsDeleteEqualTo(goods.getIsDelete());
            }

        }

        Page<TbGoods> page = (Page<TbGoods>) goodsMapper.selectByExample(example);
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

    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    @Qualifier("queue_market_solr_spu")
    private Destination queue_market_solr_spu;
    @Autowired
    @Qualifier("queue_offMarket_solr_spu")
    private Destination queue_offMarket_solr_spu;

    @Override
    public void updateMarketable(Long[] ids, String market) {
        //上下架状态：0=未上架，1=上架，2=下架

        for (Long id : ids) {
            if ("1".equals(market)) {
                jmsTemplate.send(queue_market_solr_spu, new MessageCreator() {
                    @Override
                    public Message createMessage(Session session) throws JMSException {
                        return session.createTextMessage(id.toString());
                    }
                });
            } else {
                jmsTemplate.send(queue_offMarket_solr_spu, new MessageCreator() {
                    @Override
                    public Message createMessage(Session session) throws JMSException {
                        return session.createTextMessage(id.toString());
                    }
                });
            }

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
