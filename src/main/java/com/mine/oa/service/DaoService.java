package com.mine.oa.service;

import com.github.pagehelper.PageInfo;
import com.mine.oa.dto.PageQueryDto;
import org.springframework.stereotype.Service;

import java.util.List;

/***
 *
 * 〈一句话功能简述〉<br> 
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public interface DaoService<T> {

    T get(Object key);

    int save(T entity);

    int delete(Object key);

    int updateAll(T entity);

    int updateNotNull(T entity);

    List<T> selectByExample(Object example);

    PageInfo<T> selectByPage(Object example, PageQueryDto page);

}
