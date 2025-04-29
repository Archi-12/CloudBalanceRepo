import React from "react";
import { useSelector } from "react-redux";
import Navbar from "../utils/Navbar";
import Sidebar from "../utils/Sidebar";
import Footer from "../utils/Footer";
import { SidebarItems } from "../utils/SideBarItems";
import "../utils/Home.css";

export default function Home() {
  const user = useSelector((state) => state.user);
  //console.log("User in Home:", user);

  return (
    <div className="dashboard-container">
      <Navbar />
      <div className="main-content">
        <Sidebar user={user} sidebarItems={SidebarItems} />
        <section className="dashboard-content">
          <h2>Welcome, {user.username || "User"}!</h2>
          <p>Select a dashboard from the left to get started.</p>
        </section>
      </div>
      <Footer />
    </div>
  );
}
