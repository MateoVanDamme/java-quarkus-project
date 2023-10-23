package org.example.sysdesign.api.util;

import org.example.sysdesign.model.CatalogusItem;

import java.util.List;

/**
 * Utility record representing the result of a catalogus item query.
 * Query results are paged, meaning the contained items may be a subset of the total result.
 *
 * @param items - Holds the catalogus item instances as a result of the query.
 * @param next - Boolean indicating whether additional results can be obtained.
 * @param count - Long indicating the total size of the query result.
 */
public record PagedCatalogusItemResult(List<CatalogusItem> items, Boolean next, Long count) {
}
