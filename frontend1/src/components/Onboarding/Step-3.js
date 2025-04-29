import React from "react";
import CopyBox from "./CopyBox";
import "./StepOne.css";
import img1 from "/home/archi/Cloudbalance/frontend/src/components/images_comp/step3img1.png";
import img2 from "/home/archi/Cloudbalance/frontend/src/components/images_comp/step3img2.png";
import img3 from "/home/archi/Cloudbalance/frontend/src/components/images_comp/step3img3.png";

const StepThree = ({ formData, setFormData }) => {
  return (
    <div className="step-container">
      <ol className="step-list">
        <li>
          Go to &nbsp;
          <a href="#" className="step-link">
            Cost and Usage Reports
          </a>{" "}
          in the Billing Dashboard and click on Create report.
        </li>
        <li>
          Name the report as shown below and select the{" "}
          <strong>Include resource IDs</strong> checkbox -
        </li>
        <div className="mt-2">
          <CopyBox text="ck-tuner-275595855473-hourly-cur" />
        </div>
        <p>Ensure that the following configuration is checked</p>
        <input type="checkbox" checked disabled />
        <label>Include Resource IDs</label>
        <p>
          Click on <strong>Next</strong>
        </p>
        <img src={img1} alt="CK-tuner" className="step-image" />
        <li>
          In Configure S3 Bucket, provide the name of the S3 bucket that was
          created -
        </li>
        <p>Ensure that the following configuration is checked</p>
        <input type="checkbox" checked disabled />
        <label>
          The following default policy will be applied to your bucket
        </label>
        <p>
          Click on <strong>Save</strong>
        </p>
        <img src={img2} alt="CK-tuner" className="step-image" />
        <li>
          In the Delivery options section, enter the below-mentioned Report path
          prefix -
        </li>
        <div className="mt-2">
          <CopyBox text="275595855473" />
        </div>
        <p>Additionally, ensure that the following checks are in place</p>
        <p>Time granularity:</p>
        <input type="radio" checked disabled />
        <label>
          <strong>Hourly</strong>
        </label>
        <p>
          Please make sure these checks are Enabled in Enable report data
          integration for:
        </p>
        <input type="checkbox" checked disabled />

        <label>
          <strong>Amazon Athena</strong>
        </label>
        <img src={img3} alt="CK-tuner" className="step-image" />
        <li>
          {" "}
          Click on <strong>Next</strong>. Now, review the configuration of the
          Cost and Usage Report. Once satisfied, click on{" "}
          <strong>Create Report.</strong>
        </li>
      </ol>
    </div>
  );
};

export default StepThree;
