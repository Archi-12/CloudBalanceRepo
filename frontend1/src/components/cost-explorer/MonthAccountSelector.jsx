import React from "react";
import { FormControl, InputLabel, Select, MenuItem } from "@mui/material";

const monthsList = [
  "Jan",
  "Feb",
  "Mar",
  "Apr",
  "May",
  "Jun",
  "Jul",
  "Aug",
  "Sep",
  "Oct",
  "Nov",
  "Dec",
];
const yearsList = ["2023", "2024", "2025"];
const monthYearOptions = yearsList.flatMap((year) =>
  monthsList.map((month) => `${month} ${year}`)
);

const MonthAccountSelector = ({
  startMonth,
  setStartMonth,
  selectedAccount,
  setSelectedAccount,
  accounts,
}) => (
  <>
    <FormControl size="small">
      <InputLabel id="start-month-label">Start Date</InputLabel>
      <Select
        labelId="start-month-label"
        value={startMonth}
        label="Start Date"
        onChange={(e) => setStartMonth(e.target.value)}
        sx={{ width: 150 }}
      >
        {monthYearOptions.map((option) => (
          <MenuItem key={option} value={option}>
            {option}
          </MenuItem>
        ))}
      </Select>
    </FormControl>

    <FormControl size="small">
      <InputLabel id="account-select-label">Select Account</InputLabel>
      <Select
        labelId="account-select-label"
        value={selectedAccount?.accountNumber || ""}
        label="Select Account"
        onChange={(e) =>
          setSelectedAccount(
            accounts.find((acc) => acc.accountNumber === e.target.value)
          )
        }
        sx={{ width: 220 }}
      >
        {accounts.map((acc) => (
          <MenuItem key={acc.accountNumber} value={acc.accountNumber}>
            {acc.accountName}
          </MenuItem>
        ))}
      </Select>
    </FormControl>
  </>
);

export default MonthAccountSelector;
