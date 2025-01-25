package com.nand.grievance.publisher.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PostMetricsDTO {
    //TODO: Give a metrics for the graphs -
    // Number of posts opened,
    // Number of posts resolved,
    // Number of posts escalated,
    // Number of posts which are highly active (take a threshold value to filter active posts)

    private Long unResolvedPostCount;

    private Long resolvedPostCount;

    private Long escalatedPostCount;

    private Long trendingPostCount;

    private Long totalPostCount;

    private Long totalAnonymousPostCount;

    private Long totalUnResolvedAnonymousPostCount;

    private Long totalResolvedAnonymousPostCount;

}
