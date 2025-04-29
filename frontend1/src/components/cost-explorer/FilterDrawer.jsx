import React from "react";
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
} from "@mui/material";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import api from "../config/AxiosConfig";

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
  const handleAccordionChange = (displayName) => async (_event, isExpanded) => {
    setExpanded(isExpanded ? displayName : false);
    if (isExpanded && !fetchedData[displayName]) {
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
        console.error("Error fetching filter data", error);
      } finally {
        setLoading(false);
      }
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

  return (
    <Drawer anchor="right" open={open} onClose={onClose} sx={{ width: 320 }}>
      <Box sx={{ width: 320, p: 2 }}>
        <Typography variant="h6">Filters</Typography>
        {groupByOptions.map((displayName) => (
          <Accordion
            key={displayName}
            expanded={expanded === displayName}
            onChange={handleAccordionChange(displayName)}
          >
            <AccordionSummary expandIcon={<ExpandMoreIcon />}>
              <Typography>{displayName}</Typography>
            </AccordionSummary>
            <AccordionDetails>
              {loading && expanded === displayName ? (
                <CircularProgress size={24} />
              ) : fetchedData[displayName] ? (
                fetchedData[displayName].map((item) => (
                  <FormControlLabel
                    key={item}
                    control={
                      <Checkbox
                        checked={
                          selectedFilters[displayName]?.includes(item) || false
                        }
                        onChange={() => handleCheckboxChange(displayName, item)}
                      />
                    }
                    label={item}
                  />
                ))
              ) : (
                <Typography color="text.secondary">No data loaded.</Typography>
              )}
            </AccordionDetails>
          </Accordion>
        ))}
      </Box>
    </Drawer>
  );
};

export default FilterDrawer;
