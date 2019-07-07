package brv.twitter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import brv.twitter.graphs.TwitterUserGraph;
import brv.twitter.graphs.TwitterUserGraphFactory;
import brv.twitter.services.FollowersService;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;

@SpringBootApplication
public class TwitterFollowersApplication implements CommandLineRunner {


	private static final Logger LOGGER = LogManager.getLogger(TwitterFollowersApplication.class.getName());
	
	@Autowired
	private FollowersService service;

	@Value("${app.traverse.startingUser:}")
	private String startingUser;
	
	public static void main(String[] args) {
		SpringApplication.run(TwitterFollowersApplication.class, args);
	}

	@Bean 
	public Twitter getTwitter() {

		return TwitterFactory.getSingleton();
	}
	
	@Bean 
	public TwitterUserGraph getTwitterUserGraph(@Value("${app.traverse.strategy:BFS}") TwitterUserGraphFactory factory) {
		
		LOGGER.info("Traversing strategy set to: "+ factory.name());
		return factory.getInstance();
	}
	
	@Override
	public void run(String... args) throws Exception {

		service.getFollowers(startingUser);
	}

}
