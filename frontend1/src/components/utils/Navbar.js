import React, { useState, useEffect, useRef } from "react";
import logo from "/home/archi/Cloudbalance/frontend/src/components/images_comp/cloudLogo.png";
import { useSelector, useDispatch } from "react-redux";
import { LogoutUser } from "../config/Logoutuser";
import { useNavigate } from "react-router-dom";
import { FaUserCircle } from "react-icons/fa";
import SwitchUserDropdown from "./SwitchUserDropdown";
import { switchBack } from "../redux/userSlice";

export default function Navbar() {
  const [showUserDropdown, setShowUserDropdown] = useState(false);
  const [showSwitchDropdown, setShowSwitchDropdown] = useState(false);
  const userDropdownRef = useRef(null);
  const switchDropdownRef = useRef(null);

  const navigate = useNavigate();
  const dispatch = useDispatch();
  const user = useSelector((state) => state.user);

  const handleLogout = () => {
    LogoutUser(navigate, dispatch);
  };

  const handleImageClick = () => {
    navigate("/home");
  };

  const toggleUserDropdown = () => {
    setShowUserDropdown((prev) => !prev);
    setShowSwitchDropdown(false);
  };

  const toggleSwitchDropdown = () => {
    setShowSwitchDropdown((prev) => !prev);
    setShowUserDropdown(false);
  };

  useEffect(() => {
    const handleClickOutside = (event) => {
      if (
        userDropdownRef.current &&
        !userDropdownRef.current.contains(event.target)
      ) {
        setShowUserDropdown(false);
      }

      if (
        switchDropdownRef.current &&
        !switchDropdownRef.current.contains(event.target)
      ) {
        setShowSwitchDropdown(false);
      }
    };

    document.addEventListener("mousedown", handleClickOutside);
    return () => document.removeEventListener("mousedown", handleClickOutside);
  }, []);

  return (
    <nav className="navbar">
      <div className="logo">
        <img
          src={logo}
          alt="CLOUD BALANCE"
          className="logo"
          onClick={handleImageClick}
        />
      </div>

      <div className="nav-icons">
        {user.role === "ADMIN" && (
          <div className="switch-user-container" ref={switchDropdownRef}>
            <button onClick={toggleSwitchDropdown}>Switch User</button>
            {showSwitchDropdown && (
              <SwitchUserDropdown
                onClose={() => setShowSwitchDropdown(false)}
              />
            )}
          </div>
        )}

        <div className="user-icon-container" ref={userDropdownRef}>
          <FaUserCircle
            size={32}
            style={{ cursor: "pointer" }}
            onClick={toggleUserDropdown}
          />
          {showUserDropdown && (
            <div className="user-dropdown">
              <p>Email: {user.email}</p>
              <p>Role: {user.role}</p>
              {user.originalUser ? (
                <button onClick={() => dispatch(switchBack())}>
                  Switch Back to Admin
                </button>
              ) : (
                <button onClick={handleLogout}>Logout</button>
              )}
            </div>
          )}
        </div>
      </div>
    </nav>
  );
}
