import React, { useEffect, useState } from "react";
import {
  Box,
  CircularProgress,
  Tab,
  Tabs,
  Select,
  MenuItem,
  FormControl,
  InputLabel,
  Paper,
} from "@mui/material";

import ResourceTable from "./ResourceTable";
import api from "../config/AxiosConfig";
import Sidebar from "../utils/Sidebar";
import { SidebarItems } from "../utils/SideBarItems";
import Navbar from "../utils/Navbar";
import Footer from "../utils/Footer";
import { useSelector } from "react-redux";
import { toast } from "react-toastify";

const Aws = () => {
  const [accounts, setAccounts] = useState([]);
  const [selectedAccount, setSelectedAccount] = useState(null);
  const [selectedService, setSelectedService] = useState(null);
  const [resourceData, setResourceData] = useState([]);
  const [loading, setLoading] = useState(false);
  const user = useSelector((state) => state.user);

  useEffect(() => {
    const fetchAccounts = async () => {
      try {
        let res;
        if (user?.role === "ADMIN" || user?.role === "READ_ONLY") {
          res = await api.get("/accounts");
        } else if (user?.role === "CUSTOMER" && user?.email) {
          res = await api.get(`/users/${user.email}`);
        }

        if (res?.data?.data?.length > 0) {
          const firstAccount = res.data.data[0];
          setAccounts(res.data.data);
          setSelectedAccount(firstAccount);
          setSelectedService("EC2");
        }
      } catch (err) {
        toast.error("Failed to load accounts");
      }
    };

    if (user?.role) {
      fetchAccounts();
    }
  }, [user]);

  useEffect(() => {
    if (selectedAccount && selectedService) {
      fetchResourceData(selectedService, selectedAccount.accountNumber);
    }
  }, [selectedService, selectedAccount]);

  const fetchResourceData = async (service, accountNumber) => {
    let url = "";
    switch (service) {
      case "EC2":
        url = `/ec2/instances/${accountNumber}`;
        break;
      case "RDS":
        url = `/rds/instances/${accountNumber}`;
        break;
      case "ASG":
        url = `/asg/instances/${accountNumber}`;
        break;
      default:
        return;
    }

    try {
      setLoading(true);
      const res = await api.get(url);
      setResourceData(res.data.data);
      if (res.data.data.length === 0) {
        toast.info(`No ${service} data found for account ${accountNumber}`);
      } else {
        toast.success(`${service} data fetched successfully`);
      }
    } catch (err) {
      toast.error(`Failed to fetch ${service} data`, err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <Box sx={{ backgroundColor: "#f8fafc", p: 2, borderRadius: 2 }}>
      <Navbar />
      <Box display="flex">
        <Sidebar user={user} sidebarItems={SidebarItems} />
        <Box flex={1} p={3}>
          <Paper elevation={3} sx={{ p: 2, mb: 2 }}>
            <Box
              display="flex"
              justifyContent="space-between"
              alignItems="center"
            >
              <Tabs
                value={selectedService}
                onChange={(e, newValue) => setSelectedService(newValue)}
              >
                <Tab label="EC2" value="EC2" />
                <Tab label="RDS" value="RDS" />
                <Tab label="ASG" value="ASG" />
              </Tabs>

              <FormControl
                variant="outlined"
                size="small"
                sx={{ minWidth: 200 }}
              >
                <InputLabel>Select Account</InputLabel>
                <Select
                  value={selectedAccount?.accountNumber || ""}
                  onChange={(e) =>
                    setSelectedAccount(
                      accounts.find(
                        (acc) => acc.accountNumber === e.target.value
                      )
                    )
                  }
                  label="Select Account"
                >
                  {accounts.map((acc) => (
                    <MenuItem key={acc.accountNumber} value={acc.accountNumber}>
                      {acc.accountName}
                    </MenuItem>
                  ))}
                </Select>
              </FormControl>
            </Box>
          </Paper>

          {loading ? (
            <Box display="flex" justifyContent="center" mt={4}>
              <CircularProgress />
            </Box>
          ) : (
            <ResourceTable data={resourceData} />
          )}
        </Box>
      </Box>
      <Footer />
    </Box>
  );
};

export default Aws;
