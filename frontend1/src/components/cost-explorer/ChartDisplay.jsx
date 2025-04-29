import React from "react";
import FusionCharts from "fusioncharts";
import Charts from "fusioncharts/fusioncharts.charts";
import ReactFC from "react-fusioncharts";
import FusionTheme from "fusioncharts/themes/fusioncharts.theme.fusion";

// Connect FusionCharts modules
ReactFC.fcRoot(FusionCharts, Charts, FusionTheme);

const ChartDisplay = ({ usageData = [] }) => {
  console.log("Received usageData:", usageData);

  if (!usageData || usageData.length === 0) {
    return (
      <div className="bg-white shadow p-4 mb-4 rounded-lg text-center text-gray-500">
        <p>No data available to display.</p>
      </div>
    );
  }

  // 1. Organize data for multi-series chart
  const categoriesSet = new Set();
  const datasetMap = {};

  usageData.forEach((item) => {
    const date = item.monthYear || "Others";

    const service = item.groupByValue || "Others";

    const amount = item.totalUsageAmount || "Others";

    categoriesSet.add(date);

    if (!datasetMap[service]) {
      datasetMap[service] = {}; // Initialize service
    }

    datasetMap[service][date] = amount;
  });

  // Prepare categories (x-axis labels)
  const categories = Array.from(categoriesSet).sort();

  // Prepare datasets (bars per service)
  const dataset = Object.keys(datasetMap).map((serviceName) => ({
    seriesname: serviceName,
    data: categories.map((date) => ({
      value: datasetMap[serviceName][date] || 0,
    })),
  }));

  const chartConfigs = {
    type: "mscolumn2d", // multi-series column chart
    width: "100%",
    height: "400",
    dataFormat: "json",
    dataSource: {
      chart: {
        caption: "Monthly Cost Overview",
        xAxisName: "Month-Year",
        yAxisName: "Total Cost (USD)",
        theme: "fusion",
        formatNumberScale: "0",
        rotateValues: "1",
        drawCrossLine: "1",
        animation: "1",
      },
      categories: [
        {
          category: categories.map((date) => ({ label: date })),
        },
      ],
      dataset: dataset,
    },
  };

  return (
    <div className="bg-white shadow-md p-6 mb-6 rounded-lg">
      <ReactFC {...chartConfigs} />
    </div>
  );
};

export default ChartDisplay;
