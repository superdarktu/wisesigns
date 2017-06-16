package com.signs.util;


import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 所有自定义Dao的顶级接口, 封装常用的增删查改操作,
 * 可以通过Mybatis Generator Maven 插件自动生成Dao,
 * 也可以手动编码,然后继承GenericDao 即可.
 * <p>
 * Model : 代表数据库中的表 映射的Java对象类型
 * PK :代表对象的主键类型
 */
public interface GenericDao<Model, Example, PK> {

    //插入对象
    int insert(Model model);

    //通过主键，更新对象
    int updateByPrimaryKey(Model model);

    //通过查询条件，更新对象
    int updateByExampleSelective(@Param("record") Model record, @Param("example") Example example);

    //通过主键，删除对象
    int deleteByPrimaryKey(PK id);

    //通过查询条件，删除对象
    int deleteByExample(Example example);

    //通过查询条件，统计总数
    int countByExample(Example example);

    //通过主键，查询对象
    Model selectByPrimaryKey(PK id);

    //通过查询条件，查询列表
    List<Model> selectByExample(Example example);

    //通过分页信息和查询条件，查询列表
    List<Model> selectByExampleAndPage(Page page, Example example);
}
