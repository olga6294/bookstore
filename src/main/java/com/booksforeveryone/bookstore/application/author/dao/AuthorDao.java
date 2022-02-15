package com.booksforeveryone.bookstore.application.author.dao;

import io.vavr.collection.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AuthorDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<Integer> findAllAuthorIds() {
        String query = "SELECT id FROM Author";

        java.util.List<Integer> authorIds =
                namedParameterJdbcTemplate.query(query, (resultSet, rowNo) -> resultSet.getInt("id"));

        return List.ofAll(authorIds);
    }

}
