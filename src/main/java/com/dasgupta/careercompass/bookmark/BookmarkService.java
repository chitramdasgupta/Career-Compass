package com.dasgupta.careercompass.bookmark;

import com.dasgupta.careercompass.job.JobDto;

import java.util.List;

public interface BookmarkService {
    void addBookmark(Integer userId, Integer jobId);

    void removeBookmark(Integer userId, Integer jobId);

    List<JobDto> getBookmarkedJobs(Integer userId);

    boolean isJobBookmarked(Integer userId, Integer jobId);
}
