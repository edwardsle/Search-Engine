package dao.json;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.google.gson.Gson;

import dao.sql.DataSource;
import dao.sql.DocDAO;
import dao.sql.WordDAO;
import model.Doc;
import model.Posting;
import model.Word;

public class BookKeeping {
	private LinkedHashMap<String, Word> words;
	private LinkedHashMap<String, Doc> docs;
	final private String bookkeepPath = "WEBPAGES_RAW/bookkeeping.json";
	final private static String rootPath = "WEBPAGES_RAW";
	private LinkedHashMap<String, String> bookkeep;

	// 989 Stopwords
	// Credit:
	// https://github.com/igorbrigadir/stopwords/blob/master/en/atire_puurula.txt
	private String[] stopwordsArray = { "'ll", "'ve", "1-1", "a", "a's", "able", "about", "above", "abroad", "abst",
			"accordance", "according", "accordingly", "across", "act", "actually", "added", "adj", "adopted",
			"affected", "affecting", "affects", "after", "afterwards", "again", "against", "ago", "ah", "ahead",
			"ain't", "all", "allow", "allows", "almost", "alone", "along", "alongside", "already", "also", "although",
			"always", "am", "amid", "amidst", "among", "amongst", "amoungst", "amount", "an", "and", "announce",
			"another", "any", "anybody", "anyhow", "anymore", "anyone", "anything", "anyway", "anyways", "anywhere",
			"apart", "apparently", "appear", "appreciate", "appropriate", "approximately", "are", "area", "areas",
			"aren", "aren't", "arent", "arise", "around", "as", "aside", "ask", "asked", "asking", "asks", "associated",
			"at", "auth", "available", "away", "awfully", "b", "back", "backed", "backing", "backs", "backward",
			"backwards", "be", "became", "because", "become", "becomes", "becoming", "been", "before", "beforehand",
			"began", "begin", "beginning", "beginnings", "begins", "behind", "being", "beings", "believe", "below",
			"beside", "besides", "best", "better", "between", "beyond", "big", "bill", "biol", "both", "bottom",
			"brief", "briefly", "but", "by", "c", "c'mon", "c's", "ca", "call", "called", "came", "can", "can't",
			"cannot", "cant", "caption", "case", "cases", "cause", "causes", "certain", "certainly", "changes", "clear",
			"clearly", "co", "co.", "com", "come", "comes", "con", "concerning", "consequently", "consider",
			"considering", "contain", "containing", "contains", "corresponding", "could", "couldn't", "couldnt",
			"course", "cry", "currently", "d", "dare", "daren't", "date", "de", "dear", "definitely", "describe",
			"described", "despite", "detail", "did", "didn't", "differ", "different", "differently", "directly", "do",
			"does", "doesn't", "doing", "don't", "done", "down", "downed", "downing", "downs", "downwards", "due",
			"during", "e", "each", "early", "ed", "edu", "effect", "eg", "eight", "eighty", "either", "eleven", "else",
			"elsewhere", "empty", "end", "ended", "ending", "ends", "enough", "entirely", "especially", "et", "et-al",
			"etc", "even", "evenly", "ever", "evermore", "every", "everybody", "everyone", "everything", "everywhere",
			"ex", "exactly", "example", "except", "f", "face", "faces", "fact", "facts", "fairly", "far", "farther",
			"felt", "few", "fewer", "ff", "fifteen", "fifth", "fify", "fill", "find", "finds", "fire", "first", "five",
			"fix", "followed", "following", "follows", "for", "forever", "former", "formerly", "forth", "forty",
			"forward", "found", "four", "from", "front", "full", "fully", "further", "furthered", "furthering",
			"furthermore", "furthers", "g", "gave", "general", "generally", "get", "gets", "getting", "give", "given",
			"gives", "giving", "go", "goes", "going", "gone", "good", "goods", "got", "gotten", "great", "greater",
			"greatest", "greetings", "group", "grouped", "grouping", "groups", "h", "had", "hadn't", "half", "happens",
			"hardly", "has", "hasn't", "hasnt", "have", "haven't", "having", "he", "he'd", "he'll", "he's", "hed",
			"held", "hello", "help", "hence", "her", "here", "here's", "hereafter", "hereby", "herein", "heres",
			"hereupon", "hers", "herse", "herself", "hes", "hi", "hid", "high", "higher", "highest", "him", "himse",
			"himself", "his", "hither", "home", "hopefully", "how", "howbeit", "however", "hundred", "i", "i'd", "i'll",
			"i'm", "i've", "id", "ie", "if", "ignored", "im", "immediate", "immediately", "importance", "important",
			"in", "inasmuch", "inc", "inc.", "include", "included", "including", "indeed", "index", "indicate",
			"indicated", "indicates", "information", "inner", "inside", "insofar", "instead", "interest", "interested",
			"interesting", "interests", "into", "invention", "inward", "is", "isn't", "it", "it'd", "it'll", "it's",
			"itd", "its", "itse", "itself", "j", "just", "k", "keep", "keeps", "kept", "keys", "kg", "kind", "km",
			"knew", "know", "known", "knows", "l", "large", "largely", "last", "late", "lately", "later", "latest",
			"latter", "latterly", "least", "led", "less", "lest", "let", "let's", "lets", "like", "liked", "likely",
			"likewise", "line", "links", "little", "long", "longer", "longest", "look", "looking", "looks", "low",
			"lower", "ltd", "m", "made", "mainly", "make", "makes", "making", "man", "many", "may", "maybe", "mayn't",
			"me", "mean", "means", "meantime", "meanwhile", "member", "members", "men", "merely", "mg", "might",
			"mightn't", "mill", "million", "mine", "minus", "miss", "ml", "more", "moreover", "most", "mostly", "move",
			"moved", "mr", "mrs", "much", "mug", "must", "mustn't", "my", "myse", "myself", "n", "na", "name", "namely",
			"nay", "nd", "near", "nearly", "necessarily", "necessary", "need", "needed", "needing", "needn't", "needs",
			"neither", "never", "neverf", "neverless", "nevertheless", "new", "newer", "newest", "next", "nine",
			"ninety", "no", "no-one", "nobody", "non", "none", "nonetheless", "noone", "nor", "normally", "nos", "not",
			"noted", "nothing", "notwithstanding", "novel", "now", "nowhere", "number", "numbers", "o", "obtain",
			"obtained", "obviously", "of", "off", "often", "oh", "ok", "okay", "old", "older", "oldest", "omitted",
			"on", "once", "one", "one's", "ones", "only", "onto", "open", "opened", "opening", "opens", "opposite",
			"or", "ord", "order", "ordered", "ordering", "orders", "other", "others", "otherwise", "ought", "oughtn't",
			"our", "ours", "ourselves", "out", "outside", "over", "overall", "owing", "own", "p", "page", "pages",
			"part", "parted", "particular", "particularly", "parting", "parts", "past", "per", "perhaps", "place",
			"placed", "places", "please", "plus", "point", "pointed", "pointing", "points", "poorly", "possible",
			"possibly", "potentially", "pp", "predominantly", "present", "presented", "presenting", "presents",
			"presumably", "previously", "primarily", "probably", "problem", "problems", "promptly", "proud", "provided",
			"provides", "put", "puts", "q", "que", "quickly", "quite", "qv", "r", "ran", "rather", "rd", "re",
			"readily", "really", "reasonably", "received", "recent", "recently", "ref", "refs", "regarding",
			"regardless", "regards", "related", "relatively", "research", "respectively", "resulted", "resulting",
			"results", "right", "room", "rooms", "round", "run", "s", "said", "same", "saw", "say", "saying", "says",
			"sec", "second", "secondly", "seconds", "section", "see", "seeing", "seem", "seemed", "seeming", "seems",
			"seen", "sees", "self", "selves", "sensible", "sent", "serious", "seriously", "seven", "several", "shall",
			"shan't", "she", "she'd", "she'll", "she's", "shed", "shes", "should", "shouldn't", "show", "showed",
			"showing", "shown", "showns", "shows", "side", "sides", "significant", "significantly", "similar",
			"similarly", "since", "sincere", "six", "sixty", "slightly", "small", "smaller", "smallest", "so", "some",
			"somebody", "someday", "somehow", "someone", "somethan", "something", "sometime", "sometimes", "somewhat",
			"somewhere", "soon", "sorry", "specifically", "specified", "specify", "specifying", "state", "states",
			"still", "stop", "strongly", "sub", "substantially", "successfully", "such", "sufficiently", "suggest",
			"sup", "sure", "system", "t", "t's", "take", "taken", "taking", "tell", "ten", "tends", "th", "than",
			"thank", "thanks", "thanx", "that", "that'll", "that's", "that've", "thats", "the", "their", "theirs",
			"them", "themselves", "then", "thence", "there", "there'd", "there'll", "there're", "there's", "there've",
			"thereafter", "thereby", "thered", "therefore", "therein", "thereof", "therere", "theres", "thereto",
			"thereupon", "these", "they", "they'd", "they'll", "they're", "they've", "theyd", "theyre", "thick", "thin",
			"thing", "things", "think", "thinks", "third", "thirty", "this", "thorough", "thoroughly", "those", "thou",
			"though", "thoughh", "thought", "thoughts", "thousand", "three", "throug", "through", "throughout", "thru",
			"thus", "til", "till", "time", "tip", "tis", "to", "today", "together", "too", "took", "top", "toward",
			"towards", "tried", "tries", "truly", "try", "trying", "ts", "turn", "turned", "turning", "turns", "twas",
			"twelve", "twenty", "twice", "two", "u", "un", "under", "underneath", "undoing", "unfortunately", "unless",
			"unlike", "unlikely", "until", "unto", "up", "upon", "ups", "upwards", "us", "use", "used", "useful",
			"usefully", "usefulness", "uses", "using", "usually", "uucp", "v", "value", "various", "versus", "very",
			"via", "viz", "vol", "vols", "vs", "w", "want", "wanted", "wanting", "wants", "was", "wasn't", "way",
			"ways", "we", "we'd", "we'll", "we're", "we've", "wed", "welcome", "well", "wells", "went", "were",
			"weren't", "what", "what'll", "what's", "what've", "whatever", "whats", "when", "whence", "whenever",
			"where", "where's", "whereafter", "whereas", "whereby", "wherein", "wheres", "whereupon", "wherever",
			"whether", "which", "whichever", "while", "whilst", "whim", "whither", "who", "who'd", "who'll", "who's",
			"whod", "whoever", "whole", "whom", "whomever", "whos", "whose", "why", "widely", "will", "willing", "wish",
			"with", "within", "without", "won't", "wonder", "words", "work", "worked", "working", "works", "world",
			"would", "wouldn't", "written", "www", "x", "y", "year", "years", "yes", "yet", "you", "you'd", "you'll",
			"you're", "you've", "youd", "young", "younger", "youngest", "your", "yourabout", "youre", "yours",
			"yourself", "yourselves", "z", "zero" };
	private List<String> stopwords = Arrays.asList(stopwordsArray);

	public BookKeeping() {
		super();
		this.words = new LinkedHashMap<>();
		this.docs = new LinkedHashMap<>();
	}

	public LinkedHashMap<String, Word> getWords() {
		return words;
	}

	public LinkedHashMap<String, Doc> getDocs() {
		return docs;
	}

	@SuppressWarnings("Gson checked")
	public void loadBookKeep() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(bookkeepPath));
		Gson gson = new Gson();
		bookkeep = gson.fromJson(br, LinkedHashMap.class);
	}

	public void parseFromBookKeep() throws IOException {
		if (bookkeep == null)
			loadBookKeep();

		System.out.println("Parsing indexes from bookkeep:" + bookkeepPath);
		int i = 0;
		for (String docId : bookkeep.keySet()) {
			i++;
			docs.put(docId, new Doc(docId, bookkeep.get(docId)));
			updateWordsFromNewDoc(docId);
			if (i % 1000 == 0)
				System.out.println("Finished feeding " + i + " pages");
			// if (i == 5000)
			// break;
		}
		updateAllScore();
		System.out.println("Finished total of " + i + " pages.");
	}

	public void updateAllScore() {
		for (Word word : words.values()) {
			word.computeIdf(docs.size());
			word.UpdatePostingScore();
		}
	}

	public void updateWordsFromNewDoc(String docId) throws IOException {
		String[] terms = getTermsFromDocFile(docId);
		for (String term : terms) {
			if (!stopwords.contains(term))
				addOrUpdateTerm(term, docId);
		}
	}

	private void addOrUpdateTerm(String term, String docId) {
		if (words.containsKey(term))
			words.get(term).addOrUpdate(new Posting(docId, 0.0, 1));
		else {
			Word word = new Word(term);
			word.addOrUpdate(new Posting(docId, 0.0, 1));
			words.put(term, word);
		}
	}

	public static String[] getTermsFromDocFile(String docId) throws IOException {
		return getText(docId).split("(\\W+|_+)");
	}

	public static Document loadFile(String id) throws IOException {
		String filepath = rootPath + "/" + id;
		File input = new File(filepath);
		Document doc = Jsoup.parse(input, "UTF-8");
		return doc;
	}

	public static String getText(String id) throws IOException {
		return loadFile(id).text().toLowerCase();
	}

	public static void main(String[] args) throws IOException, SQLException {
		BookKeeping bk = new BookKeeping();
		bk.loadBookKeep();
		bk.parseFromBookKeep();

		DocDAO docDao = new DocDAO();
		docDao.addDocs((bk.getDocs().values()));
		//
		// for (Word word : bk.getWords().values())
		// {
		//// System.out.println(word.getTerm());
		// for (Posting posting : word.getPostings())
		// {
		//// System.out.println("----- " + posting.getDocId() + " - " +
		// posting.getScore()+ " - " + posting.getTf());
		// }
		// }
		////
		WordDAO wordDao = new WordDAO();
		wordDao.addWords(bk.getWords().values());
		//
		DataSource.close();
	}
}
