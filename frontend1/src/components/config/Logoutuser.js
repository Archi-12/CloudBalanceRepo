import { toast } from "react-toastify";
import { logoutUser as logoutReduxUser } from "../redux/userSlice";
import api from "./AxiosConfig";

export const LogoutUser = async (navigate, dispatch) => {
  const token = localStorage.getItem("token");

  try {
    if (token) {
      await api.post("/auth/logout", null);
    }
  } catch (error) {
    console.warn(
      "Logout request failed (probably already expired or blacklisted):",
      error
    );
  }

  localStorage.removeItem("token");
  localStorage.removeItem("userState");

  dispatch(logoutReduxUser());
  navigate("/");
  toast.success("Logout successful");
};
