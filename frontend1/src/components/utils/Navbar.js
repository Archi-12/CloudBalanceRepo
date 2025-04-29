// import React from "react";
// import logo from "/home/archi/Cloudbalance/frontend/src/components/images_comp/cloudLogo.png";
// import { useSelector } from "react-redux";
// import { LogoutUser } from "../config/Logoutuser";
// import { useDispatch } from "react-redux";
// import { useNavigate } from "react-router-dom";

// export default function Navbar() {
//   const navigate = useNavigate();
//   const dispatch = useDispatch();
//   const handleLogout = () => {
//     LogoutUser(navigate, dispatch);
//   };
//   const handleImageClick = () => {
//     navigate("/home");
//   };

//   const user = useSelector((state) => state.user);
//   return (
//     <nav className="navbar">
//       <div className="logo">
//         <img
//           src={logo}
//           alt="CLOUD BALANCE"
//           className="logo"
//           onClick={handleImageClick}
//         />
//       </div>
//       <div className="nav-icons">
//         <h3>
//           <br /> {user.username || "User"}
//         </h3>
//         <button onClick={handleLogout}>Logout</button>
//       </div>
//     </nav>
//   );
// }

import React, { useState, useEffect, useRef } from "react";
import logo from "/home/archi/Cloudbalance/frontend/src/components/images_comp/cloudLogo.png";
import { useSelector } from "react-redux";
import { LogoutUser } from "../config/Logoutuser";
import { useDispatch } from "react-redux";
import { useNavigate } from "react-router-dom";
import { FaUserCircle } from "react-icons/fa";

export default function Navbar() {
  const [showDropdown, setShowDropdown] = useState(false);
  const dropdownRef = useRef(null);
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const user = useSelector((state) => state.user);

  const handleLogout = () => {
    LogoutUser(navigate, dispatch);
  };

  const handleImageClick = () => {
    navigate("/home");
  };

  const toggleDropdown = () => {
    setShowDropdown((prev) => !prev);
  };

  // Close dropdown when clicking outside
  useEffect(() => {
    const handleClickOutside = (event) => {
      if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
        setShowDropdown(false);
      }
    };
    document.addEventListener("mousedown", handleClickOutside);
    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
    };
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
        <div className="user-icon-container" ref={dropdownRef}>
          <FaUserCircle
            size={32}
            style={{ cursor: "pointer" }}
            onClick={toggleDropdown}
          />
          {showDropdown && (
            <div className="user-dropdown">
              <p>Email: {user.email}</p>
              <p>Role: {user.role}</p>
              <button onClick={handleLogout}>Logout</button>
            </div>
          )}
        </div>
      </div>
    </nav>
  );
}
