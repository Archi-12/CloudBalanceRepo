import React, { useEffect, useState } from "react";
import { Box, Button, CircularProgress, Typography } from "@mui/material";
import FilterListIcon from "@mui/icons-material/FilterList";
import Navbar from "../utils/Navbar";
import Sidebar from "../utils/Sidebar";
import { SidebarItems } from "../utils/SideBarItems";
import { useSelector } from "react-redux";
import { fetchAccounts, fetchGroupByOptions, fetchUsageData } from "./usageApi";
import ChartDisplay from "./ChartDisplay";
import LineChartDisplay from "/home/archi/Cloudbalance/frontend/src/components/cost-explorer/ LineChartDisplay.jsx";
import DataTable from "./DataTable";
import FilterDrawer from "./FilterDrawer";
import GroupByTabs from "./GroupByTabs";
import MonthAccountSelector from "./MonthAccountSelector";

const GroupByWithSidebarFilter = () => {
  const user1 = useSelector((state) => state.user);

  const [groupByOptions, setGroupByOptions] = useState([]);
  const [selectedGroupBy, setSelectedGroupBy] = useState("Service");
  const [filterDrawerOpen, setFilterDrawerOpen] = useState(false);
  const [startMonth, setStartMonth] = useState("");
  const [accounts, setAccounts] = useState([]);
  const [selectedAccount, setSelectedAccount] = useState(null);
  const [usageData, setUsageData] = useState([]);
  const [selectedFilters, setSelectedFilters] = useState({});
  const [fetchedData, setFetchedData] = useState({});
  const [expanded, setExpanded] = useState(false);
  const [loading, setLoading] = useState(false);
  const [chartLoading, setChartLoading] = useState(false);
  const [initializing, setInitializing] = useState(true);
  const [shouldAutoFetch, setShouldAutoFetch] = useState(false);

  // First useEffect: load accounts and groupBy
  useEffect(() => {
    const init = async () => {
      const accountsData = await fetchAccounts(user1);
      const options = await fetchGroupByOptions();
      setAccounts(accountsData);
      setGroupByOptions(options);

      if (accountsData.length > 0) {
        setSelectedAccount(accountsData[0]);
        setStartMonth("Apr 2025");
        console.log("Start month: ");
        setSelectedGroupBy("Service");
        setShouldAutoFetch(true);
      }
      setInitializing(false);
    };
    init();
  }, [user1]);

  // Second useEffect: when selectedAccount + startMonth + groupBy are ready
  useEffect(() => {
    const autoFetchUsageData = async () => {
      if (shouldAutoFetch && selectedAccount && startMonth && selectedGroupBy) {
        setChartLoading(true);
        const data = await fetchUsageData(
          selectedGroupBy,
          startMonth,
          selectedAccount,
          {}
        );
        setUsageData(data);
        setChartLoading(false);
        setShouldAutoFetch(false);
      }
    };
    autoFetchUsageData();
  }, [shouldAutoFetch, selectedAccount, startMonth, selectedGroupBy]);

  const handleSubmitUsageData = async () => {
    if (!selectedAccount || !startMonth) {
      console.error("Missing fields");
      return;
    }
    setChartLoading(true);
    const data = await fetchUsageData(
      selectedGroupBy,
      startMonth,
      selectedAccount,
      selectedFilters
    );
    setUsageData(data);
    setChartLoading(false);
  };

  return (
    <Box sx={{ backgroundColor: "#f8fafc", p: 2, borderRadius: 2 }}>
      <Navbar />
      <Box display="flex" height="calc(100vh - 64px)">
        {" "}
        <Box
          width="250px"
          bgcolor="#f5f5f5"
          height="100vh"
          position="sticky"
          top="64px"
        >
          <Sidebar user={user1} sidebarItems={SidebarItems} />
        </Box>
        <Box flex={1} overflow="auto" p={3}>
          <Box
            display="flex"
            justifyContent="space-between"
            alignItems="center"
            mb={3}
          >
            <GroupByTabs
              groupByOptions={groupByOptions}
              selectedGroupBy={selectedGroupBy}
              setSelectedGroupBy={setSelectedGroupBy}
            />
            <Button
              variant="outlined"
              startIcon={<FilterListIcon />}
              onClick={() => setFilterDrawerOpen(!filterDrawerOpen)}
            >
              {filterDrawerOpen ? "Close Filters" : "Filter"}
            </Button>
          </Box>

          <Box display="flex" gap={2} flexWrap="wrap" mb={3}>
            <MonthAccountSelector
              startMonth={startMonth}
              setStartMonth={setStartMonth}
              selectedAccount={selectedAccount}
              setSelectedAccount={setSelectedAccount}
              accounts={accounts}
            />
            <Button
              variant="contained"
              color="primary"
              onClick={handleSubmitUsageData}
            >
              Get Usage Data
            </Button>
          </Box>

          {chartLoading ? (
            <CircularProgress />
          ) : usageData.length > 0 ? (
            <>
              <ChartDisplay
                usageData={usageData}
                selectedGroupBy={selectedGroupBy}
              />
              <LineChartDisplay
                usageData={usageData}
                selectedGroupBy={selectedGroupBy}
              />
              <DataTable
                usageData={usageData}
                selectedGroupBy={selectedGroupBy}
              />
            </>
          ) : (
            <Typography variant="body2">No data to show.</Typography>
          )}
        </Box>
      </Box>

      {/* Filter Drawer */}
      <FilterDrawer
        open={filterDrawerOpen}
        onClose={() => setFilterDrawerOpen(false)}
        groupByOptions={groupByOptions}
        expanded={expanded}
        setExpanded={setExpanded}
        fetchedData={fetchedData}
        setFetchedData={setFetchedData}
        selectedFilters={selectedFilters}
        setSelectedFilters={setSelectedFilters}
        loading={loading}
        setLoading={setLoading}
      />
    </Box>
  );
};

export default GroupByWithSidebarFilter;
