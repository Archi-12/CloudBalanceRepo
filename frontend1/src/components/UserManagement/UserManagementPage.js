import React, { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import Navbar from "../utils/Navbar";
import Sidebar from "../utils/Sidebar";
import Footer from "../utils/Footer";
import { SidebarItems } from "../utils/SideBarItems";
import UserTable from "./UserTable";
import api from "../config/AxiosConfig";
import "./UserManagement.css";
import { toast } from "react-toastify";

function UserManagementPage() {
  const user = useSelector((state) => state.user);
  const navigate = useNavigate();
  const [users, setUsers] = useState([]);

  const fetchUsers = async () => {
    try {
      const res = await api.get("/users");
      setUsers(res.data.data);
    } catch (err) {
      toast.error("Error fetching users:", err);
    }
  };

  useEffect(() => {
    fetchUsers();
  }, []);

  const handleAdd = () => {
    navigate("/add-user");
  };

  const handleEdit = (userData) => {
    navigate("/edit-user", { state: { userData } });
  };

  return (
    <div className="dashboard-container">
      <Navbar />
      <div className="main-content">
        <Sidebar user={user} sidebarItems={SidebarItems} />
        <section className="dashboard-content">
          <h2>User Management</h2>
          <button onClick={handleAdd}>Add User</button>
          <UserTable users={users} onEdit={handleEdit} />
        </section>
      </div>
      <Footer />
    </div>
  );
}
export default UserManagementPage;
