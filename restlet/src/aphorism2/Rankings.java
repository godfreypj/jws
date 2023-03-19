package aphorism2;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Rankings {
	private static CopyOnWriteArrayList<Ranking> bandNames;
	private static AtomicInteger initialRanking;

	static {
		String[] aphorisms = { "Grateful Dead.",
				"Phish.",
				"Little Feat.",
				"Billy Joel.",
				"moe." };
		bandNames = new CopyOnWriteArrayList<Ranking>();
		initialRanking = new AtomicInteger();
		for (String str : aphorisms)
			addInitial(str);
	}

	public static String toPlain() {
		String retval = "";
		for (Ranking bandName : bandNames)
			retval += bandName.toString() + "\n";
		return retval;
	}

	public static CopyOnWriteArrayList<Ranking> getList() {
		return bandNames;
	}

	// Support GET one operation.
	public static Ranking find(int ranking) {
		Ranking band = null;
		for (Ranking b : bandNames) {
			if (b.getRanking() == ranking) {
				band = b;
				break;
			}
		}
		return band;
	}

	// Support Initial Add operation.
	public static void addInitial(String band) {
		int localRanking = initialRanking.incrementAndGet();
		Ranking localBand = new Ranking();
		localBand.setBandName(band);
		localBand.setRanking(localRanking);
		bandNames.add(localBand);
	}

	// Support POST operation
	// If we are updating our data stucture, we need to re-adjust the ID's
	// afterwards
	// bc the data structure is mutable but the setId() function needs to be run
	// again
	public static void add(String band, int ranking) {
		Ranking localBand = new Ranking();
		localBand.setBandName(band);
		localBand.setRanking(ranking);
		bandNames.add(ranking - 1, localBand);
		updateRankings();
	}

	public static void updateRankings() {
		CopyOnWriteArrayList<Ranking> currentBands = Rankings.getList();

		for (int i = 0; i < currentBands.size(); i++) {
			Ranking currentBand = currentBands.get(i);
			currentBand.setRanking(i + 1);
		}
	}
}
