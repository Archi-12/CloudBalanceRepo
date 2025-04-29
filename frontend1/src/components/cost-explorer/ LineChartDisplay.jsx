import React from "react";
import FusionCharts from "fusioncharts";
import Charts from "fusioncharts/fusioncharts.charts";
import ReactFC from "react-fusioncharts";
import FusionTheme from "fusioncharts/themes/fusioncharts.theme.fusion";

ReactFC.fcRoot(FusionCharts, Charts, FusionTheme);

const LineChartDisplay = ({ usageData = [] }) => {
  if (!usageData.length) return null;

  // 1. Organize data
  const categoriesSet = new Set();
  const datasetMap = {};

  usageData.forEach((item) => {
    const date = item.monthYear || "Others";
    const service = item.groupByValue || "Others";
    const amount = item.totalUsageAmount || 0;

    categoriesSet.add(date);

    if (!datasetMap[service]) {
      datasetMap[service] = {}; // Initialize service
    }

    datasetMap[service][date] = amount;
  });

  const categories = Array.from(categoriesSet).sort();

  const dataset = Object.keys(datasetMap).map((serviceName) => ({
    seriesname: serviceName,
    data: categories.map((date) => ({
      value: datasetMap[serviceName][date] || 0,
    })),
  }));

  const chartConfigs = {
    type: "msline", // Multi-series line chart
    width: "100%",
    height: "400",
    dataFormat: "json",
    dataSource: {
      chart: {
        xAxisName: "Month-Year",
        yAxisName: "Total Cost (USD)",
        caption: "Line Chart",
        theme: "fusion",
        drawAnchors: "1",
        anchorRadius: "4",
        lineThickness: "2",
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

export default LineChartDisplay;
