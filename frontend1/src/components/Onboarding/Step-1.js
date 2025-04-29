import React from "react";
import CopyBox from "./CopyBox";
import "./StepOne.css";
import img from "/home/archi/Cloudbalance/frontend/src/components/images_comp/step1img1.png";
import AccountForm from "./AccountForm";

const StepOne = ({ formData, setFormData }) => {
  return (
    <div className="step-container">
      <ol className="step-list">
        <li>
          Log into AWS account &amp; &nbsp;
          <a href="#" className="step-link">
            Create an IAM Role
          </a>
          .
        </li>

        <li>
          In the Trusted entity type section, select{" "}
          <strong>Custom trust policy</strong>. Replace the prefilled policy
          with the one below:
          <div className="step-box">
            <CopyBox
              text={`{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Principal": {
        "AWS": "arn:aws:iam::951485052809:root"
      },
      "Action": "sts:AssumeRole",
      "Condition": {
        "StringEquals": {
          "sts:ExternalId": "Um9oaXRDS19ERUZBVUxUZDIzOTJkZTgtN2E0OS00NWQ3LTg3MzItODkyM2ExZTIzMjQw"
        }
      }
    },
    {
      "Effect": "Allow",
      "Principal": {
        "Service": "s3.amazonaws.com"
      },
      "Action": "sts:AssumeRole"
    }
  ]
}`}
            />
          </div>
        </li>
        <li>
          Click on <strong>Next</strong> to go to the{" "}
          <strong>Add permissions</strong> page. No permissions are needed at
          this stage. Just click <strong>Next</strong>.
        </li>

        <li>
          In the <strong>Role name</strong> field, enter the below name and
          click <strong>Create Role</strong>:
          <div className="mt-2">
            <CopyBox text="CK-Tuner-Role-dev2" />
          </div>
        </li>

        <li>
          Go to the newly created IAM Role and copy the{" "}
          <strong>Role ARN</strong>.
          <img src={img} alt="stepOne" className="step-image" />
        </li>

        <li>
          Paste the copied Role ARN and fill account details below:
          <br />
          <AccountForm formData={formData} setFormData={setFormData} />
        </li>
      </ol>
    </div>
  );
};

export default StepOne;
