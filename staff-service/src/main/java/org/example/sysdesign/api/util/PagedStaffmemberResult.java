package org.example.sysdesign.api.util;

import org.example.sysdesign.model.Staffmember;

import java.util.List;

/**
 * Utility record representing the result of a Staffmember query.
 * Query results are paged, meaning the contained ratings may be a subset of the total result.
 *
 * @param staffmember - Holds the Staffmember instances as a result of the query.
 * @param next - Boolean indicating whether additional results can be obtained.
 * @param count - Long indicating the total size of the query result.
 */
public record PagedStaffmemberResult(List<Staffmember> staffmember, Boolean next, Long count) {
}
