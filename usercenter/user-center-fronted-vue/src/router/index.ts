import { createRouter, createWebHistory, RouteRecordRaw } from "vue-router";
import HomeView from "../views/HomeView.vue";
import homePage from "@/pages/HomePage.vue";
import { userLoginUserStore } from "@/store/userLoginUserStore";
import UserLoginPage from "@/pages/user/UserLoginPage.vue";
import UserRegisterPage from "@/pages/user/UserRegisterPage.vue";
import UserManagePage from "@/pages/admin/UserManagePage.vue";

const routes: Array<RouteRecordRaw> = [
  {
    path: "/",
    name: "home",
    component: homePage,
  },
  {
    path: "/user/login",
    name: "userLogin",
    component: UserLoginPage,
  },
  {
    path: "/user/register",
    name: "userRegister",
    component: UserRegisterPage,
  },
  {
    path: "/admin/userManage",
    name: "adminUserManage",
    component: UserManagePage,
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;
