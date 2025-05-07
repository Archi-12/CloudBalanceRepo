import React from "react";
import "./UserManagement.css";
import { FaPencilAlt } from "react-icons/fa";

export default function UserTable({ users, onEdit }) {
  const formatDate = (isoString) => {
    const date = new Date(isoString);
    return date.toLocaleString();
  };

  return (
    <div className="user-table-container">
      <div className="user-table-header">
        <span className="active-tab">Active ({users.length})</span>
        <span className="all-tab">All</span>
      </div>

      <table className="user-table">
        <thead>
          <tr>
            <th>Id</th>
            <th>Name</th>
            <th>Email ID</th>
            <th>Roles</th>
            <th>Last Login</th>
            <th>Action</th>
          </tr>
        </thead>
        <tbody>
          {users.map((u, index) => {
            const [Name] = (u.username || "").split(" ");

            return (
              <tr key={index}>
                <td>{u.id}</td>
                <td>{Name}</td>
                <td>{u.email}</td>
                <td>{u.roles?.name || "N/A"}</td>
                <td>{u.lastLoginAt ? formatDate(u.lastLoginAt) : "--"}</td>
                <td>
                  <FaPencilAlt
                    className="edit-icon"
                    style={{ cursor: "pointer" }}
                    onClick={() => onEdit(u)}
                  />
                </td>
              </tr>
            );
          })}
        </tbody>
      </table>
    </div>
  );
}
