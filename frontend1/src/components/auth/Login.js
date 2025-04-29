import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { FaEye, FaEyeSlash } from "react-icons/fa";
import { useDispatch, useSelector } from "react-redux";
import { setUser } from "../redux/userSlice";
import logo from "/home/archi/Cloudbalance/frontend/src/components/images_comp/cloudLogo.png";
import { toast } from "react-toastify";
import "./login.css";
import api from "../config/AxiosConfig";

export default function Login() {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const [information, setInformation] = useState({ email: "", pwd: "" });
  const [showPassword, setShowPassword] = useState(false);

  const contactSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await api.post("/auth/login", {
        email: information.email,
        password: information.pwd,
      });
      const { token, email, username, role } = response.data.data;
      localStorage.setItem("token", token);
      dispatch(setUser({ email, username, role }));

      navigate("/home", { replace: true });
      toast.success("Login successful!");
      setInformation({ email: "", pwd: "" });
    } catch (error) {
      toast.error("Login failed: " + error.response?.data?.message);
    }
  };

  const handleChange = (field, value) => {
    setInformation({ ...information, [field]: value });
  };

  return (
    <div className="login-container">
      <form className="login-form" onSubmit={contactSubmit}>
        <img src={logo} alt="CLOUD BALANCE" className="logo1" />
        <label className="field-label">Email</label>
        <input
          type="email"
          onChange={(e) => handleChange("email", e.target.value)}
          value={information.email}
          required
        />

        <label className="field-label">Password</label>
        <div className="password-field">
          <input
            type={showPassword ? "text" : "password"}
            onChange={(e) => handleChange("pwd", e.target.value)}
            value={information.pwd}
            required
          />
          <span
            onClick={() => setShowPassword(!showPassword)}
            className="eye-icon"
          >
            {showPassword ? <FaEyeSlash /> : <FaEye />}
          </span>
        </div>
        <button type="submit" className="login-btn">
          LOGIN
        </button>
      </form>
    </div>
  );
}
