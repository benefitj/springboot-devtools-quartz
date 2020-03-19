package com.example.devtoolsquartz.core;

import com.hsrg.core.DateFmtter;
import com.hsrg.core.ReflectUtils;
import org.apache.commons.lang3.StringUtils;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;
import tk.mybatis.mapper.common.RowBoundsMapper;
import tk.mybatis.mapper.common.ids.SelectByIdsMapper;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.util.Sqls;

import javax.persistence.Transient;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Mapper
 *
 * @param <T>
 */
public interface BaseMapper<T> extends Mapper<T>, MySqlMapper<T>, SelectByIdsMapper<T>, RowBoundsMapper<T> {

  /**
   * 转换为 yyyy-MM-dd 格式数据
   *
   * @param date 日期
   * @return 返回格式化后的日期
   */
  @Transient
  static String fmtDate(Date date) {
    return DateFmtter.fmt(date, DateFmtter._yMd);
  }

  /**
   * 转换为 yyyy-MM-dd HH:mm:ss 格式数据
   *
   * @param date 日期
   * @return 返回格式化后的日期
   */
  @Transient
  static String fmt(Date date) {
    return DateFmtter.fmt(date);
  }

  @Transient
  static boolean isNotBlank(CharSequence cs) {
    return StringUtils.isNotBlank(cs);
  }

  /**
   * 获取实体类型
   */
  @Transient
  default Class<T> getEntityClass() {
    return EntityHolder.getEntityClass(getClass(), "T");
  }

  /**
   * Example
   */
  @Transient
  default Example example(Sqls sqls) {
    return new Example.Builder(getEntityClass())
        .where(sqls)
        .build();
  }

  @Transient
  default int selectCountByPK(Map<String, Object> values) {
    final Sqls sqls = Sqls.custom();
    Set<EntityColumn> pkColumns = EntityHelper.getPKColumns(getEntityClass());
    pkColumns.stream()
        .map(EntityColumn::getProperty)
        .filter(values::containsKey)
        .forEach(property -> sqls.andEqualTo(property, values.get(property)));
    return selectCountByExample(example(sqls));
  }

  final class EntityHolder {
    /**
     * 缓存实体类
     */
    private static final Map<Class<?>, Class> ENTITY_CACHE = new ConcurrentHashMap<>();

    private EntityHolder() {
    }

    /**
     * 获取实体类类型
     */
    public static <T> Class<T> getEntityClass(Class<?> clazz, String typeParamName) {
      Class<T> entityClass = (Class<T>) ENTITY_CACHE.get(clazz);
      if (entityClass != null) {
        return entityClass;
      }
      entityClass = ReflectUtils.getParameterizedTypeClass(clazz, typeParamName);
      if (entityClass == null) {
        throw new IllegalStateException("无法获取实体类型!");
      }
      ENTITY_CACHE.put(clazz, entityClass);
      return entityClass;
    }
  }


}
