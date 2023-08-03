package com.ii.testautomation.service;

import com.ii.testautomation.dto.request.TestGroupingRequest;
import com.ii.testautomation.dto.search.TestGroupingSearch;
import com.ii.testautomation.response.common.PaginatedContentResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface TestGroupingService {
    void saveTestGrouping(TestGroupingRequest testGroupingRequest);

    public boolean existsByTestGroupingName(String testGroupingName, Long testCaseId);

    boolean existsByTestGroupingId(Long testGroupingId);

    boolean isUpdateTestGroupingNameExits(String testGroupingName, Long testCaseId, Long testGroupingId);

    void deleteTestGroupingById(Long testGroupingId);

    boolean existsByTestCasesId(Long testCaseId);

    boolean allTestCasesInSameProject(List<Long> testCaseIds);

    boolean existsByTestTypesId(Long testTypeId);


}
