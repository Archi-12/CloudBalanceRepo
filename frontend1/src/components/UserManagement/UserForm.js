import React, { useState, useEffect } from "react";
import "./UserForm.css";
import api from "../config/AxiosConfig";
import { toast } from "react-toastify";
import { useNavigate } from "react-router-dom";
import { FormConfig } from "./FormConfig";
import Navbar from "../utils/Navbar";
import Sidebar from "../utils/Sidebar";
import { SidebarItems } from "../utils/SideBarItems";
import { useSelector } from "react-redux";
import { useLocation } from "react-router-dom";

export default function UserForm({ onSubmit }) {
  const initialFormData = FormConfig.reduce((acc, field) => {
    acc[field.name] = "";
    return acc;
  }, {});
  const [formData, setFormData] = useState(initialFormData);
  const [accounts, setAccounts] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchAccounts = async () => {
      try {
        const res = await api.get("/accounts");
        setAccounts(Array.isArray(res.data.data) ? res.data.data : []);
      } catch (err) {
        console.error("Error fetching accounts:", err);
      }
    };
    fetchAccounts();
  }, []);

  const location = useLocation();
  const editUserData = location.state?.userData || null;

  const [isEditMode, setIsEditMode] = useState(false);

  useEffect(() => {
    if (editUserData) {
      const prefillData = {
        name: editUserData.username,
        email: editUserData.email,
        role: editUserData.roles?.name || "",
        accountNumbers: editUserData.accountNumbers || [],
        password: "", // password blank rehta edit ke time
      };
      setFormData(prefillData);
      setIsEditMode(true);
    }
  }, [editUserData]);

  const handleCancel = () => {
    setFormData(initialFormData);
    navigate("/user-management");
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleCheckboxChange = (e) => {
    const selected = new Set(formData.accountNumbers || []);
    if (e.target.checked) {
      selected.add(e.target.value);
    } else {
      selected.delete(e.target.value);
    }
    setFormData((prev) => ({
      ...prev,
      accountNumbers: Array.from(selected),
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    const roleValue = (formData.role || "").toUpperCase();
    const userPayload = {
      email: formData.email,
      password: formData.password,
      username: formData.name,
      roles: {
        name: roleValue,
      },
      accountNumbers:
        roleValue === "CUSTOMER" ? formData.accountNumbers || [] : [],
    };

    try {
      if (isEditMode) {
        // UPDATE existing user
        await api.put(`/users/${editUserData.email}`, userPayload);
        toast.success("User updated successfully");
      } else {
        // CREATE new user
        await api.post("/users", userPayload);
        toast.success("User created successfully");
      }

      navigate("/user-management");
      onSubmit && onSubmit(userPayload);
    } catch (error) {
      if (error.response?.status === 409) {
        toast.error("User already exists");
      }
      if (error.response?.status === 400) {
        toast.error("Invalid input data");
      }
      toast.error("Error adding/updating user:", error.response?.data.status);
      toast.error("Failed to process user");
    }
  };

  const user = useSelector((state) => state.user);

  return (
    <div className="dashboard-container">
      <Navbar />
      <div className="main-content">
        <Sidebar user={user} sidebarItems={SidebarItems} />
        <form className="user-form" onSubmit={handleSubmit}>
          {FormConfig.map((field, index) => (
            <div key={index} className="form-group">
              <label>
                {field.label} {field.required && "*"}
              </label>
              {field.type === "select" ? (
                <select
                  name={field.name}
                  value={formData[field.name]}
                  onChange={handleChange}
                  required={field.required}
                >
                  <option value="">Select {field.label}</option>
                  {field.options.map((option, i) => (
                    <option key={i} value={option.value}>
                      {option.label}
                    </option>
                  ))}
                </select>
              ) : (
                <input
                  type={field.type}
                  name={field.name}
                  placeholder={field.placeholder}
                  value={formData[field.name]}
                  onChange={handleChange}
                  required={field.required}
                />
              )}
            </div>
          ))}

          {(formData.role || "").toUpperCase() === "CUSTOMER" && (
            <div className="form-group">
              <label>Accounts *</label>
              <div className="checkbox-group">
                {accounts.map((acc) => (
                  <label key={acc.accountNumber} className="checkbox-item">
                    <input
                      type="checkbox"
                      value={acc.accountNumber}
                      checked={formData.accountNumbers?.includes(
                        acc.accountNumber
                      )}
                      onChange={handleCheckboxChange}
                    />
                    <span className="account-label">
                      <strong>{acc.accountName}</strong> ({acc.accountNumber})
                    </span>
                  </label>
                ))}
              </div>
            </div>
          )}

          <div className="form-actions">
            <button type="submit">Add User</button>
            <button type="button" className="cancel-btn" onClick={handleCancel}>
              Cancel
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
