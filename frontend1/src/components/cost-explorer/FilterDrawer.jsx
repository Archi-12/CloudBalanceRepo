import React, { useState } from "react";
import {
  Drawer,
  Box,
  Accordion,
  AccordionSummary,
  AccordionDetails,
  Typography,
  Checkbox,
  FormControlLabel,
  CircularProgress,
  Divider,
  TextField,
} from "@mui/material";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import api from "../config/AxiosConfig";
import { toast } from "react-toastify";

const FilterDrawer = ({
  open,
  onClose,
  groupByOptions,
  expanded,
  setExpanded,
  fetchedData,
  setFetchedData,
  selectedFilters,
  setSelectedFilters,
  loading,
  setLoading,
}) => {
  const [searchTerms, setSearchTerms] = useState({});

  const handleAccordionChange = (displayName) => async (_event, isExpanded) => {
    setExpanded(isExpanded ? displayName : false);
    if (isExpanded && !fetchedData[displayName]) {
      await fetchFilterData(displayName);
    }
  };

  const fetchFilterData = async (displayName) => {
    setLoading(true);
    try {
      const response = await api.get(`/snowflake-columns/data`, {
        params: { displayName },
      });
      setFetchedData((prev) => ({
        ...prev,
        [displayName]: response.data.data,
      }));
    } catch (error) {
      const status = error.response?.status;
      const message =
        error.response?.data?.message || "Failed to fetch filter data";
      toast.error(status ? `Error ${status}: ${message}` : "Network error");
    } finally {
      setLoading(false);
    }
  };

  const handleCheckboxChange = (displayName, item) => {
    setSelectedFilters((prev) => {
      const current = prev[displayName] || [];
      const updated = current.includes(item)
        ? current.filter((i) => i !== item)
        : [...current, item];
      return { ...prev, [displayName]: updated };
    });
  };

  const handleSearchChange = (displayName, value) => {
    setSearchTerms((prev) => ({ ...prev, [displayName]: value }));
  };

  return (
    <Drawer anchor="right" open={open} onClose={onClose}>
      <Box sx={{ width: 320, p: 2 }}>
        <Typography variant="h6" gutterBottom>
          Filters
        </Typography>
        <Divider sx={{ mb: 2 }} />
        {groupByOptions.map((displayName) => {
          const searchTerm = searchTerms[displayName] || "";
          const allOptions = fetchedData[displayName] || [];
          const filteredOptions = allOptions.filter((option) =>
            option?.toLowerCase().includes(searchTerm.toLowerCase())
          );

          return (
            <Accordion
              key={displayName}
              expanded={expanded === displayName}
              onChange={handleAccordionChange(displayName)}
              sx={{ mb: 1, borderRadius: 2, boxShadow: 1 }}
            >
              <AccordionSummary expandIcon={<ExpandMoreIcon />}>
                <Typography sx={{ fontWeight: 600 }}>{displayName}</Typography>
              </AccordionSummary>
              <AccordionDetails>
                {loading && expanded === displayName ? (
                  <Box display="flex" justifyContent="center" my={2}>
                    <CircularProgress size={24} />
                  </Box>
                ) : (
                  <>
                    <TextField
                      size="small"
                      placeholder="Search..."
                      fullWidth
                      value={searchTerm}
                      onChange={(e) =>
                        handleSearchChange(displayName, e.target.value)
                      }
                      sx={{ mb: 1 }}
                    />
                    {filteredOptions.length > 0 ? (
                      filteredOptions.map((item) => (
                        <FormControlLabel
                          key={item}
                          control={
                            <Checkbox
                              checked={
                                selectedFilters[displayName]?.includes(item) ||
                                false
                              }
                              onChange={() =>
                                handleCheckboxChange(displayName, item)
                              }
                            />
                          }
                          label={item || "Others"}
                          sx={{ ml: 1 }}
                        />
                      ))
                    ) : (
                      <Typography color="text.secondary" fontStyle="italic">
                        No matching options.
                      </Typography>
                    )}
                  </>
                )}
              </AccordionDetails>
            </Accordion>
          );
        })}
      </Box>
    </Drawer>
  );
};

export default FilterDrawer;
