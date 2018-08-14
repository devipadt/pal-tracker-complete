package io.pivotal.pal.tracker;

import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class InMemoryTimeEntryRepository implements TimeEntryRepository{


    private TimeEntry timeEntry;

    private HashMap<Long, TimeEntry> inMemData = new HashMap<>();

    public TimeEntry create(TimeEntry timeEntry) {
        TimeEntry timeEntryToCreate = new TimeEntry(inMemData.size() + 1, timeEntry.getProjectId(), timeEntry.getUserId(), timeEntry.getDate(), timeEntry.getHours());

        inMemData.put(timeEntryToCreate.getId(), timeEntryToCreate);
        return timeEntryToCreate;
    }

    public TimeEntry find(long id) {
        return inMemData.get(id);
    }

    public TimeEntry update(long id, TimeEntry timeEntry) {
        TimeEntry timeEntryToCreate = new TimeEntry(id, timeEntry.getProjectId(), timeEntry.getUserId(), timeEntry.getDate(), timeEntry.getHours());
        inMemData.replace(id, timeEntryToCreate);
        return timeEntryToCreate;
    }

    public void delete(long id) {
        inMemData.remove(id);
    }

    public List<TimeEntry> list() {
        return new ArrayList<>(inMemData.values());
    }
}
