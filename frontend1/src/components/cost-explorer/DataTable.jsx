import React from "react";
import {
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
} from "@mui/material";
import DownloadButton from "./DownloadButton"; // 👈 Import the button

const DataTable = ({ usageData = [] }) => {
  console.log("DataTable usageData:", usageData);

  if (!usageData.length) {
    return <div>No table data available.</div>;
  }

  return (
    <div>
      <DownloadButton usageData={usageData} /> {/* 👈 Button added here */}
      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>
                <strong>Service</strong>
              </TableCell>
              <TableCell>
                <strong>Amount (USD)</strong>
              </TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {usageData.map((item, idx) => (
              <TableRow key={idx}>
                <TableCell>{item.groupByValue || "Others"}</TableCell>
                <TableCell>
                  {item.totalUsageAmount ? item.totalUsageAmount.toFixed(2) : 0}
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    </div>
  );
};
export default DataTable;
