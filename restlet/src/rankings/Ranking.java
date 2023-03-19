package rankings;

public class Ranking {
    private String bandName;
    private int wordCount;
    private int ranking;

    public Ranking() {
    }

    // overrides
    @Override
    public String toString() {
        return String.format("Rank:%2d", this.ranking) + " - " + bandName + " -- " + wordCount + " words";
    }

    // properties
    public void setBandName(String band) {
        this.bandName = band;
        this.wordCount = band.trim().split("\\s+").length;
    }

    public String getBandName() {
        return this.bandName;
    }

    public void setWordCount(int wordCount) {
    }

    public int getWordCount() {
        return this.wordCount;
    }

    public void setRanking(int id) {
        this.ranking = id;
    }

    public int getRanking() {
        return this.ranking;
    }
}