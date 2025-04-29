import React from "react";
import Navbar from "../utils/Navbar";
import Sidebar from "../utils/Sidebar";
import { SidebarItems } from "../utils/SideBarItems";
import img from "../images_comp/green_tick_check.svg";
import { useSelector } from "react-redux";
import "./thankyou.css";

export const Thankyou = () => {
  const user = useSelector((state) => state.user);
  return (
    <div className="page-container">
      <Sidebar user={user} sidebarItems={SidebarItems} />
      <div className="content-container">
        <Navbar />
        <div className="main-section">
          <img src={img} alt="Success Tick" className="success-icon" />
          <h1 className="heading">Thank You For CUR Access!</h1>
        </div>
      </div>
    </div>
  );
};
