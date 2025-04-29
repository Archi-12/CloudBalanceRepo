export const FormConfig = [
  {
    name: "name",
    label: "Name",
    type: "text",
    placeholder: "Name",
    required: true,
  },
  {
    name: "email",
    label: "Email",
    type: "email",
    placeholder: "Email",
    required: true,
  },
  {
    name: "role",
    label: "Role",
    type: "select",
    required: true,
    options: [
      { label: "Admin", value: "admin" },
      { label: "Read Only", value: "read_only" },
      { label: "Customer", value: "customer" },
    ],
  },
  {
    name: "password",
    label: "Password",
    type: "password",
    placeholder: "Password",
    required: true,
  },
];
