import React from "react";
import { toast } from "react-toastify";

const CopyBox = ({ text }) => {
  const handleCopy = () => {
    navigator.clipboard.writeText(text);
    toast.success("Copied to clipboard!");
  };

  return (
    <div
      className="flex items-center justify-between p-4 rounded-lg bg-gray-100 border border-gray-300 w-full max-w-md shadow-sm"
      onClick={handleCopy}
    >
      <span className="truncate text-gray-800 font-mono">{text}</span>
    </div>
  );
};

export default CopyBox;
