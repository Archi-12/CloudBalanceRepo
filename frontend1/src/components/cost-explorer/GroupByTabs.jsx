import React, { useState } from "react";
import { Box, Tabs, Tab, Button, Menu, MenuItem } from "@mui/material";
import MoreHorizIcon from "@mui/icons-material/MoreHoriz";

const GroupByTabs = ({
  groupByOptions,
  selectedGroupBy,
  setSelectedGroupBy,
}) => {
  const [moreAnchorEl, setMoreAnchorEl] = useState(null);
  const primaryTabsCount = 6;

  const handleTabChange = (_event, newValue) => {
    setSelectedGroupBy(newValue);
  };

  const handleMoreClick = (event) => {
    setMoreAnchorEl(event.currentTarget);
  };

  const handleMoreClose = (value) => {
    if (value) setSelectedGroupBy(value);
    setMoreAnchorEl(null);
  };

  return (
    <Box display="flex" alignItems="center" gap={2}>
      <Tabs
        value={
          groupByOptions.slice(0, primaryTabsCount).includes(selectedGroupBy)
            ? selectedGroupBy
            : false
        }
        onChange={handleTabChange}
        variant="scrollable"
        scrollButtons="auto"
      >
        {groupByOptions.slice(0, primaryTabsCount).map((label) => (
          <Tab key={label} label={label} value={label} />
        ))}
      </Tabs>
      {groupByOptions.length > primaryTabsCount && (
        <>
          <Button onClick={handleMoreClick}>
            <MoreHorizIcon />
          </Button>
          <Menu
            anchorEl={moreAnchorEl}
            open={Boolean(moreAnchorEl)}
            onClose={() => handleMoreClose()}
          >
            {groupByOptions.slice(primaryTabsCount).map((label) => (
              <MenuItem key={label} onClick={() => handleMoreClose(label)}>
                {label}
              </MenuItem>
            ))}
          </Menu>
        </>
      )}
    </Box>
  );
};

export default GroupByTabs;
