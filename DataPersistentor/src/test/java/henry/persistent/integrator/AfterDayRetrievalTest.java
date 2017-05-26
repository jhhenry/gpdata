package henry.persistent.integrator;

import henry.commons.CalendarUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.testng.annotations.Test;

import dailyRun.PriceVolumeRetrieval;

public class AfterDayRetrievalTest
{

    @Test
    public void test() throws InterruptedException, ExecutionException
    {
        PriceVolumeRetrieval r = new PriceVolumeRetrieval();
        List<String> stocks = new ArrayList<>(10);
        stocks.add("sh600420");
        stocks.add("sh600421");
        stocks.add("sh600493");
        stocks.add("sh600503");
        stocks.add("sh600603");
        r.capture(CalendarUtil.getCalendar(2014, 10, 23), CalendarUtil.getCalendar(2014, 10, 23), stocks);

        // for (Future<Object> f: fs) {
        // f.get();
        // }
    }
}
