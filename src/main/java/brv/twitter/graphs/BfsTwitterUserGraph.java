package brv.twitter.graphs;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import twitter4j.PagableResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

public class BfsTwitterUserGraph implements TwitterUserGraph {

	private static final Logger LOGGER = LogManager.getLogger(BfsTwitterUserGraph.class.getName());
	
	@Autowired
	private Twitter twitter;
	
	private static final int USERS_PER_PAGE = 200;
	private static final long MILLISECONDS = 1000;
	
	private Set<String> visitedFollowers = new HashSet<>();
	private Queue<User> users = new LinkedList<>();
	
	@Override
	public void traverse(String screenName) {

		try {
			
			visitedFollowers.add(screenName);
			users.add(twitter.showUser(screenName));
			traverse();
			
		} catch (TwitterException e) {
			
			LOGGER.error("An error has occured: "+ e.getErrorMessage());
			LOGGER.error("HTTP Status Code: "+e.getStatusCode());		
		}

		
	}
	
	/**
	 * Traverses all users in the queue until there are no users.
	 * @throws TwitterException
	 */
	private void traverse() throws TwitterException {

		while(!users.isEmpty()) {
		
			// Traverse the user followers
			traverse(users.poll(), -1);

		}
		
	}
	
	/**
	 * Traverses all the followers from the currentUser.
	 * 
	 * @param currentUser
	 * @param cursor
	 * @throws TwitterException
	 */
	private void traverse(User currentUser, long currentCursor) throws TwitterException {
		
		long cursor = currentCursor;
		
		try {
			
			if(!currentUser.isProtected()) {
				
				// Obtain followers for current user iteration
				PagableResponseList<User> followerPage;
				
				
				do {
					
					followerPage = twitter.getFollowersList(currentUser.getScreenName(), 
															cursor, 
															USERS_PER_PAGE);
					
					traverse(followerPage);
			
				} while((cursor = followerPage.getNextCursor()) != 0);
			}
			
		} catch (TwitterException e) {
			
			LOGGER.warn("Exception - traverse(User,long)");
			// Rate Limit Exceeded
			if(e.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS.value()) {
				
				// Relaunch execution
				int secondsUntilReset = e.getRateLimitStatus().getSecondsUntilReset();
				LOGGER.warn(e.getErrorMessage()+ ". "+ secondsUntilReset+" seconds before retry");
				sleep(secondsUntilReset);
				
				traverse(currentUser, currentCursor);
				
			} else {
				LOGGER.info("User failed: "+currentUser);
				throw e;
			}
		}
	}
	
	/**
	 * Traverses a single page of followers.
	 * <p>
	 * Every user is marked as visited and added to the queue of users to be checked later.
	 * </p>
	 * @param followerPage
	 */
	private void traverse(PagableResponseList<User> userPage) throws TwitterException {
		
		for(User user : userPage) {
			
			if(!visitedFollowers.contains(user.getScreenName())) {

				// Mark as visited and add it to the queue
				visitedFollowers.add(user.getScreenName());
				users.add(user);
				
				// Execute specific action with this user
				executeAction(user);
				
			} else {
				
				// Already visited follower
				LOGGER.info("Already visited: @"+user.getScreenName());
			}
		}
		
	}
	
	/**
	 * Executes an action on the specified user.
	 * @param screenName
	 */
	private void executeAction(User user) throws TwitterException{
		
		if(!user.isFollowRequestSent()) {
			LOGGER.info("Sending follow request: @"+user.getScreenName());
			//twitter.createFriendship(user.getScreenName());
		}
		
		/*
		try {
			
			twitter.createFriendship(user.getScreenName());
			
		} catch (TwitterException e) {
			
			// Rate Limit Exceeded
			if(e.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS.value()) {
				
				LOGGER.warn(e.getErrorMessage());
				
				// Relaunch execution
				int secondsUntilReset = e.getRateLimitStatus().getSecondsUntilReset();
				LOGGER.info("Seconds before retry: "+ secondsUntilReset);
				sleep(secondsUntilReset);
				
				executeAction(user);
				
			} else {
				throw e;
			}
		}
		*/
	}
	
	private void sleep(int seconds) {
		
		try {
			
			LOGGER.info("Visited users: "+visitedFollowers.size());
			LOGGER.info("Current users in queue: "+users.size());
			Thread.sleep(seconds * MILLISECONDS);
			
		} catch (InterruptedException e) {
			LOGGER.error("Thread was interrupted");
			Thread.currentThread().interrupt();
		}
	}


}
