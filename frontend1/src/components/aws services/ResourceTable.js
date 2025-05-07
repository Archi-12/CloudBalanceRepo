import React, { useState, useEffect, useMemo } from "react";
import {
  Table,
  TableHead,
  TableRow,
  TableCell,
  TableBody,
  Paper,
  TableContainer,
  Typography,
  Box,
  IconButton,
  Menu,
  MenuItem,
  Checkbox,
  ListItemText,
} from "@mui/material";
import FilterListIcon from "@mui/icons-material/FilterList";

const ResourceTable = ({ data }) => {
  const [filters, setFilters] = useState({});
  const [anchorEls, setAnchorEls] = useState({});
  const [filteredData, setFilteredData] = useState(data);

  const headers = useMemo(
    () => (data.length ? Object.keys(data[0]) : []),
    [data]
  );

  // Get distinct values for each column
  const uniqueValues = useMemo(() => {
    const values = {};
    headers.forEach((header) => {
      values[header] = [...new Set(data.map((row) => row[header]))];
    });
    return values;
  }, [headers, data]);

  // Apply filters
  useEffect(() => {
    const result = data.filter((row) =>
      Object.entries(filters).every(([key, values]) =>
        values.length ? values.includes(row[key]) : true
      )
    );
    setFilteredData(result);
  }, [filters, data]);

  const handleFilterIconClick = (event, header) => {
    setAnchorEls((prev) => ({ ...prev, [header]: event.currentTarget }));
  };

  const handleClose = (header) => {
    setAnchorEls((prev) => ({ ...prev, [header]: null }));
  };

  const handleFilterChange = (header, value) => {
    const current = filters[header] || [];
    const updated = current.includes(value)
      ? current.filter((v) => v !== value)
      : [...current, value];
    setFilters({ ...filters, [header]: updated });
  };

  if (!data.length)
    return (
      <Box mt={4} textAlign="center">
        <Typography variant="body1">No data available.</Typography>
      </Box>
    );

  return (
    <TableContainer
      component={Paper}
      elevation={4}
      sx={{ mt: 3, maxHeight: "70vh", overflow: "auto" }}
    >
      <Table stickyHeader>
        <TableHead sx={{ backgroundColor: "#f9f9f9" }}>
          <TableRow>
            {headers.map((header) => (
              <TableCell
                key={header}
                sx={{
                  fontWeight: "bold",
                  backgroundColor: "#f0f0f0",
                  color: "#333",
                  position: "relative",
                }}
              >
                <Box
                  display="flex"
                  alignItems="center"
                  justifyContent="space-between"
                >
                  {header}
                  <IconButton
                    size="small"
                    onClick={(e) => handleFilterIconClick(e, header)}
                    sx={{ ml: 1 }}
                  >
                    <FilterListIcon fontSize="small" />
                  </IconButton>

                  {/* Dropdown Menu */}
                  <Menu
                    anchorEl={anchorEls[header]}
                    open={Boolean(anchorEls[header])}
                    onClose={() => handleClose(header)}
                  >
                    {uniqueValues[header]?.map((value) => (
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
            <TableRow key={idx} hover>
              {headers.map((header) => (
                <TableCell key={header} sx={{ fontSize: "0.9rem" }}>
                  {row[header]}
                </TableCell>
              ))}
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </TableContainer>
  );
};

export default ResourceTable;
