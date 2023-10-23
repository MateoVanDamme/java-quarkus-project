package org.example.sysdesign.api.util;

import org.example.sysdesign.model.Rating;

import java.util.List;

/**
 * Utility record representing the result of a Rating query.
 * Query results are paged, meaning the contained ratings may be a subset of the total result.
 *
 * @param ratings - Holds the rating instances as a result of the query.
 * @param next - Boolean indicating whether additional results can be obtained.
 * @param count - Long indicating the total size of the query result.
 */
public record PagedRatingResult(List<Rating> ratings, Boolean next, Long count) {
}
