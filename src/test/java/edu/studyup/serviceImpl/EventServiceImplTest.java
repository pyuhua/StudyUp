package edu.studyup.serviceImpl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import edu.studyup.entity.Event;
import edu.studyup.entity.Location;
import edu.studyup.entity.Student;
import edu.studyup.util.DataStorage;
import edu.studyup.util.StudyUpException;

import java.text.SimpleDateFormat;
import java.text.ParseException;

class EventServiceImplTest {

	EventServiceImpl eventServiceImpl;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		eventServiceImpl = new EventServiceImpl();
		//Create Student
		Student student = new Student();
		student.setFirstName("John");
		student.setLastName("Doe");
		student.setEmail("JohnDoe@email.com");
		student.setId(1);
		
		//Create Event1
		Event event = new Event();
		event.setEventID(1);
		event.setDate(new Date());
		event.setName("Event 1");
		Location location = new Location(-122, 37);
		event.setLocation(location);
		List<Student> eventStudents = new ArrayList<>();
		eventStudents.add(student);
		event.setStudents(eventStudents);
		
		DataStorage.eventData.put(event.getEventID(), event);
	}

	@AfterEach
	void tearDown() throws Exception {
		DataStorage.eventData.clear();
	}

	@Test
	void testUpdateEventName_GoodCase() throws StudyUpException {
		int eventID = 1;
		eventServiceImpl.updateEventName(eventID, "Renamed Event 1");
		assertEquals("Renamed Event 1", DataStorage.eventData.get(eventID).getName());
	}
	
	@Test
	void testUpdateEvent_WrongEventID_badCase() {
		int eventID = 3;
		Assertions.assertThrows(StudyUpException.class, () -> {
			eventServiceImpl.updateEventName(eventID, "Renamed Event 3");
		  });
	}
	
	@Test
	void testupdateEventName_NameLength20() {
		int eventID = 1;
		String testname20 = "abcdefghijklmnopqrst";
		Assertions.assertDoesNotThrow(() -> {
			eventServiceImpl.updateEventName(eventID, testname20);
		});
	}
	
	@Test
	void testupdateEventName_NameLength21() {
		int eventID = 1;
		String testname21 = "abcdefghijklmnopqrsta";
		Assertions.assertThrows(StudyUpException.class,() -> {
			eventServiceImpl.updateEventName(eventID, testname21);
		  });
	}
	
	@Test
	void testgetActiveEvents_PastEvent() throws ParseException {
		SimpleDateFormat dateformat = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        String TestDate = "03-03-1998 12:00:00";
        Date date3 = dateformat.parse(TestDate);
        
        Event Testevent = new Event();
        Testevent.setEventID(2);
        Testevent.setDate(date3);
        Testevent.setName("Event 3");
        Location location3 = new Location(100, 50);
        Testevent.setLocation(location3);
        DataStorage.eventData.put(Testevent.getEventID(), Testevent);
        
        List<Event> EventList = eventServiceImpl.getActiveEvents();
        for(Event i: EventList) {
        	Assertions.assertTrue(i.getDate().after(new Date()));
        }
	}
	
	@Test
	void testPastEvents() throws ParseException {
		SimpleDateFormat dateformat = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        String TestDate = "03-03-2020 12:00:00";
        Date date3 = dateformat.parse(TestDate);
        
        Event TestEvent = new Event();
        TestEvent.setEventID(2);
        TestEvent.setDate(date3);
        TestEvent.setName("Event 3");
        Location location = new Location(0, 0);
        TestEvent.setLocation(location);
        DataStorage.eventData.put(TestEvent.getEventID(), TestEvent);
        
        List<Event> EventList = eventServiceImpl.getPastEvents();
        for(Event i: EventList) {
        	Assertions.assertTrue(i.getDate().before(new Date()));
        }
	}
	
	@Test
	void testAddStudentToEvent_NullEvent() {
		//Create Student
		Student TestStudent = new Student();
		TestStudent.setFirstName("1");
		TestStudent.setLastName("1");
		TestStudent.setEmail("11@email.com");
		TestStudent.setId(2);
		Assertions.assertThrows(StudyUpException.class,() -> {
			eventServiceImpl.addStudentToEvent(TestStudent, 2);
		  });
	}

	
	@Test
	void testAddStudentToEvent_2StudentInEvent() throws StudyUpException{
		//Create Student
		Student TestStudent = new Student();
		TestStudent.setFirstName("1");
		TestStudent.setLastName("1");
		TestStudent.setEmail("11@email.com");
		TestStudent.setId(2);

		eventServiceImpl.addStudentToEvent(TestStudent, 1);
		Assertions.assertEquals(2, DataStorage.eventData.get(1).getStudents().size());
	}
	
	@Test
	void testAddStudentToEvent_3StudentInEvent() throws StudyUpException{
		//Create 2Students
		Student TestStudent = new Student();
		TestStudent.setFirstName("1");
		TestStudent.setLastName("1");
		TestStudent.setEmail("11@email.com");
		TestStudent.setId(2);
		Student TestStudent2 = new Student();
		TestStudent2.setFirstName("2");
		TestStudent2.setLastName("2");
		TestStudent2.setEmail("22@email.com");
		TestStudent2.setId(2);
		
		eventServiceImpl.addStudentToEvent(TestStudent, 1);
		eventServiceImpl.addStudentToEvent(TestStudent2, 1);
		Assertions.assertEquals(2, DataStorage.eventData.get(1).getStudents().size());
	}
	
	@Test
	void testDeleteEvent() {
		Event TestEvent = new Event();
        TestEvent.setEventID(2);
        TestEvent.setDate(new Date());
        TestEvent.setName("Event 3");
        Location location = new Location(0, 0);
        TestEvent.setLocation(location);
        DataStorage.eventData.put(TestEvent.getEventID(), TestEvent);
        eventServiceImpl.deleteEvent(2);
        Assertions.assertNull(DataStorage.eventData.get(2));
	}
}
