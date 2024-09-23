package com.dasgupta.careercompass.bookmark;

import com.dasgupta.careercompass.job.JobDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookmarkService {
    void addBookmark(Integer userId, Integer jobId);

    void removeBookmark(Integer userId, Integer jobId);

    Page<JobDto> getBookmarkedJobs(Integer userId, Pageable pageable);

    boolean isJobBookmarked(Integer userId, Integer jobId);
}
