package org.xiaotuitui.framework.domain.repository;

import java.io.Serializable;
import java.util.List;

import org.xiaotuitui.framework.domain.model.PageObject;
import org.xiaotuitui.framework.domain.model.SqlParameters;

public interface JPABaseRep<T>{
	
	public T create(T t);
	
	public T update(T t);
	
	public void remove(T t);

	public void remove(Serializable id);
	
	public T find(Serializable id);
	
	public T getReference(Serializable id); 

	public List<T> query(String sql);

	public List<T> query(SqlParameters sqlParameters);
	
	public List<T> namedQuery(String name);
	
	public List<T> namedQuery(SqlParameters sqlParameters);

	public T queryFirstResult(String sql);
	
	public T queryFirstResult(SqlParameters sqlParameters);
	
	public T namedQueryFirstResult(String name);
	
	public T namedQueryFirstResult(SqlParameters sqlParameters);
	
	public T querySingleResult(String sql);
	
	public T querySingleResult(SqlParameters sqlParameters);
	
	public T namedQuerySingleResult(String name);
	
	public T namedQuerySingleResult(SqlParameters sqlParameters);

	public List<T> queryByPage(String sql, PageObject pageObject);
	
	public List<T> queryByPage(SqlParameters sqlParameters, PageObject pageObject);
	
	public List<T> namedQueryByPage(String name, PageObject pageObject);
	
	public List<T> namedQueryByPage(SqlParameters sqlParameters, PageObject pageObject);
	
	public long count(SqlParameters sqlParameters);
	
}