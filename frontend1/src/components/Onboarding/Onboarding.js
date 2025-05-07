import React, { useState } from "react";
import { useSelector } from "react-redux";
import StepOne from "./Step-1";
import StepTwo from "./Step-2";
import StepThree from "./Step-3";
import Navbar from "../utils/Navbar";
import { useNavigate } from "react-router-dom";
import { toast, ToastContainer } from "react-toastify";
import api from "../config/AxiosConfig.js";
import Sidebar from "../utils/Sidebar";
import { SidebarItems } from "../utils/SideBarItems";
import Footer from "../utils/Footer";

const Onboarding = () => {
  const [step, setStep] = useState(0);
  const navigate = useNavigate();

  const user = useSelector((state) => state.user);

  const [formData, setFormData] = useState({
    arnNumber: "",
    accountNumber: "",
    accountName: "",
  });

  const steps = [
    {
      title: "Create an IAM role",
      subTitle: "Create an IAM Role by following these steps:",
      component: <StepOne formData={formData} setFormData={setFormData} />,
    },
    {
      title: "Add Customer Managed Policies",
      subTitle: "Create an Inline policy for the role by following these steps",
      component: <StepTwo />,
    },
    {
      title: "Create Cost & Usage Report",
      subTitle: "Create a Cost & Usage Report by following these steps",
      component: <StepThree />,
    },
  ];

  const handleNext = () => {
    if (step === 0) {
      const { arnNumber, accountNumber, accountName } = formData;

      if (!arnNumber || !accountNumber || !accountName) {
        toast.error("Please fill in all fields.");
        return;
      }
    }

    if (step < steps.length - 1) {
      setStep((prev) => prev + 1);
    }
  };

  const handleBack = () => {
    if (step > 0) setStep((prev) => prev - 1);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    const payload = {
      arnNumber: formData.arnNumber,
      accountNumber: formData.accountNumber,
      accountName: formData.accountName,
    };

    try {
      const response = await api.post("/accounts", payload);

      if (response.status === 201) {
        navigate("/thankyou");
        toast.success("Account data submitted successfully!");
      }
    } catch (error) {
      const status = error.response?.status;
      const message =
        error.response?.data?.message || "Unexpected error occurred";

      if (status) {
        toast.error(`Error ${status}: ${message}`);
      } else {
        toast.error("Network or server error. Please try again later.");
      }
    }
  };

  return (
    <div className="onboarding-wrapper">
      <Navbar />
      <div className="onboarding-main">
        <Sidebar user={user} sidebarItems={SidebarItems} />

        <div className="onboarding-content">
          <h2>{steps[step].title}</h2>
          <p>{steps[step].subTitle}</p>
          <form onSubmit={handleSubmit}>
            {steps[step].component}
            <div className="button-group">
              {step > 0 && (
                <button
                  type="button"
                  className="cancel-button"
                  onClick={() => {
                    setStep(0);
                    setFormData(
                      {
                        arnNumber: "",
                        accountNumber: "",
                        accountName: "",
                      },
                      navigate("/Onboarding")
                    );
                    toast.info("Changes discarded, back to Step 1.");
                  }}
                >
                  Cancel
                </button>
              )}

              <div className="right-buttons">
                {step > 0 && (
                  <button
                    type="button"
                    className="step-buttons"
                    onClick={handleBack}
                  >
                    Back
                  </button>
                )}

                {step < steps.length - 1 && (
                  <button
                    type="button"
                    className="step-buttons"
                    onClick={handleNext}
                  >
                    Next
                  </button>
                )}

                {step === steps.length - 1 && (
                  <button type="submit" className="step-buttons">
                    Submit
                  </button>
                )}
              </div>
            </div>
          </form>
        </div>
      </div>
      <Footer />
      <ToastContainer />
    </div>
  );
};

export default Onboarding;
