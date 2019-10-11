package dao.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.TreeMap;

import model.Posting;
import model.SearchResult;

public class SearchDAO extends BasicDAO {

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

	public ArrayList<SearchResult> search(String multipleQuery, int topLimit) throws SQLException {
		multipleQuery = multipleQuery.toLowerCase();
		String[] words = multipleQuery.split("(\\W+|_+)");

		ArrayList<String> filteredWords = new ArrayList<String>();

		// filter out stop words and remove duplicates
		for (String word : words) {
			if (!stopwords.contains(word) && !filteredWords.contains(word)) {
				filteredWords.add(word);
			}
		}

		TreeMap<Integer, ArrayList<SearchResult>> tree = new TreeMap<Integer, ArrayList<SearchResult>>();

		for (String word : filteredWords) {
			ArrayList<SearchResult> list = getAllMatches(word);
			tree.put(list.size(), list);
		}

		// find the intersection of the posting lists

		ArrayList<SearchResult> merged = null;

		for (int key : tree.keySet()) {
			if (merged == null)
				merged = tree.get(key);
			else {
				merged = mergePostingListUnion(merged, tree.get(key));
			}
		}

		// sort the result based on the score

		TreeMap<Double, SearchResult> sorted = new TreeMap<>();
		for (SearchResult each : merged) {
			sorted.put(each.getScore(), each);
		}

		// get limited result
		merged = new ArrayList<>();
		int count = 0;
		for (double key : sorted.descendingKeySet()) {
			count++;
			merged.add(sorted.get(key));
			if (count >= topLimit)
				break;
		}

		return merged;
	}

	public ArrayList<SearchResult> getAllMatches(String singleWord) throws SQLException {
		PreparedStatement statement = connection.prepareStatement(
				"SELECT * FROM (docs INNER JOIN (SELECT DISTINCT doc_id, word_id, score, tf FROM postings WHERE word_id = ? ORDER BY score DESC) q ON docs.docId = q.doc_id)");
		statement.setString(1, singleWord);
		ResultSet rs = statement.executeQuery();

		ArrayList<SearchResult> matches = new ArrayList<SearchResult>();
		while (rs.next()) {
			String url = rs.getString("url");
			String doc_id = rs.getString("doc_id");
			ArrayList<String> keyword = new ArrayList<String>();
			keyword.add(singleWord);
			double score = rs.getDouble("score");
			int tf = rs.getInt("tf");

			matches.add(new SearchResult(doc_id, score, keyword, url));
		}

		statement.close();

		return matches;
	}

	public ArrayList<SearchResult> mergePostingListInnerJoin(ArrayList<SearchResult> merge,
			ArrayList<SearchResult> fromList) {
		for (SearchResult result : fromList) {
			if (merge.contains(result)) {
				result.getMatchedKeywords().addAll(merge.get(merge.indexOf(result)).getMatchedKeywords());
				result.setScore(result.getScore() + merge.get(merge.indexOf(result)).getScore());
				merge.add(result);
			}
		}

		return merge;
	}

	public ArrayList<SearchResult> mergePostingListUnion(ArrayList<SearchResult> list1, ArrayList<SearchResult> list2) {
		ArrayList<SearchResult> small;
		ArrayList<SearchResult> big;

		if (list1.size() < list2.size()) {
			small = list1;
			big = list2;
		} else {
			small = list2;
			big = list1;
		}

		for (SearchResult result : small) {
			if (big.contains(result)) {
				SearchResult correspondence = big.get(big.indexOf(result));
				correspondence.getMatchedKeywords().addAll(result.getMatchedKeywords());
				correspondence.setScore(result.getScore() + correspondence.getScore());
			}
		}

		return big;
	}

	public SearchDAO() throws SQLException {
		super();
		// TODO Auto-generated constructor stub
	}

}
