package henry.html.extractor.internal;

import java.util.HashSet;
import java.util.Set;

public class MissingRecords {
	// TODO once any exception happens during retrieval of the stock statistics, record the event in this instance and retry before the program finishes.
	private final Set<MisssingRecord> recs = new HashSet<>();
	
	
	private static class MisssingRecord
	{
		
	}
}
