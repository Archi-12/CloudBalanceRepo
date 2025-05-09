import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  id: null,
  username: null,
  email: null,
  role: null,
};

const userSlice = createSlice({
  name: "user",
  initialState,
  reducers: {
    setUser(state, action) {
      state.id = action.payload.id;
      state.username = action.payload.username;
      state.email = action.payload.email;
      state.role = action.payload.role;
    },
    logoutUser(state) {
      state.id = null;
      state.username = null;
      state.email = null;
      state.role = null;
    },
    switchUser(state, action) {
      state.id = action.payload.id;
      state.username = action.payload.username;
      state.email = action.payload.email;
      state.role = action.payload.role;
    },
  },
});

export const { setUser, logoutUser, switchUser, switchBack } =
  userSlice.actions;
export default userSlice.reducer;
