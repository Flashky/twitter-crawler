package brv.twitter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import brv.twitter.graphs.BfsTwitterUserGraph;
import brv.twitter.graphs.TwitterUserGraph;
import brv.twitter.services.FollowersService;
import twitter4j.RateLimitStatusListener;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;

@SpringBootApplication
@EnableScheduling
public class TwitterFollowersApplication implements CommandLineRunner {


	private static final Logger LOGGER = LogManager.getLogger(TwitterFollowersApplication.class.getName());
	
	@Autowired
	private FollowersService service;

	
	public static void main(String[] args) {
		SpringApplication.run(TwitterFollowersApplication.class, args);
	}

	@Bean 
	public Twitter getTwitter(RateLimitStatusListener listener) {
		if(listener == null) {
			LOGGER.warn("No RateLimitStatusListener has been appended.");
		}
		
		Twitter twitter = TwitterFactory.getSingleton();
		twitter.addRateLimitStatusListener(listener);
		
		return twitter;
	}
	
	@Bean 
	public TwitterUserGraph getTwitterUserGraph() {
		return new BfsTwitterUserGraph();
	}
	
	@Override
	public void run(String... args) throws Exception {

		service.getFollowers();
	}

}
