import React, { useEffect, useState } from "react";
import { useSelector, useDispatch } from "react-redux";
import { setUser } from "../redux/userSlice";
import { Navigate, useNavigate } from "react-router-dom";
import { LogoutUser } from "./Logoutuser";
import api from "./AxiosConfig";

const ProtectedRoute = ({ children }) => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const [loading, setLoading] = useState(true);
  const token = localStorage.getItem("token");
  const user = useSelector((state) => state.user);

  useEffect(() => {
    const fetchUser = async () => {
      try {
        const response = await api.get("/users/me");
        const { email, username, roles } = response.data.data;
        dispatch(setUser({ email, username, role: roles }));
        setLoading(false);
      } catch (error) {
        console.error("Error fetching user in Home:", error);
        LogoutUser(navigate, dispatch);
      }
    };
    if (!user?.username) {
      fetchUser();
    } else {
      setLoading(false);
    }
  }, []);

  if (loading) {
    return <div>Loading...</div>;
  }

  if (!token) {
    return <Navigate to="/" replace />;
  }

  return children;
};

export default ProtectedRoute;
