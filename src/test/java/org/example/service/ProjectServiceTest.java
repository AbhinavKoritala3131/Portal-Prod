package org.example.service;

import org.example.entity.ProjectGroups;
import org.example.repository.ProjectGroupsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {

    @Mock
    private ProjectGroupsRepository groupsRepo;
    @InjectMocks
    private ProjectService projectService;

    private ProjectGroups pg;





    @Test
    void addUserToTypeTest() {
        // 1️⃣ Arrange - create a dummy ProjectGroups object
            pg = new ProjectGroups();
            pg.setProjectType("PYTHON");
            pg.setEmpId(452L);

        // 2️⃣ Stub repository behavior
        when(groupsRepo.save(pg)).thenReturn(pg);

        // 3️⃣ Act - call the service method
        ProjectGroups returned = projectService.addUserToType(pg);

        // 4️⃣ Assert - verify behavior and result
        assertEquals("PYTHON", returned.getProjectType());
        assertNotNull(returned);

        // 5️⃣ Verify interaction
        verify(groupsRepo,times(1)).save(pg);
    }

}
