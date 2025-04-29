package com.example.cloudBalance.service;

import com.example.cloudBalance.dto.UsageAmountResult;
import com.example.cloudBalance.entity.SnowflakeColumns;
import com.example.cloudBalance.repository.SnowflakeColumnRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class SnowflakeColumnService {

    private final SnowflakeColumnRepository repository;
    private final JdbcTemplate snowflakeJdbcTemplate;

    public SnowflakeColumnService(SnowflakeColumnRepository repository,
                                  @Qualifier("snowflakeJdbcTemplate") JdbcTemplate snowflakeJdbcTemplate) {
        this.repository = repository;
        this.snowflakeJdbcTemplate = snowflakeJdbcTemplate;

    }

    // Method to get all display names
    public List<String> getAllDisplayNames() {
        return repository.findAllDisplayNames();
    }

    // Method to get distinct data for a given displayName
    public List<Object> getDataByDisplayName(String displayName) {
        Optional<SnowflakeColumns> optionalColumn = repository.findByDisplayName(displayName);
        if (optionalColumn.isEmpty()) {
            throw new IllegalArgumentException("Invalid display name: " + displayName);
        }

        // Get the actual name of the column from the SnowflakeColumns entity
        String actualName = optionalColumn.get().getActualName();

        String query = String.format(
                "SELECT DISTINCT \"%s\" FROM cost_explorer WHERE \"%s\" IS NOT NULL LIMIT 1000",
                actualName, actualName
        );


        return snowflakeJdbcTemplate.queryForList(query, Object.class);
    }

    public List<UsageAmountResult> getUsageAmountGroupedBy(
            String displayName,
            String monthYear,
            String linkedAccountId,
            List<String> selectedFilters,
            Map<String, List<String>> filterValues
    ) {
        Optional<SnowflakeColumns> optionalGroupByColumn = repository.findByDisplayName(displayName);
        if (optionalGroupByColumn.isEmpty()) {
            throw new IllegalArgumentException("Invalid display name: " + displayName);
        }

        String groupByColumn = optionalGroupByColumn.get().getActualName();

        YearMonth yearMonth = YearMonth.parse(monthYear);
        int month = yearMonth.getMonthValue();
        int year = yearMonth.getYear();

        StringBuilder query = new StringBuilder(String.format(
                "SELECT " +
                        "\"%s\", " +
                        "CONCAT(MYCLOUD_STARTYEAR, '-', LPAD(MYCLOUD_STARTMONTH, 2, '0')) AS month_year, " +
                        "SUM(LINEITEM_USAGEAMOUNT) AS total_usage_amount " +
                        "FROM cost_explorer WHERE ",
                groupByColumn
        ));
        List<Object> params = new ArrayList<>();

        // Always required filters
        query.append("MYCLOUD_STARTMONTH = ? AND MYCLOUD_STARTYEAR = ? AND LINKEDACCOUNTID = ? ");
        params.add(month);
        params.add(year);
        params.add(linkedAccountId);

        if (selectedFilters != null && !selectedFilters.isEmpty()) {
            for (String filterDisplayName : selectedFilters) {
                Optional<SnowflakeColumns> optionalFilterColumn = repository.findByDisplayName(filterDisplayName);
                if (optionalFilterColumn.isEmpty()) {
                    throw new IllegalArgumentException("Invalid filter display name: " + filterDisplayName);
                }

                String filterActualName = optionalFilterColumn.get().getActualName();
                List<String> values = filterValues.get(filterDisplayName);

                if (values != null && !values.isEmpty()) {
                    query.append("AND (");
                    for (int i = 0; i < values.size(); i++) {
                        query.append("\"").append(filterActualName).append("\" = ? ");
                        params.add(values.get(i));
                        if (i < values.size() - 1) {
                            query.append("OR ");
                        }
                    }
                    query.append(") ");
                }
            }
        }
        // Important: Group by both your selected field AND month+year
        query.append(String.format("GROUP BY \"%s\", MYCLOUD_STARTYEAR, MYCLOUD_STARTMONTH", groupByColumn));
        System.out.println("Generated SQL Query: " + query.toString());

        return snowflakeJdbcTemplate.query(query.toString(), params.toArray(), (rs, rowNum) -> {
            UsageAmountResult result = new UsageAmountResult();
            result.setGroupByValue(rs.getString(1));     // your selected group
            result.setMonthYear(rs.getString(2));         // newly added monthYear
            result.setTotalUsageAmount(rs.getDouble(3));
            return result;
        });
    }
}
