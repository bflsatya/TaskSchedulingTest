package com.appviewx.repos;

import java.util.List;

public interface JdbcRepository<T> {
    int[] batchInsert(List<T> itemsList);
    int[][] batchInsert(List<T> itemsList, int batchSize);
    int[] batchUpdate(List<T> itemsList);
    int[][] batchUpdate(List<T> itemsList, int batchSize);
}
