package dailyRun;

public class AfterDayRetrieval
{
    // private static final Logger logger =
    // LogManager.getLogger("AfterDayRetrieval");

    public static void main(String[] args) throws Exception
    {
        String[] get = new String[] { "daily", "-price_vol", "2014-10-14" };
        EntryPoint.main(get);
    }

    // public void checkMissingStocks(Calendar date)
    // {
    // EntityManagerFactory emf =
    // Persistence.createEntityManagerFactory("DayPriceVolService");
    // EntityManager em = emf.createEntityManager();
    // DayPriceVolService dayPriceVolService = new DayPriceVolService(em);
    // stocks = dayPriceVolService.getMissingStocks(date.getTime());
    // //dayPriceVolService.deleteByStock(stocks);
    // em.close();
    // }

}
