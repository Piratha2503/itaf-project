package com.ii.testautomation.service;

import com.ii.testautomation.dto.response.ExecutionHistoryResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.w3c.dom.html.HTMLDocument;

import javax.swing.text.html.HTML;
import java.io.IOException;
import java.util.List;

public interface ExecutionHistoryService {

    boolean existByTestGropingId(Long id);

    List<ExecutionHistoryResponse> viewByTestGroupingId(Long id);

    String viewReportByExecutionHistoryId(Long id) throws IOException;

    boolean existByExecutionHistoryId(Long id);

    void deleteExecutionHistory(Long id);
}
