package org.xiaotuitui.hecate.infrastructure.persistence;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.xiaotuitui.framework.infrastructure.persistence.JPABaseRepImpl;
import org.xiaotuitui.hecate.domain.model.User;
import org.xiaotuitui.hecate.domain.repository.UserRep;

@Repository
public class UserRepImpl extends JPABaseRepImpl<User> implements UserRep{

	@PersistenceContext
	private EntityManager entityManager;
	
	protected EntityManager getEntityManager() {
		return entityManager;
	}

}