import React from "react";
import { FaUser, FaRocket, FaDollarSign, FaAws } from "react-icons/fa";

export const SidebarItems = [
  {
    label: "User Management",
    icon: <FaUser className="icon" />,
    path: "/user-management",
    allowedRoles: ["ADMIN", "READ_ONLY"],
  },
  {
    label: "Onboarding Dashboard",
    icon: <FaRocket className="icon" />,
    path: "/onboarding",
    allowedRoles: ["ADMIN", "READ_ONLY"],
  },
  {
    label: "Cost Explorer",
    icon: <FaDollarSign className="icon" />,
    path: "/cost-explorer",
    allowedRoles: ["ADMIN", "READ_ONLY", "CUSTOMER"],
  },
  {
    label: "AWS Services",
    icon: <FaAws className="icon" />,
    path: "/aws-services",
    allowedRoles: ["ADMIN", "READ_ONLY", "CUSTOMER"],
  },
];
