package henry.persistent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EMProvider
{
	private static final Map<String, EntityManagerFactory> factories = new ConcurrentHashMap<>();
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("DayPriceVolService");
    private static final EntityManagerFactory emf_test = Persistence.createEntityManagerFactory("DayPriceVolServiceTest");
    // it maps to the "stock" schema in the db. "DayPriceVolService" is the unit name in the persistence.xml file

    public static EntityManager getEM()
    {
        return emf.createEntityManager();
    }
    
    public static EntityManager getEMTest()
    {
        return emf_test.createEntityManager();
    }
    
    public static EntityManagerFactory getEMFactory(String unitName)
    {
    	EntityManagerFactory entityManagerFactory = factories.get(unitName);
		if (entityManagerFactory == null) {
    		return createEMFactory(unitName);
    	}
    	return entityManagerFactory;
    }
    
    private static synchronized  EntityManagerFactory createEMFactory(String unitName)
    {
    	EntityManagerFactory entityManagerFactory = factories.get(unitName);
		if (entityManagerFactory == null) {
			entityManagerFactory = Persistence.createEntityManagerFactory("DayPriceVolService");
			factories.put(unitName, entityManagerFactory);
		}
		return entityManagerFactory;
    }
}
