import React from "react";
import { NavLink } from "react-router-dom";

export default function Sidebar({ user, sidebarItems }) {
  const filteredItems = sidebarItems.filter((item) =>
    item.allowedRoles.includes(user?.role)
  );

  return (
    <aside className="sidebar">
      <ul>
        {filteredItems.map((item, index) => (
          <li key={index}>
            <NavLink to={item.path} className="sidebar-link">
              {item.icon} {item.label}
            </NavLink>
          </li>
        ))}
      </ul>
    </aside>
  );
}
