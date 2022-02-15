package com.booksforeveryone.bookstore.application.book.dao;

import com.google.common.collect.Lists;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;

@Repository
public class MergableNamedParameterJdbcTemplate<T> extends NamedParameterJdbcTemplate {

    private static final Integer MAX_SUBLIST_SIZE = 5;

    public MergableNamedParameterJdbcTemplate(DataSource dataSource) {
        super(dataSource);
    }

    public List<T> query(String sql,
                  String mergeableParamName,
                  List<Integer> mergeableParams,
                  MapSqlParameterSource params,
                  int offset,
                  int pageSize,
                  Function<T, Integer> compareFunction,
                  RowMapper<T> rowMapper) throws DataAccessException {
        List<List<Integer>> dividedParams = divideParams(mergeableParams);

        List<List<T>> resultsList = dividedParams
                .stream()
                .map(paramsList -> {
                    MapSqlParameterSource queryParams = new MapSqlParameterSource();
                    queryParams.addValues(params.getValues());
                    queryParams.addValue(mergeableParamName, paramsList);
                    return super.query(sql, queryParams, rowMapper);
                })
                .collect(Collectors.toList());

        List<T> sorted = merge(resultsList)
                .stream()
                .sorted(Comparator.comparing(compareFunction))
                .collect(Collectors.toList());

        if (!sorted.isEmpty()) {
            return sorted.subList(offset, sorted.size()).size() > pageSize ?
                    sorted.subList(offset, offset + pageSize) : sorted.subList(offset, sorted.size());
        }
        return sorted;
    }

    private List<List<Integer>> divideParams(List<Integer> params) {
        return Lists.partition(params, MAX_SUBLIST_SIZE);
    }

    private List<T> merge(List<List<T>> resultLists) {
        return resultLists
                .stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }
}


