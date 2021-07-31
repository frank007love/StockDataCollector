package org.tonylin.stock.dao;

import java.io.File;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.SessionFactory;
import net.sf.hibernate.Transaction;
import net.sf.hibernate.cfg.Configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HibernateUtil {
	static private Logger logger = LoggerFactory.getLogger(HibernateUtil.class);
	
    private static final SessionFactory mSessionFactory;
    private static final ThreadLocal<Session> mSession = new ThreadLocal<Session>();
	
    static {
        try {
            Configuration config = new Configuration().configure(new File("config/hibernate.cfg.xml"));

            mSessionFactory = config.buildSessionFactory();
        } catch (Throwable ex) {
        	logger.error("Init sessionFacotry failed.", ex);
        	throw new ExceptionInInitializerError(ex);
        }
    }
	
	public static void rollbackTransaction(Transaction aTransaction){
		if( aTransaction != null ){
			try {
				aTransaction.rollback();
			} catch (HibernateException e) {
				logger.error("Rollback transaction failed.", e);
			}
		}
	}
	
	public static void closeSession(Session aSeesion){
		if( aSeesion != null ){
			synchronized (aSeesion) {
				try {
					aSeesion.close();
				} catch (HibernateException e) {
					logger.error("Close seesion failed.", e);
				}
			}
		}
	}
    public static Session currentSession() throws HibernateException {
        Session s = (Session) mSession.get();
        if (s == null) {
            s = mSessionFactory.openSession();
            mSession.set(s);
        }
        return s;
    }

    public static void closeSession() {
        Session s = (Session) mSession.get();
		try {
			if (s != null)
				s.close();
		} catch (HibernateException e) {
			logger.error("Close session failed.", e);
		}
        mSession.set(null);
    }
}
