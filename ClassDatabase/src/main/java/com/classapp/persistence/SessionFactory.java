package com.classapp.persistence;

import java.io.Serializable;
import java.sql.Connection;
import java.util.Map;
import java.util.Set;

import javax.naming.NamingException;
import javax.naming.Reference;

import org.hibernate.Cache;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionBuilder;
import org.hibernate.StatelessSession;
import org.hibernate.StatelessSessionBuilder;
import org.hibernate.TypeHelper;
import org.hibernate.engine.spi.FilterDefinition;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.metadata.CollectionMetadata;
import org.hibernate.stat.Statistics;

public class SessionFactory implements org.hibernate.SessionFactory{

	@Override
	public Reference getReference() throws NamingException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SessionFactoryOptions getSessionFactoryOptions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SessionBuilder withOptions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Session openSession() throws HibernateException {
		
		return null;
	}

	@Override
	public Session getCurrentSession() throws HibernateException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StatelessSessionBuilder withStatelessOptions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StatelessSession openStatelessSession() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StatelessSession openStatelessSession(Connection connection) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClassMetadata getClassMetadata(Class entityClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClassMetadata getClassMetadata(String entityName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CollectionMetadata getCollectionMetadata(String roleName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, ClassMetadata> getAllClassMetadata() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map getAllCollectionMetadata() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Statistics getStatistics() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void close() throws HibernateException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isClosed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Cache getCache() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void evict(Class persistentClass) throws HibernateException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void evict(Class persistentClass, Serializable id) throws HibernateException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void evictEntity(String entityName) throws HibernateException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void evictEntity(String entityName, Serializable id) throws HibernateException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void evictCollection(String roleName) throws HibernateException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void evictCollection(String roleName, Serializable id) throws HibernateException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void evictQueries(String cacheRegion) throws HibernateException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void evictQueries() throws HibernateException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Set getDefinedFilterNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FilterDefinition getFilterDefinition(String filterName) throws HibernateException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean containsFetchProfileDefinition(String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public TypeHelper getTypeHelper() {
		// TODO Auto-generated method stub
		return null;
	}

}
