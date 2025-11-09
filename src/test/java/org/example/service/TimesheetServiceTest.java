package org.example.service;


import org.example.dto.TimesheetDTO;
import org.example.dto.TimesheetDTOEntries;
import org.example.entity.ProjectsList;
import org.example.entity.Status;
import org.example.entity.Timesheet;
import org.example.entity.UserStatus;
import org.example.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TimesheetServiceTest {

    @Mock
    private TimesheetRepository timesheetRepository;
    @Mock
    private StatusRepository statusRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ProjectsListRepository projectsListRepository;

    @Mock
    private ClockRepository clockRepository;
    @Mock
    private UserStatusRepository userStatusRepository;
    @InjectMocks
    private TimesheetService timesheetService;

    private Timesheet timesheet;
    private TimesheetDTO timesheetDTO;
    private TimesheetDTOEntries entry;


    @BeforeEach
    void setup() {
        // Build dummy DTO input
        entry = new TimesheetDTOEntries();
        entry.setUserId(1L);
        entry.setDate(LocalDate.now());
        entry.setStart(LocalTime.of(9, 0));
        entry.setEnd(LocalTime.of(17, 0));
        entry.setWeek("2025-W45");
        entry.setTotal(8);
        entry.setProject("ProjectX");

        timesheetDTO = new TimesheetDTO();
        timesheetDTO.setUserId(1L);
        timesheetDTO.setWeek("2025-W45");
        timesheetDTO.setWeekTotal(40);
        timesheetDTO.setEntries(List.of(entry));
    }

    // ✅ Test for submitTimesheetWeek() happy path
    @Test
    void testSubmitTimesheetWeek_NewEntry() {
        // Mock repository behaviors
        when(timesheetRepository.findByEmpIdAndDate(anyLong(), any()))
                .thenReturn(Optional.empty());
        when(statusRepository.findByEmpIdAndWeek(anyLong(), any()))
                .thenReturn(Optional.empty());
        ProjectsList project = new ProjectsList();
        project.setProjectDescription("Backend Work");
        when(projectsListRepository.findByProjectName("ProjectX"))
                .thenReturn(project);

        // Execute
        timesheetService.submitTimesheetWeek(timesheetDTO);

        // Verify repository saves
        verify(timesheetRepository, times(1)).save(any(Timesheet.class));
        verify(statusRepository, times(1)).save(any(Status.class));
        verify(projectsListRepository, times(1)).findByProjectName("ProjectX");
    }

    // ✅ Test for submitTimesheetWeek() with "N/A" project
    @Test
    void testSubmitTimesheetWeek_NotApplicableProject() {
        entry.setProject("N/A");
        when(timesheetRepository.findByEmpIdAndDate(anyLong(), any()))
                .thenReturn(Optional.empty());
        when(statusRepository.findByEmpIdAndWeek(anyLong(), any()))
                .thenReturn(Optional.empty());

        timesheetService.submitTimesheetWeek(timesheetDTO);

        verify(statusRepository).save(argThat(status ->
                "Not Applicable".equals(status.getNote())));
    }




    // ✅ Test for userStat() when user status exists
    @Test
    void testUserStat_UserStatusExists() {
        UserStatus status = new UserStatus();
        status.setStatus("CLOCK_IN");
        when(userStatusRepository.findById(1L))
                .thenReturn(Optional.of(status));

        String result = invokePrivateUserStat(1L);
        assertEquals("CLOCK_IN", result);
    }

    // ✅ Test for userStat() when no status found
    @Test
    void testUserStat_UserStatusMissing() {
        when(userStatusRepository.findById(1L))
                .thenReturn(Optional.empty());

        String result = invokePrivateUserStat(1L);
        assertEquals("CLOCK_OUT", result);
    }

    // Helper to invoke private userStat() using reflection
    private String invokePrivateUserStat(Long id) {
        try {
            var method = TimesheetService.class.getDeclaredMethod("userStat", Long.class);
            method.setAccessible(true);
            return (String) method.invoke(timesheetService, id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
