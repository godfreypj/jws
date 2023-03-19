package aphorism2;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Adages {
	private static CopyOnWriteArrayList<Adage> adages;
	private static AtomicInteger id;

	static {
		String[] aphorisms = { "What can be shown cannot be said.",
				"If a lion could talk, we could not understand him.",
				"Philosophy is a battle against the bewitchment of our intelligence by means of language.",
				"Ambition is the death of thought.",
				"The limits of my language mean the limits of my world." };
		adages = new CopyOnWriteArrayList<Adage>();
		id = new AtomicInteger();
		for (String str : aphorisms)
			addInitial(str);
	}

	public static String toPlain() {
		String retval = "";
		for (Adage adage : adages)
			retval += adage.toString() + "\n";
		return retval;
	}

	public static CopyOnWriteArrayList<Adage> getList() {
		return adages;
	}

	// Support GET one operation.
	public static Adage find(int id) {
		Adage adage = null;
		for (Adage a : adages) {
			if (a.getId() == id) {
				adage = a;
				break;
			}
		}
		return adage;
	}

	// Support Initial Add operation.
	public static void addInitial(String words) {
		int localId = id.incrementAndGet();
		Adage adage = new Adage();
		adage.setWords(words);
		adage.setId(localId);
		adages.add(adage);
	}

	// Support POST operation
	// If we are updating our data stucture, we need to re-adjust the ID's
	// afterwards
	// bc the data structure is mutable but the setId() function needs to be run
	// again
	public static void add(String words, int ranking) {
		Adage adage = new Adage();
		adage.setWords(words);
		adage.setId(ranking);
		adages.add(ranking - 1, adage);

		CopyOnWriteArrayList<Adage> currentAdages = Adages.getList();

		for (int i = 0; i < currentAdages.size(); i++) {
			Adage currentAdage = currentAdages.get(i);
			currentAdage.setId(i + 1);
		}
	}
}
