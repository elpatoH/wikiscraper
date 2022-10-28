import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*
 * name: Daniel Gil
 * purpose: this class finds the path between two wikipedia websites
 * and returns the path (ladder) that can be taken to get from
 * one wikipedia article to the target article.
 */
public class WikiRacer {

	public static void main(String[] args) {
		List<String> ladder = findWikiLadder(args[0], args[1]);
		System.out.println(ladder);
	}

	/*
	 * findWikiLadder finds a path between one wikipedia article and another one.
	 * and returns a list<String> with the path(ladder) from one article
	 * to the target article
	 */
	private static List<String> findWikiLadder(String start, String end) {
		List<String> endPageLinks = new ArrayList<>(WikiScraper.findWikiLinks(end));
		Set<String> visitedPages = new HashSet<>();
		
		//Create an empty priority queue of ladders (a ladder is a List<String>).
		MaxPQ pq = new MaxPQ();
		
		//Create/add a ladder containing {startPage} to the queue.
		List<String> startLadder = new ArrayList<>();
		startLadder.add(start);
		visitedPages.add(start);
		pq.enqueue(startLadder, 1);
		
		//While the queue is not empty:
		while (!pq.isEmpty()) {
			//Dequeue the highest priority partial-ladder from the front of the queue.
			List<String> highestPrioLadder = pq.dequeue();
			System.out.println(highestPrioLadder); 
			
			//Get the set of links on the current page.
			//the current page is the page at the end of the just dequeued ladder.
			Set<String> links = WikiScraper.
					findWikiLinks(highestPrioLadder.get(highestPrioLadder.size() - 1));
			
			//If the endPage is in this set:
			if (links.contains(end)) {
				//Add endPage to the ladder you just dequeued and return it.
				highestPrioLadder.add(end);
				return highestPrioLadder;
			}
			
			//optimization
			links.parallelStream().forEach(link -> {
				WikiScraper.findWikiLinks(link);
				});
			
			//For each neighbor page in the current page's link set:
			for (String page: links) {
				//If this neighbor page hasn't already been visited:
				if (!visitedPages.contains(page)) {
					visitedPages.add(page);
					
					//Create a copy of the current partial-ladder.
					List<String> copyLadder = new ArrayList<>(highestPrioLadder);
					
					//Put the neighbor page String name on top of the copied ladder.
					copyLadder.add(page);

					//Add the copied ladder to the queue.
					List<String> linksInPage = new ArrayList<>(WikiScraper.findWikiLinks(page));
					pq.enqueue(copyLadder, getLadderPriority(linksInPage, endPageLinks));
				}
			}
			
		}
		List<String> empty = new ArrayList<>();
		return empty;
	}
	
	/*
	 * returns the given page priority.
	 * endPage is the page that is going to be compared and to measure the prio
	 * the more links page and endpage have in common the higher the prio
	 */
	private static int getLadderPriority(List<String> page, List<String> endPage){
		List<String> copy = new ArrayList<>(page);
		//only keeps the elements that are also in endPage
		copy.retainAll(endPage);
		return copy.size();
	}

}
