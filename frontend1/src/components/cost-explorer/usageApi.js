import api from "../config/AxiosConfig";

const monthMap = {
  Jan: "01",
  Feb: "02",
  Mar: "03",
  Apr: "04",
  May: "05",
  Jun: "06",
  Jul: "07",
  Aug: "08",
  Sep: "09",
  Oct: "10",
  Nov: "11",
  Dec: "12",
};

export const fetchAccounts = async (user) => {
  try {
    if (user?.role === "ADMIN" || user?.role === "READ_ONLY") {
      const res = await api.get("/accounts");
      return res.data.data;
    } else if (user?.role === "CUSTOMER" && user?.email) {
      const res = await api.get(`/users/${user.email}`);
      return res.data.data;
    }
  } catch (err) {
    console.error("Failed to load accounts", err);
    return [];
  }
};

export const fetchGroupByOptions = async () => {
  try {
    const res = await api.get("/snowflake-columns/display-names");
    return res.data.data;
  } catch (err) {
    console.error("Failed to load group by options", err);
    return [];
  }
};

export const fetchUsageData = async (
  groupBy,
  startMonth,
  account,
  selectedFilters
) => {
  try {
    const [monthStr, year] = startMonth.split(" ");
    const monthYear = `${year}-${monthMap[monthStr]}`;

    const filtersPayload = {};
    const selectedKeys = [];

    for (const key in selectedFilters) {
      if (selectedFilters[key]?.length > 0) {
        filtersPayload[key.toUpperCase()] = selectedFilters[key];
        selectedKeys.push(key.toUpperCase());
      }
    }

    const res = await api.post("/snowflake-columns/usage", filtersPayload, {
      params: {
        displayName: groupBy,
        monthYear,
        linkedAccountId: account.accountNumber,
        selectedFilters: selectedKeys.join(","),
      },
    });

    const rawData = res.data.data || [];

    // ✅ Group by monthYear
    const groupedByMonth = {};
    rawData.forEach((item) => {
      const month = item.monthYear || monthYear; // fallback if not present
      if (!groupedByMonth[month]) groupedByMonth[month] = [];
      groupedByMonth[month].push(item);
    });

    // ✅ Process each group
    const finalData = [];

    for (const month in groupedByMonth) {
      const sorted = groupedByMonth[month].sort((a, b) => b.amount - a.amount);
      const top5 = sorted.slice(0, 5);
      const others = sorted.slice(5);
      const othersAmount = others.reduce(
        (sum, item) => sum + (item.amount || 0),
        0
      );

      finalData.push(
        ...top5,
        ...(others.length > 0
          ? [{ usage: "Others", amount: othersAmount, monthYear: month }]
          : [])
      );
    }

    return finalData;
  } catch (err) {
    console.error("Failed to load usage data", err);
    return [];
  }
};
