package henry.dailyUpdate;

public class Main
{

	public static void main(String[] args)
	{
		/*
		 * Functional Requirements:
		 * 	Overview	At the end of each dealing day, the following thing need be done to insert or update data in the database
		 *  Details:
		 *  1.  Update daily quote, and price vol for each stock
		 *  2.	If the dividend happens for a stock, update all its historical record.
		 *  3.  Detect the new stocks
		 *  4.  At the end of every week, update the weekly quote.
		 *  
		 *  Interface Requirements:
		 *  1.	There are dedicated classes(entry point) for each of the functional req. above.
		 *  2. 	Each task is abstracted as a Command. The semantics of the commands cannot be changed.
		 *  
		 *  Work Flow
		 *   for (each stock in stocks) {
		 *  	1	Update historical quotes if necessary: 
		 *  		Get the daily quote info from sohu to see if there is today a dividend for it .
		 *  		1a If yes, update the historical info: daily quote and weekly quote.
		 *  	2	Update the daily and weekly quote for it
		 *  	3	Insert today's price-vol data.
		 *   }
		 *   
		 *   Exception Reporting Procedure:
		 *   1. For any IOException and DBException that cause failures of retrieval and update, report the exception to 
		 *   	a dedicated table in the database
		 *   
		 *   Exception Recovering Procedure:
		 *   1. For any recoverable exception, the system should record enough info, in memory or db,  to redo the action to 
		 *   overcome the accidental failure.
		 *   
		 *  Engineering Requirements:
		 *  1. The class involving the procedure in the work flow should use a Logger to record the detailed steps.
		 *  2. Each Exception should be logged in file.
		 *  3. Exceptions should be recorded in both a regular log file and a error-log file.
		 */

	}

}
