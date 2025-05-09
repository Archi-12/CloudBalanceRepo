import React, { useState, useEffect, useRef } from "react";
import api from "../config/AxiosConfig";
import { toast } from "react-toastify";
import { useDispatch } from "react-redux";
import { switchUser } from "../redux/userSlice";

export default function SwitchUserDropdown({ onClose }) {
  const [userList, setUserList] = useState([]);
  const dropdownRef = useRef(null);
  const dispatch = useDispatch();

  useEffect(() => {
    const fetchUsers = async () => {
      try {
        const res = await api.get("/users/customers");
        setUserList(res.data.data);
      } catch (err) {
        toast.error("Failed to fetch users");
        console.error("Error:", err.response || err);
      }
    };
    fetchUsers();
  }, []);

  const handleSwitch = (user) => {
    const userWithRole = {
      id: user.id,
      username: user.username,
      email: user.email,
      role: user.roles.name,
    };

    dispatch(switchUser(userWithRole));
    toast.success(`Now impersonating ${user.username}`);
    onClose();
  };

  useEffect(() => {
    const handleClickOutside = (event) => {
      if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
        onClose();
      }
    };
    document.addEventListener("mousedown", handleClickOutside);
    return () => document.removeEventListener("mousedown", handleClickOutside);
  }, [onClose]);

  return (
    <div className="user-dropdown" ref={dropdownRef}>
      <p>
        <strong>Select Customer:</strong>
      </p>
      <ul>
        {userList.map((user) => (
          <li
            key={user.id}
            onClick={() => handleSwitch(user)}
            style={{ cursor: "pointer", padding: "4px 0" }}
          >
            {user.username} ({user.email})
          </li>
        ))}
      </ul>
    </div>
  );
}
