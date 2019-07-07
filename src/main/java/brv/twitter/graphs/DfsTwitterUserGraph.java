package brv.twitter.graphs;

import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import twitter4j.PagableResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

public class DfsTwitterUserGraph implements TwitterUserGraph {

	private static final Logger LOGGER = LogManager.getLogger(DfsTwitterUserGraph.class.getName());
	
	@Autowired
	private Twitter twitter;
	
	@Override
	public void traverse(String screenName) {

		traverse(screenName, new HashSet<>(), -1);
		
	}

	private void traverse(String screenName, Set<String> visitedFollowers, long currentCursor) {
		
		
		long cursor = currentCursor;
		try {
			
			visitedFollowers.add(screenName);
		
			// Obtain followers for current user iteration
			PagableResponseList<User> followerPage;
			
			
			do {
				
					followerPage = twitter.getFollowersList(screenName, cursor);
					//followerPage = twitter.getFriendsList(screenName, cursor);
					for(User user : followerPage) {
						
						if(!visitedFollowers.contains(user.getScreenName())) {
								
							// Not visited follower
							LOGGER.info("Non visited follower: @"+user.getScreenName());
							//twitter.createFriendship(user.getScreenName());
							
							traverse(user.getScreenName(), visitedFollowers, -1);
							
						} else {
							
							// Already visited follower
							LOGGER.info("Already visited: @"+user.getScreenName());
						}
					}
	
			} while((cursor = followerPage.getNextCursor()) != 0);

		} catch (TwitterException e) {

			

			// Rate Limit Exceeded
			if(e.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS.value()) {
				
				LOGGER.warn(e.getErrorMessage());
				
				// Relaunch execution
				int secondsUntilReset = e.getRateLimitStatus().getSecondsUntilReset();
				LOGGER.info("Seconds before retry: "+ secondsUntilReset);
				sleep(secondsUntilReset);
				traverse(screenName,visitedFollowers, cursor);
			} else {
				LOGGER.error("An error has occured: "+ e.getErrorMessage());
				LOGGER.error("HTTP Status Code: "+e.getStatusCode());
				LOGGER.error("Exception code: "+e.getExceptionCode());
			}
			
		}
		
	}
	
	private void sleep(int seconds) {
		
		try {
			Thread.sleep(seconds*1000);
		} catch (InterruptedException e) {
			LOGGER.error("Thread was interrupted");
			Thread.currentThread().interrupt();
		}
	}
}
