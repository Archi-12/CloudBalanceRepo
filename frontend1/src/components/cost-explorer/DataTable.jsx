import React, { useState, useMemo } from "react";
import {
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
  IconButton,
  Menu,
  MenuItem,
  Checkbox,
  ListItemText,
  Box,
  Typography,
} from "@mui/material";
import FilterListIcon from "@mui/icons-material/FilterList";
import DownloadButton from "./DownloadButton";

const FilterableDataTable = ({ usageData = [] }) => {
  const [anchorEls, setAnchorEls] = useState({});
  const [filters, setFilters] = useState({});

  const headers = usageData.length ? Object.keys(usageData[0]) : [];

  // Get unique values per column
  const uniqueColumnValues = useMemo(() => {
    const result = {};
    headers.forEach((header) => {
      result[header] = [...new Set(usageData.map((item) => item[header]))];
    });
    return result;
  }, [usageData]);

  // Apply all filters to the data
  const filteredData = useMemo(() => {
    return usageData.filter((row) =>
      Object.entries(filters).every(([column, selectedValues]) =>
        selectedValues.length > 0 ? selectedValues.includes(row[column]) : true
      )
    );
  }, [filters, usageData]);

  // Handle filter open/close
  const handleFilterIconClick = (event, header) => {
    setAnchorEls((prev) => ({ ...prev, [header]: event.currentTarget }));
  };

  const handleClose = (header) => {
    setAnchorEls((prev) => ({ ...prev, [header]: null }));
  };

  // Handle selection inside filter dropdown
  const handleFilterChange = (header, value) => {
    const current = filters[header] || [];
    const newValues = current.includes(value)
      ? current.filter((v) => v !== value)
      : [...current, value];
    setFilters({ ...filters, [header]: newValues });
  };

  if (!usageData.length) {
    return (
      <Box mt={5} textAlign="center">
        <Typography variant="h6" color="text.secondary">
          No table data available.
        </Typography>
      </Box>
    );
  }

  return (
    <Box p={3}>
      <DownloadButton usageData={filteredData} />
      <TableContainer component={Paper} elevation={4}>
        <Table>
          <TableHead sx={{ backgroundColor: "#f9f9f9" }}>
            <TableRow>
              {headers.map((header) => (
                <TableCell key={header} sx={{ fontWeight: "bold" }}>
                  <Box display="flex" alignItems="center">
                    {header}
                    <IconButton
                      size="small"
                      onClick={(e) => handleFilterIconClick(e, header)}
                    >
                      <FilterListIcon fontSize="small" />
                    </IconButton>

                    {/* Filter Dropdown */}
                    <Menu
                      anchorEl={anchorEls[header]}
                      open={Boolean(anchorEls[header])}
                      onClose={() => handleClose(header)}
                    >
                      {uniqueColumnValues[header].map((value) => (
                        <MenuItem
                          key={value}
                          onClick={() => handleFilterChange(header, value)}
                        >
                          <Checkbox
                            checked={filters[header]?.includes(value) || false}
                          />
                          <ListItemText primary={value || "Others"} />
                        </MenuItem>
                      ))}
                    </Menu>
                  </Box>
                </TableCell>
              ))}
            </TableRow>
          </TableHead>
          <TableBody>
            {filteredData.map((row, idx) => (
              <TableRow key={idx}>
                {headers.map((header) => (
                  <TableCell key={header}>
                    {typeof row[header] === "number"
                      ? row[header].toFixed(2)
                      : row[header] || "Others"}
                  </TableCell>
                ))}
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    </Box>
  );
};

export default FilterableDataTable;
