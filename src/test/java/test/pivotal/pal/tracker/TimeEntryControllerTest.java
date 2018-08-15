package test.pivotal.pal.tracker;

import io.pivotal.pal.tracker.TimeEntry;
import io.pivotal.pal.tracker.TimeEntryController;
import io.pivotal.pal.tracker.TimeEntryRepository;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.boot.actuate.metrics.GaugeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

import static java.util.Arrays.asList;


public class TimeEntryControllerTest {
    private TimeEntryRepository timeEntryRepository;
    private TimeEntryController controller;
    private CounterService counter;
    private GaugeService gauge;

    @Before
    public void setUp() throws Exception {
        timeEntryRepository = Mockito.mock(TimeEntryRepository.class);
        counter= Mockito.mock(CounterService.class);
        gauge= Mockito.mock(GaugeService.class);
        controller = new TimeEntryController(timeEntryRepository, counter, gauge);
    }

    @Test
    public void testCreate() throws Exception {
        TimeEntry timeEntryToCreate = new TimeEntry(123L, 456L, LocalDate.parse("2017-01-08"), 8);
        TimeEntry expectedResult = new TimeEntry(1L, 123L, 456L, LocalDate.parse("2017-01-08"), 8);
        Mockito.doReturn(expectedResult)
            .when(timeEntryRepository)
            .create(Matchers.any(TimeEntry.class));


        ResponseEntity response = controller.create(timeEntryToCreate);


        Mockito.verify(timeEntryRepository).create(timeEntryToCreate);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(response.getBody()).isEqualTo(expectedResult);
    }

    @Test
    public void testRead() throws Exception {
        TimeEntry expected = new TimeEntry(1L, 123L, 456L, LocalDate.parse("2017-01-08"), 8);
        Mockito.doReturn(expected)
            .when(timeEntryRepository)
            .find(1L);

        ResponseEntity<TimeEntry> response = controller.read(1L);

        Mockito.verify(timeEntryRepository).find(1L);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isEqualTo(expected);
    }

    @Test
    public void testRead_NotFound() throws Exception {
        Mockito.doReturn(null)
            .when(timeEntryRepository)
            .find(1L);

        ResponseEntity<TimeEntry> response = controller.read(1L);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testList() throws Exception {
        List<TimeEntry> expected = asList(
            new TimeEntry(1L, 123L, 456L, LocalDate.parse("2017-01-08"), 8),
            new TimeEntry(2L, 789L, 321L, LocalDate.parse("2017-01-07"), 4)
        );
        Mockito.doReturn(expected).when(timeEntryRepository).list();

        ResponseEntity<List<TimeEntry>> response = controller.list();

        Mockito.verify(timeEntryRepository).list();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isEqualTo(expected);
    }

    @Test
    public void testUpdate() throws Exception {
        TimeEntry expected = new TimeEntry(1L, 987L, 654L, LocalDate.parse("2017-01-07"), 4);
        Mockito.doReturn(expected)
            .when(timeEntryRepository)
            .update(Matchers.eq(1L), Matchers.any(TimeEntry.class));

        ResponseEntity response = controller.update(1L, expected);

        Mockito.verify(timeEntryRepository).update(1L, expected);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isEqualTo(expected);
    }

    @Test
    public void testUpdate_NotFound() throws Exception {
        Mockito.doReturn(null)
            .when(timeEntryRepository)
            .update(Matchers.eq(1L), Matchers.any(TimeEntry.class));

        ResponseEntity response = controller.update(1L, new TimeEntry());
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testDelete() throws Exception {
        ResponseEntity<TimeEntry> response = controller.delete(1L);
        Mockito.verify(timeEntryRepository).delete(1L);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
