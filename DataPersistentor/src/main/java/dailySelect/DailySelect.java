package dailySelect;

import henry.analyzer.PhaseReport;
import henry.analyzer.StockPhaseAnalyzer;
import henry.commons.CalendarUtil;
import henry.query.AndFilter;
import henry.query.HugeTurnOverChange;
import henry.query.IStockFilter;
import henry.query.MainFilter;
import henry.query.NRiseFilter;
import henry.query.OrFilter;
import henry.query.ScaleFilter;
import henry.query.TempAdjustFilter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

import dailyRun.Stocks;

public class DailySelect
{
    static final Logger logger = LogManager.getLogger();

    @Test
    public void run()
    {
        MainFilter mf = new MainFilter();
        List<String> stocks = Stocks.getAllStocksNoPrefix();
        // Arrays.asList(s);//
        Map<String, PhaseReport> allPhaseReports = new HashMap<>(stocks.size());
        for (String stock : stocks) {
            PhaseReport latest = StockPhaseAnalyzer.getLatestReport(stock, CalendarUtil.getToday().getTime(), 10);
            allPhaseReports.put(stock, latest);
        }

        // 小阳线
        int minDuration = 4;
        double tolerateDropPercentage = 0.01;// 1%
        double maxRiseGap = 0.09; // 6%
        IStockFilter rise = new NRiseFilter(minDuration, tolerateDropPercentage, maxRiseGap);
        doFilter(mf, allPhaseReports, rise, "小阳线");
        // 仙人指路
        doFilter(mf, allPhaseReports, new TempAdjustFilter(), "仙人指路");
        // 底部确认

        // 突然放量

        // 连续下跌
    }

    private void doFilter(MainFilter mf, Map<String, PhaseReport> allPhaseReports, IStockFilter rise, String reportTitle)
    {
        Map<String, PhaseReport> nRiseReport = mf.filter(allPhaseReports, rise);
        logger.info(reportTitle);
        for (String stock : nRiseReport.keySet()) {
            logger.info(stock);
        }
    }

    private IStockFilter makeFilter()
    {
        List<IStockFilter> fs = new ArrayList<>();
        IStockFilter rise = new NRiseFilter();
        fs.add(new ScaleFilter(500));
        fs.add(rise);
        IStockFilter f = new AndFilter(fs);

        OrFilter orF = new OrFilter();
        orF.addFilter(new HugeTurnOverChange(4));
        orF.addFilter(f);
        return orF;
    }
}
