import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Home from "./components/auth/Home";
import Login from "./components/auth/Login";
import ProtectedRoute from "./components/config/ProtectedRoute";
import { Provider } from "react-redux";
import { store } from "./components/redux/store";
import UserManagementPage from "./components/UserManagement/UserManagementPage";
import { ToastContainer } from "react-toastify";
import "./components/config/AxiosConfig";
import Onboarding from "./components/Onboarding/Onboarding";
import { Thankyou } from "./components/Onboarding/Thankyou";
import Aws from "./components/aws services/Aws";
import UserForm from "./components/UserManagement/UserForm";
import GroupByTabsWithFilter from "./components/cost-explorer/GroupByTabsWithFilter";

function App() {
  return (
    <Provider store={store}>
      <Router>
        <ToastContainer
          position="top-right"
          autoClose={500}
          hideProgressBar={false}
          newestOnTop={false}
          closeOnClick
          pauseOnFocusLoss
          draggable
          pauseOnHover
          theme="light"
        />
        <Routes>
          <Route path="/" element={<Login />} />
          <Route
            path="/home"
            element={
              <ProtectedRoute>
                <Home />
              </ProtectedRoute>
            }
          />
          <Route
            path="/user-management"
            element={
              <ProtectedRoute>
                <UserManagementPage />
              </ProtectedRoute>
            }
          />
          <Route path="/edit-user" element={<UserForm />} />
          <Route
            path="/add-user"
            element={
              <ProtectedRoute>
                <UserForm />
              </ProtectedRoute>
            }
          />
          <Route
            path="/onboarding"
            element={
              <ProtectedRoute>
                <Onboarding />
              </ProtectedRoute>
            }
          />
          <Route
            path="/thankyou"
            element={
              <ProtectedRoute>
                <Thankyou />
              </ProtectedRoute>
            }
          />
          <Route
            path="/aws-services"
            element={
              <ProtectedRoute>
                <Aws />
              </ProtectedRoute>
            }
          />
          <Route
            path="/cost-explorer"
            element={
              <ProtectedRoute>
                <GroupByTabsWithFilter />
              </ProtectedRoute>
            }
          />
        </Routes>
      </Router>
    </Provider>
  );
}
export default App;
