package com.benefitj.devtoolsquartz.service;

import com.benefitj.devtoolsquartz.core.BaseMapper;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.entity.EntityTable;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.util.Sqls;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;

/**
 * base service
 *
 * @param <T>
 * @param <M>
 */
public abstract class BaseService<T, M extends BaseMapper<T>> {

  private static final Map<Class<?>, EntityColumn> PK_CACHE = new WeakHashMap<>();
  private static final Function<Class<?>, EntityColumn> PK_FUNC = clazz -> {
    EntityTable et = EntityHelper.getEntityTable(clazz);
    Set<EntityColumn> set = et.getEntityClassPKColumns();
    if (set.size() != 1) {
      throw new IllegalStateException("无法匹配主键");
    }
    return set.iterator().next();
  };

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  /**
   * Mybatis SQL operation
   */
  private SqlSessionTemplate template;


  public BaseService() {
  }

  /**
   * mapper
   */
  protected abstract M getMapper();

  protected SqlSessionTemplate getTemplate() {
    return template;
  }

  @Autowired
  protected void setTemplate(SqlSessionTemplate template) {
    this.template = template;
  }

  /**
   * 默认的实体类
   */
  protected Class<T> getEntityClass() {
    return getMapper().getEntityClass();
  }

  /**
   * 保存记录
   *
   * @param record 记录
   * @return 返回保存的条数
   */
  @Transactional(rollbackFor = Throwable.class)
  protected int insert(T record) {
    return getMapper().insert(record);
  }

  /**
   * 通过主键更新
   *
   * @param record 记录
   * @return 更新的条数
   */
  @Transactional(rollbackFor = Throwable.class)
  protected int updateByPK(T record) {
    return getMapper().updateByPrimaryKey(record);
  }

  /**
   * 通过主键更新，null值不更新
   *
   * @param record 记录
   * @return 更新的条数
   */
  @Transactional(rollbackFor = Throwable.class)
  protected int updateByPKSelective(T record) {
    return getMapper().updateByPrimaryKeySelective(record);
  }

  /**
   * 通过主键查询
   *
   * @param pk 主键
   * @return 返回查询到的记录
   */
  protected T getByPK(Object pk) {
    return pk != null ? getMapper().selectByPrimaryKey(pk) : null;
  }

  /**
   * 通过条件查询一条数据
   *
   * @param condition 条件
   * @return 返回查询到的数据
   */
  protected T getOne(@Nonnull T condition) {
    return getMapper().selectOne(condition);
  }

  /**
   * 通过条件查询全部数据
   *
   * @param condition 条件
   * @return 返回查询到的全部数据
   */
  protected List<T> getAll(@Nullable T condition) {
    return condition != null ? getMapper().select(condition) : getMapper().selectAll();
  }

  /**
   * 查询列表
   *
   * @param condition  条件
   * @param startTime  开始时间
   * @param endTime    结束时间
   * @param multiLevel 是否为多层级
   * @return 返回查询的列表
   */
  public List<T> getList(T condition, Date startTime, Date endTime, boolean multiLevel) {
    throw new UnsupportedOperationException("还未实现，请实现此方法!");
  }


  /**
   * 统计条数
   *
   * @param condition 条件
   * @return 返回统计的条数
   */
  public int count(T condition) {
    return getMapper().selectCount(condition);
  }

  /**
   * 通过主键统计条数
   *
   * @param pkValues 主键值(属性, 值)
   * @return 返回统计的条数
   */
  public int countByPK(Map<String, Object> pkValues) {
    return getMapper().selectCountByPK(pkValues);
  }

  /**
   * 通过主键统计条数
   *
   * @param pk 主键
   * @return 返回统计的条数
   */
  public int countByPK(Object pk) {
    EntityColumn pkColumn = PK_CACHE.computeIfAbsent(getEntityClass(), PK_FUNC);
    Sqls sqls = Sqls.custom();
    sqls.andEqualTo(pkColumn.getColumn(), pk);
    final M mapper = getMapper();
    return mapper.selectCountByExample(mapper.example(sqls));
  }

  /**
   * 删除记录
   *
   * @param record 记录
   * @return 返回删除的条数
   */
  @Transactional(rollbackFor = Throwable.class)
  protected int delete(T record) {
    return getMapper().delete(record);
  }

  /**
   * 通过主键删除记录
   *
   * @param pk 主键
   * @return 返回删除的条数
   */
  @Transactional(rollbackFor = Throwable.class)
  protected int deleteByPK(String pk) {
    return getMapper().deleteByPrimaryKey(pk);
  }

}
