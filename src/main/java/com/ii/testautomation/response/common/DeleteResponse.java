package com.ii.testautomation.response.common;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter

    public class DeleteResponse {
        private List<Long> successfullyDeletedIds;
        private List<Long> failedToDeleteIds;

        public DeleteResponse(List<Long> successfullyDeletedIds, List<Long> failedToDeleteIds) {
            this.successfullyDeletedIds = successfullyDeletedIds;
            this.failedToDeleteIds = failedToDeleteIds;
        }

    }


