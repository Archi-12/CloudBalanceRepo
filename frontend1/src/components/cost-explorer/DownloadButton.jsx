import React from "react";
import { Button } from "@mui/material";
import { saveAs } from "file-saver";
import * as ExcelJS from "exceljs";
import { FaFileExcel } from "react-icons/fa";
import { toast } from "react-toastify";

const DownloadButton = ({ usageData = [] }) => {
  const handleDownload = async () => {
    if (!usageData.length) {
      toast.info("No data available for download.");
      return;
    }

    const workbook = new ExcelJS.Workbook();
    const worksheet = workbook.addWorksheet("Usage Data");

    worksheet.addRow(["Service", "Amount (USD)"]);

    usageData.forEach((item) => {
      worksheet.addRow([
        item.groupByValue || "Others",
        item.totalUsageAmount ? item.totalUsageAmount.toFixed(2) : "0",
      ]);
    });

    worksheet.getRow(1).font = { bold: true };

    const buffer = await workbook.xlsx.writeBuffer();
    const blob = new Blob([buffer], {
      type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
    });
    saveAs(blob, "usage-data.xlsx");
  };

  return (
    <Button
      variant="contained"
      color="primary"
      onClick={handleDownload}
      style={{ marginBottom: "16px" }}
    >
      <FaFileExcel />
    </Button>
  );
};

export default DownloadButton;
