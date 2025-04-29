import React from "react";
import "./AccountForm.css";

const AccountForm = ({ formData, setFormData }) => {
  const handleChange = (e) => {
    setFormData((prev) => ({
      ...prev,
      [e.target.name]: e.target.value,
    }));
  };

  return (
    <div className="account-form">
      <input
        type="text"
        name="arnNumber"
        placeholder="ARN Number"
        required
        value={formData.arnNumber}
        onChange={handleChange}
      />
      <input
        type="text"
        name="accountNumber"
        placeholder="Account Number"
        required
        value={formData.accountNumber}
        onChange={handleChange}
      />
      <input
        type="text"
        name="accountName"
        placeholder="Account Name"
        required
        value={formData.accountName}
        onChange={handleChange}
      />
    </div>
  );
};

export default AccountForm;
