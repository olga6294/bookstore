Filtering database records by large number of
parameters while maintaining pagination functionality in Spring Framework

USE CASE

A while ago I faced a problem of filtering database records by thousands of parameters
(much more than a single database call can handle) in a web application where pagination was already implemented.

When implementing pagination on backend's side the first approach
is to sort database records in an sql query and distinguish only the part we want to retrieve:

```

SELECT Book.id, Book.title, Author.firstname, Author.lastname FROM Book
INNER JOIN Author ON Book.authorId = Author.id
WHERE Author.id IN (:authorIds)
ORDER BY Book.id OFFSET 5 LIMIT 10 ROWS ONLY

```

Since database records are not sorted in any order we need to use ORDER BY clause to be able
to exclude given number of records and retrieve only as many records as we need.
In the example above we also filter records by autor ids provided by the end user of the application.
That solution works perfectly fine but what happens when user provides much more parameters than
a single database query can handle (for example on MSSQL we can only filter by 2100 parameters)?

SOLUTION

In the proposal of solution of the problem we create a reusable generic class specifically for handling
such cases. We divide list of parameters in sublists, each X elements max
(this is where guava library comes in handy, we use Lists.partition method) ...

```
    private static final Integer MAX_SUBLIST_SIZE = 5;

    private List<List<Integer>> divideParams(List<Integer> params) {
        return Lists.partition(params, MAX_SUBLIST_SIZE);
    }

    private List<T> merge(List<List<T>> resultLists) {
        return resultLists
                .stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

```

... and run database query for each sublist.

```
public List<T> query(String sql,
String mergeableParamName,
List<Integer> mergeableParams,
MapSqlParameterSource params,
int offset,
int pageSize,
Function<T, Comparable> compareFunction,
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

                ...
    }
```

The result is not paginated of course. In order to keep pagination functionality work
as it does in the 'traditional' approach, mentioned in the previous section of this article, we need to
concatenate the results of all database queries, sort it and then cut only the elements we want to return.

```
...
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

```

We sort entities returned from database only after concatenation the resuls from each database call.
We cannot sort each database result list and then concatenate them all together because the result
of such operation will not be sorted in most cases. The last step is cutting only the elements we want
to return from the backend.

Now it's time for testing proposed solution.
The complete code of example project used in this article is available in: https://github.com/olga6294/bookstore
(for the purpose of this article
maximum sublist size was set to 5 in the source code). Testing the solution comes to three steps:
1. Fetching the code from repository
2. Running app's main method in BookstoreApplication class
3. Hitting the following endpoint with appropriate parameters:

```
GET http://localhost:8080/books/findAll?authorIds=1,2,3&page=1&pageSize=10
```



