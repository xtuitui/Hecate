package org.xiaotuitui.framework.infrastructure.persistence;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.xiaotuitui.framework.domain.model.PageObject;
import org.xiaotuitui.framework.domain.model.SqlParameters;
import org.xiaotuitui.framework.domain.repository.JPABaseRep;
import org.xiaotuitui.framework.exception.SysException;
import org.xiaotuitui.framework.util.beanutil.ReflectUtil;

public abstract class JPABaseRepImpl<T> implements JPABaseRep<T>{

	private Class<T> persistentClass;
	
	protected abstract EntityManager getEntityManager();
	
	public JPABaseRepImpl() {
		this.persistentClass = ReflectUtil.getGenericTypeArgument(this.getClass());
	}

	public T create(T t) {
		getEntityManager().persist(t);
		return t;
	}
	
	public T update(T t) {
		return getEntityManager().merge(t);
	}
	
	public void remove(T t) {
		getEntityManager().remove(t);
	}

	public void remove(Serializable id) {
		this.remove(getReference(id));
	}
	
	public T find(Serializable id) {
		return getEntityManager().find(persistentClass, id);
	}
	
	public T getReference(Serializable id) {
		return getEntityManager().getReference(persistentClass, id);
	}

	@SuppressWarnings("unchecked")
	public List<T> query(String sql) {
		return getEntityManager().createQuery(sql).getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<T> query(SqlParameters sqlParameters) {
		return createQuery(getEntityManager(), sqlParameters).getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<T> namedQuery(String name){
		return getEntityManager().createNamedQuery(name).getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<T> namedQuery(SqlParameters sqlParameters) {
		return createNamedQuery(getEntityManager(), sqlParameters).getResultList();
	}
	
	public T queryFirstResult(String sql) {
		return getFirst(query(sql));
	}

	public T queryFirstResult(SqlParameters sqlParameters) {
		return getFirst(query(sqlParameters));
	}
	
	public T namedQueryFirstResult(String name) {
		return getFirst(namedQuery(name));
	}

	public T namedQueryFirstResult(SqlParameters sqlParameters) {
		return getFirst(namedQuery(sqlParameters));
	}
	
	private T getFirst(List<T> resultList) {
		if(resultList==null||resultList.size()==0){
			return null;
		}else{
			return resultList.get(0);
		}
	}
	
	@SuppressWarnings("unchecked")
	public T querySingleResult(String sql) {
		return (T) getEntityManager().createQuery(sql).getSingleResult();
	}
	
	@SuppressWarnings("unchecked")
	public T querySingleResult(SqlParameters sqlParameters) {
		return (T) createQuery(getEntityManager(), sqlParameters).getSingleResult();
	}
	
	@SuppressWarnings("unchecked")
	public T namedQuerySingleResult(String name) {
		return (T) getEntityManager().createNamedQuery(name).getSingleResult();
	}
	
	@SuppressWarnings("unchecked")
	public T namedQuerySingleResult(SqlParameters sqlParameters) {
		return (T) createNamedQuery(getEntityManager(), sqlParameters).getSingleResult();
	}
	
	public List<T> queryByPage(String sql, PageObject pageObject) {
		executeQueryTotalRecord(sql, pageObject);
		return executeQueryPageResult(getEntityManager().createQuery(sql), pageObject);
	}
	
	public List<T> queryByPage(SqlParameters sqlParameters, PageObject pageObject) {
		executeQueryTotalRecord(sqlParameters, pageObject);
		return executeQueryPageResult(createQuery(getEntityManager(), sqlParameters), pageObject);
	}
	
	/**
	 * Can not get the sql, so can not set the page object, need to query the totalRecord manually
	 */
	public List<T> namedQueryByPage(String name, PageObject pageObject) {
		return executeQueryPageResult(getEntityManager().createNamedQuery(name), pageObject);
	}
	
	public List<T> namedQueryByPage(SqlParameters sqlParameters, PageObject pageObject) {
		executeQueryTotalRecord(sqlParameters, pageObject);
		return executeQueryPageResult(createNamedQuery(getEntityManager(), sqlParameters), pageObject);
	}
	
	private void executeQueryTotalRecord(String sql, PageObject pageObject){
		SqlParameters sqlParameters = new SqlParameters(sql, null);
		executeQueryTotalRecord(sqlParameters, pageObject);
	}
	
	private void executeQueryTotalRecord(SqlParameters sqlParameters, PageObject pageObject){
		Long totalRecord = this.count(sqlParameters);
		pageObject.setTotalRecord(totalRecord);
	}

	@SuppressWarnings("unchecked")
	private List<T> executeQueryPageResult(Query query, PageObject pageObject){
		checkPageObject(pageObject);
		int currentPage = pageObject.getCurrentPage().intValue();
		int pageSize = PageObject.getPageSize();
		return query.setFirstResult((currentPage - 1) * pageSize).setMaxResults(pageSize).getResultList();
	}
	
	private void checkPageObject(PageObject pageObject) {
		if(PageObject.getPageSize()==null){
			throw new SysException("Please provide page size !");
		}
		if(PageObject.getPageBarSize()==null){
			throw new SysException("Please provide page bar size !");
		}
		if(pageObject.getCurrentPage()==null){
			throw new SysException("Please provide current page !");
		}
	}

	public long count(SqlParameters sqlParameters) {
		SqlParameters countSqlParameters = new SqlParameters();
		StringBuilder countSqlStringBuilder = buildCountSql(sqlParameters.getSqlStringBuilder());
		countSqlParameters.setSqlStringBuilder(countSqlStringBuilder);
		countSqlParameters.setParameters(sqlParameters.getParameters());
		return (Long) createQuery(getEntityManager(), countSqlParameters).getSingleResult();
	}
	
	private Query createQuery(EntityManager entityManager, SqlParameters sqlParameters) {
		Query query = entityManager.createQuery(sqlParameters.getSqlStringBuilder().toString());
		return setQueryParameter(query, sqlParameters.getParameters());
	}
	
	private Query createNamedQuery(EntityManager entityManager, SqlParameters sqlParameters){
		Query query = entityManager.createNamedQuery(sqlParameters.getSqlStringBuilder().toString());
		return setQueryParameter(query, sqlParameters.getParameters());
	}
	
	private Query setQueryParameter(Query query, Map<String, Object> parameters){
		if(parameters==null){
			return query;
		}
		Set<String> keySet = parameters.keySet();
		for(String key:keySet){
			query.setParameter(key, parameters.get(key));
		}
		return query;
	}
	
	private StringBuilder buildCountSql(StringBuilder sql) {
		int beginIndexFrom = sql.indexOf("from");
		String sqlFromEx = sql.substring(0, beginIndexFrom).replaceAll("select", "").trim();
		if(StringUtils.isBlank(sqlFromEx)){
			sqlFromEx = "*";
		}
		String sqlWhereRear = "";
		int beginIndexWhere = sql.indexOf("where");
		if (beginIndexWhere != -1) {
			sqlWhereRear = sql.substring(beginIndexWhere).replaceAll("1=1 and", "").replaceAll("1=1", "").trim();
			int beginIndexOrderBy = sqlWhereRear.indexOf("order by");
			if (beginIndexOrderBy != -1){
				sqlWhereRear = sqlWhereRear.substring(0, beginIndexOrderBy).trim();
			}
			if (sqlWhereRear.length() == "where".length()){
				sqlWhereRear = "";
			}
		}
		String sqlFromWhere = "";
		if (beginIndexWhere != -1){
			sqlFromWhere = sql.substring(beginIndexFrom, beginIndexWhere);
		}else{
			sqlFromWhere = sql.substring(beginIndexFrom);
		}
		return new StringBuilder("select count(" + sqlFromEx + ") " + sqlFromWhere + sqlWhereRear);
	}

}