import React from "react";
import { FaTwitter, FaGithub, FaLinkedin } from "react-icons/fa";

export default function Footer() {
  return (
    <footer className="footer">
      <p>
        Follow us: <FaTwitter className="icon" /> |{" "}
        <FaLinkedin className="icon" />| <FaGithub className="icon" />
      </p>
      <p>&copy; CloudBalance 2025 | All Rights Reserved</p>
    </footer>
  );
}
