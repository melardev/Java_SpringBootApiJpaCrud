package com.melardev.spring.apicrud.utils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;

public class JpaUtilEntityManager {
    private static final EntityManagerFactory emFactory;

    static {
        // Hibernate
        emFactory = Persistence.createEntityManagerFactory("H2HibernateAppPersistenceUnit");

        // EclipseLink
        // emFactory = Persistence.createEntityManagerFactory("H2EclipseLinkAppPersistenceUnit");
    }

    public static CriteriaBuilder getCriteriaBuilder() {
        return emFactory.getCriteriaBuilder();
    }

    public static EntityManager getEntityManager() {
        return emFactory.createEntityManager();
    }
}
