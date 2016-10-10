package com.tiy;

import org.junit.*;
import org.junit.runner.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.*;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GameTrackerSpringApplicationTests {

	@Autowired
	UserRepository userRepo;

	@Autowired
	UserEventRepository userEventRepo;

	@Autowired
	EventRepository eventRepo;

	@Autowired
	ContactRequestRepository contactRequestRepo;

	@Autowired
	private TestRestTemplate restTemplate;

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private GameTrackerJSONController trackerService;

	@Test
	public void contextLoads() {
	}


	@Test
	public void testGamesEndPoint() throws Exception {
		logger.info("testGamesEndPoint()");

		List<Game> games = this.trackerService.getGames();
		assertNotNull(games);
		for (Game game : games) {
			logger.info("Game = " + game.getName());
		}

		String gamesAsJson = this.restTemplate.getForObject("/games.json", String.class);
		// make sure the json string returned is not null
		assertNotNull(gamesAsJson);
		logger.info("JSON response = " + gamesAsJson);
	}

	@Test
	public void testContactRequest() {
		logger.debug("testContactRequest() - debug");
		logger.info("testContactRequest() - info");
		logger.warn("testContactRequest() - warn");
		logger.error("testContactRequest() - error");

		User requestingUser = null;
		User targetUser = null;
		ContactRequest contactRequest = null;

		try {

			requestingUser = new User("test-user-abc123", "test-password");
			requestingUser = userRepo.save(requestingUser);

			targetUser = new User("target-user-abc123", "test-password");
			targetUser = userRepo.save(targetUser);

			contactRequest = new ContactRequest(requestingUser, targetUser,
					"PENDING_APPROVAL");
			contactRequest = contactRequestRepo.save(contactRequest);

			// check that the contact request got created properly
			ContactRequest dbContactRequest = contactRequestRepo.findOne(contactRequest.getId());
			assertNotNull(dbContactRequest);
			assertEquals(contactRequest.getId(), dbContactRequest.getId());

//			assertNotNull(null);
		} finally {
			logger.debug("Cleaning up after testContactRequest()");
			// clean up after myself
			if (contactRequest != null) {
				contactRequestRepo.delete(contactRequest);
			}
			if (requestingUser != null) {
				userRepo.delete(requestingUser);
			}
			if (targetUser != null) {
				userRepo.delete(targetUser);
			}
		}

		logger.debug("testContactRequest() - done");
	}

	@Test
	public void testAttendEvent() {
		Event testEvent = null;
		Event anotherEvent = null;
		User testUser = null;
		User secondTestUser = null;
		UserEvent testUserEvent = null;
		UserEvent anotherUserEvent = null;
		UserEvent secondTestUserEvent = null;

		try {
			testEvent = new Event("test-event", "test-location");
			testEvent = eventRepo.save(testEvent);

			anotherEvent = new Event("test-event2", "test-location");
			anotherEvent = eventRepo.save(anotherEvent);

			testUser = new User("test-user", "test-password");
			testUser = userRepo.save(testUser);

			secondTestUser = new User("second-test-user", "test-password");
			secondTestUser = userRepo.save(secondTestUser);

			testUserEvent = new UserEvent(testUser, testEvent);
			userEventRepo.save(testUserEvent);
			anotherUserEvent = new UserEvent(testUser, anotherEvent);
			userEventRepo.save(anotherUserEvent);
			secondTestUserEvent = new UserEvent(secondTestUser, anotherEvent);
			userEventRepo.save(secondTestUserEvent);

			// make sure that we can find all the events
			// for our test user
			List<UserEvent> dbUserEvents = userEventRepo.findByUser(testUser);
			assertNotNull(dbUserEvents);
			assertEquals(2, dbUserEvents.size());
			for (UserEvent dbUserEvent : dbUserEvents) {
				System.out.println("User Event ID " + dbUserEvent.getId() +
						" for event " + dbUserEvent.getEvent().getId());
			}

			// make sure we can find a specific event for our test user
			List<UserEvent> listForOneSpecificEvent = userEventRepo.findByUserAndEvent(testUser, anotherEvent);
			assertNotNull(listForOneSpecificEvent);
			assertEquals(1, listForOneSpecificEvent.size());
			assertEquals(anotherEvent.getId(), listForOneSpecificEvent.get(0).getEvent().getId());

			// make sure we can find a specific event for our second test user
			List<UserEvent> userEventsForSecondTestUser = userEventRepo.findByUserAndEvent(secondTestUser, anotherEvent);
			assertNotNull(userEventsForSecondTestUser);
			assertEquals(1, userEventsForSecondTestUser.size());
			assertEquals(anotherEvent.getId(), userEventsForSecondTestUser.get(0).getEvent().getId());
		} finally {
			// clean up after myself
			userEventRepo.delete(testUserEvent);
			userEventRepo.delete(anotherUserEvent);
			userEventRepo.delete(secondTestUserEvent);
			userRepo.delete(testUser.getId());
			userRepo.delete(secondTestUser);
			eventRepo.delete(testEvent.getId());
			eventRepo.delete(anotherEvent);
		}
	}

	@Test
	public void testCreateUser() {
		User testUser = new User("test-user", "test-password");
		testUser = userRepo.save(testUser);
		System.out.println("Created user with ID: " + testUser.getId());

		User dbUser = userRepo.findOne(testUser.getId());
		assertNotNull(dbUser);

		userRepo.delete(testUser.getId());
		dbUser = userRepo.findOne(testUser.getId());
		assertNull(dbUser);
	}

	@Test
	public void testCreateEvent() {
		Event testEvent = new Event("test-event", "test-location");
		testEvent = eventRepo.save(testEvent);
		System.out.println("Created event with ID: " + testEvent.getId());

		Event dbEvent = eventRepo.findOne(testEvent.getId());
		assertNotNull(dbEvent);

		eventRepo.delete(testEvent.getId());
		dbEvent = eventRepo.findOne(testEvent.getId());
		assertNull(dbEvent);
	}

}
