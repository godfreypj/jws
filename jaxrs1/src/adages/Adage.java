package adages;
import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "adage")
public class Adage {
    protected String words;
    protected int wordCount;
    protected String time;
    
    public Adage() { }

    // overrides
    @Override
    public String toString() {
	return words + " -- " + wordCount + " words" + " Today " + time;
    }
    
    // properties
    public void setWords(String words) { 
	this.words = words;
    this.time = getTime();
	this.wordCount = words.trim().split("\\s+").length;
    }
    public String getWords() { return this.words; }

    public void setWordCount(int wordCount) { }
    public int getWordCount() { return this.wordCount; }

    public void setTime(String time) { }
    public String getTime() {
        Date now = new Date();
        String time = now.toString();
        return time; }
}
