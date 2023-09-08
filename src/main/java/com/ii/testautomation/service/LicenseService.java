package com.ii.testautomation.service;
import com.ii.testautomation.dto.response.LicenseResponse;
import com.ii.testautomation.dto.search.LicensesSearch;
import com.ii.testautomation.response.common.PaginatedContentResponse;
import org.springframework.data.domain.Pageable;
import java.util.List;


public interface LicenseService {
 List< LicenseResponse> multiSearchLicensesWithPagination(Pageable pageable , PaginatedContentResponse.Pagination pagination, LicensesSearch licensesSearch);

}
