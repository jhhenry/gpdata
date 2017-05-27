package dailyRun;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import henry.commons.CalendarUtil;
import henry.commons.DateUtils;

public class EntryPoint
{
    private static final String PLEASE_INPUT = "please input your command and arguments. For example, daily";
    private static final String DAILY = "daily";
    private static final Logger logger = LogManager.getLogger(EntryPoint.class);

    public static void main(String[] args) throws Exception
    {
        // Commands:
        // 1. retrieve and persist today's stocks' quotes
        // 2. retrieve and persist today's stocks' price volume
        // 3. get historical stocks' quotes
        // 4. get historical price volume
        // 5. get report of the accumulative price volume
        // 6. get report of the statistics about a stock to research on its
        // price trend
        // 7. query

        /*
         * Command Format: <command> [<-argName> [argValue]*]*
         */
        /*
         * <Commands>         */
        /* 1. "daily" retrieve and persist today's stocks' quotes and price volume
    		syntax: -daily [start_date~[end_date]]
        // 2. "report" arguments: a. stock default to all b. phase
        // 3. "query"
        // stock: by stock
        // phase: phase list separated by comma
         */
        if (args == null || args.length == 0) {
            String input = readArgsFromConsole();
            args = input.split("\\s+");
        }
        CommandLine cmd = parseArgs(args);
        if (cmd == null) {
            logger.warn("do nothing because cli args is empty");
            return;
        }
        
        if (cmd.hasOption(DAILY)) {
        	List<Calendar> specifiedDates = selectDates(cmd);
            boolean hasPriceVol = cmd.hasOption("price_vol");
            boolean hasQuote = cmd.hasOption("quote");
            boolean getAll = !hasPriceVol && !hasQuote;

            if (hasPriceVol || getAll) {
                for (Calendar c : specifiedDates) {
                    getPriceVol(c, Stocks.getAllStocks());
                }
            }
            if (hasQuote || getAll) {
                for (Calendar c : specifiedDates) {
                    // GET quote
                    logger.info("get today's quotes");
                    Date date = c.getTime();
                    List<String> stocks = Stocks.getAllStocksNoPrefix();// Arrays.asList(new String[]{"002430"})
                    new DailyQuoteRetrieval(null).getAll(date, date, stocks);
                }
            }
        }
        if (cmd.hasOption("query")) {
            String[] params = cmd.getOptionValues("query");
            if (params.length >= 2) {
                if ("scale".equalsIgnoreCase(params[0])) {
                    // IStockFilter filter = new SimpleStaticInfoFilter(Stocks.getAllStocksNoPrefix(), Double.valueOf(params[1]));
                    // Collection<BasicDailyQuote> stocks = filter.filter();
                    // for (BasicDailyQuote s : stocks) {
                    // logger.info("stock: {}" + s.getStock());
                    // }
                }
            }
        }

    }

	private static CommandLine parseArgs(String[] args) throws ParseException {
		Options options = createOptions();
        CommandLineParser parser = new PosixParser();
        CommandLine cmd;
        try {
            cmd = parser.parse(options, args);
        }
        catch (ParseException e) {
            logger.error(e);
            throw e;
        }
		return cmd;
	}

	private static String readArgsFromConsole() throws IOException {
		Console con = System.console();
		String input;
		if (con != null) {
		    input = con.readLine(PLEASE_INPUT);
		}
		else {
		    System.out.println(PLEASE_INPUT);
		    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		    input = br.readLine();
		    br.close();
		}
		return input;
	}

    private static List<Calendar> selectDates(CommandLine cmd) throws java.text.ParseException
    {
    	List<Calendar> specifiedDates = new ArrayList<>();
    	if (cmd.getOptionValues(DAILY) != null) {
    		String[] dates = cmd.getOptionValues(DAILY);
    		ArrayList<String> result = new ArrayList<>();
    		for (String d : dates) {
    			if (d.endsWith("~")) {
    				String startDate = d.substring(0, d.length() - 1).trim();
    				result.add(startDate);
    				LocalDate addedDate = LocalDate.parse(d);
    				while (addedDate != null && addedDate.isBefore(LocalDate.now())) {
    					if (addedDate.getDayOfWeek().compareTo(DayOfWeek.FRIDAY) <= 0) {
    						Calendar c = Calendar.getInstance(Locale.CHINA);
    						c.setTime(DateUtils.asDate(addedDate));
    						specifiedDates.add(c);
    					}
    					addedDate = addedDate.plusDays(1);
    				}
    			}
    			else {
    				LocalDate addedDate = LocalDate.parse(d);
    				if (addedDate != null && addedDate.isBefore(LocalDate.now()) && addedDate.getDayOfWeek().compareTo(DayOfWeek.FRIDAY) <= 0) {
    					Calendar c = Calendar.getInstance(Locale.CHINA);
						c.setTime(DateUtils.asDate(addedDate));
    					specifiedDates.add(c);
    				}
    			}
    		}
    	}
    	else {
    		specifiedDates.add(CalendarUtil.getToday());
    	}
    	return specifiedDates;
    }

    private static void getPriceVol(Calendar price_vol_date, List<String> stocks)
    {
        // get price volumn
        logger.info("get today's price vol");
        long start = System.currentTimeMillis();
        PriceVolumeRetrieval r = new PriceVolumeRetrieval();
        r.capture(price_vol_date, price_vol_date, stocks);
        logger.info("It took {} seond to take all price vols:", (System.currentTimeMillis() - start));
    }

    @SuppressWarnings("static-access")
    private static Options createOptions()
    {
        // create Options object
        Options options = new Options();

        // add daily option
        options.addOption(OptionBuilder.hasOptionalArg().withValueSeparator(',').withDescription( "perform daily tasks of retrieving stocks info").create(DAILY));
        options.addOption(OptionBuilder.hasArgs().withValueSeparator(' ').withDescription("query stocks").create("query"));
        options.addOption(OptionBuilder.hasArgs().withValueSeparator(' ').withDescription("explicitly indicate that the price_vol is need. accept more than one date").create("price_vol"));
        options.addOption(OptionBuilder.withValueSeparator(' ').withDescription("explicitly indicate that the quote is need. accept more than one date").create("quote"));
        options.addOption(OptionBuilder.hasArgs().withValueSeparator(' ').withDescription("specify the date").create("date"));

        return options;
    }

}
