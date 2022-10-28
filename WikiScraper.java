import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * name: Daniel Gil
 * class: csc210
 * purpose: this class scrapes the wikipedia website
 * and the articles in it to get the html inside any article
 * it then finds all the anchor hrefs tags inside the article
 * and stores all the titles in a set
 */
public class WikiScraper {
	
	private static Map<String, Set<String>> memo = new HashMap<>();
			
	/*
	 * findWikiLinks first gets the html in a wikipedia website
	 * then sends the html string into a scraper that will remove everything
	 * but the links and returns a set of anchor href references.
	 * Memoization also takes place here to speed up the process 
	 * which wont make the functions run twice
	 */
	public static Set<String> findWikiLinks(String link) {
		if (memo.containsKey(link)) {
			return memo.get(link);
		}
		String html = fetchHTML(link);
		Set<String> links = scrapeHTML(html);
		memo.put(link, links);
		return links;
	}
	
	/*
	 * fetchHTML gets the elements of a website and stores 
	 * them inside a big string
	 * it uses the https protocol to access a website
	 * and fetch all the html inside the website
	 */
	private static String fetchHTML(String link) {
		StringBuffer buffer = null;
		try {
			URL url = new URL(getURL(link));
			InputStream is = url.openStream();
			int ptr = 0;
			buffer = new StringBuffer();
			while ((ptr = is.read()) != -1) {
			    buffer.append((char)ptr);
			}
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			ex.printStackTrace();
		}
		return buffer.toString();
	}
	
	/*
	 * getURL turns a wikipedia title to an url link
	 */
	private static String getURL(String link) {
		return "https://en.wikipedia.org/wiki/" + link;
	}
	
	/*
	 * Takes in a string of the HTML source for a webPage.
	 * Returns a Set<String> containing all of the valid wiki link
	 * titles found in the HTML source.
	 */
	private static Set<String> scrapeHTML(String html) {
		Set<String> linkTitles = new HashSet<String>();
		if (html.contentEquals(""))
			return linkTitles;
		
		//iterate through string
		for (int i = 0; i < html.length(); i++) {
			//find anchor
			if (html.charAt(i) == '<' && html.charAt(i + 1) == 'a') {
				//find ending anchor
				int closingBracketIndex = html.indexOf(">", i);
				//get anchor
				String anchor = html.substring(i, closingBracketIndex);
				
				//while loop is just to use break
				int index = 0;
				while (index < anchor.length()) {
					//find href attribute
					int hrefTagStart = anchor.indexOf("href=\"/wiki/");
					int tagPathStart = anchor.indexOf("\"", hrefTagStart) + 2;
					int tagPathEnd = anchor.indexOf("\"", tagPathStart + 1);
					
					//if no href attribute
					if (tagPathStart == -1 || tagPathEnd == -1 || 
							hrefTagStart == -1)
						break;
					
					//get href
					String hrefPath = anchor.substring(tagPathStart, tagPathEnd);
					
					//invalid links
					if (hrefPath.contains(":") || hrefPath.contains("#") ||
							hrefPath.startsWith("//") ||
							hrefPath.contains("index.php"))
						break;
					
					//get title location
					int lastFolder = hrefPath.indexOf("/") + 1;
					
					if (!(lastFolder == -1)) {
						String title = hrefPath.substring(lastFolder);
						linkTitles.add(title);
					}
					index++;
					break;
				}
				//keep iterating after anchor
				i = closingBracketIndex;
			}
		}
		return linkTitles;
	}
	
}
