import api from "../config/AxiosConfig";
import { toast } from "react-toastify";
import { useSelector } from "react-redux";
import { useEffect, useState } from "react";

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

export const useFetchAccounts = () => {
  const user = useSelector((state) => state.user);
  const [accounts, setAccounts] = useState([]);

  useEffect(() => {
    const fetchAccounts = async () => {
      try {
        if (user?.role === "ADMIN" || user?.role === "READ_ONLY") {
          const res = await api.get("/accounts");
          setAccounts(res.data.data);
        } else if (user?.role === "CUSTOMER" && user?.email && user?.id) {
          const res = await api.get(`/users/${user.id}`);
          setAccounts(res.data.data);
        }
      } catch (err) {
        const msg = err.response?.data?.message || "Failed to load accounts.";
        const code = err.response?.status;
        toast.error(`Error ${code || ""}: ${msg}`);
        setAccounts([]);
      }
    };

    if (user?.role) fetchAccounts();
  }, [user]);

  return { accounts };
};

export const fetchGroupByOptions = async () => {
  try {
    const res = await api.get("/snowflake-columns/display-names");
    return res.data.data;
  } catch (err) {
    const msg =
      err.response?.data?.message || "Failed to load group by options.";
    const code = err.response?.status;
    toast.error(`Error ${code || ""}: ${msg}`);
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

    const groupedByMonth = {};
    rawData.forEach((item) => {
      const month = item.monthYear || monthYear;
      if (!groupedByMonth[month]) groupedByMonth[month] = [];
      groupedByMonth[month].push(item);
    });

    const finalData = [];

    for (const month in groupedByMonth) {
      const sorted = groupedByMonth[month].sort(
        (a, b) => b.totalUsageAmount - a.totalUsageAmount
      );
      const top5 = sorted.slice(0, 5);
      const others = sorted.slice(5);
      const othersAmount = others.reduce((sum, item) => {
        return (sum += item.totalUsageAmount);
      }, 0);

      console.log("othersAmount", othersAmount);

      finalData.push(
        ...top5,
        ...(others.length > 0
          ? [
              {
                groupByValue: "others",
                totalUsageAmount: othersAmount,
                monthYear: month,
              },
            ]
          : [])
      );
    }

    return finalData;
  } catch (err) {
    const msg = err.response?.data?.message || "Failed to load usage data.";
    const code = err.response?.status;
    toast.error(`Error ${code || ""}: ${msg}`);
    return [];
  }
};
