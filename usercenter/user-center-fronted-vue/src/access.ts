import router from "@/router";
import { userLoginUserStore } from "@/store/userLoginUserStore";
import { message } from "ant-design-vue";

router.beforeEach(async (to, from, next) => {
  const loginUserStore = userLoginUserStore();
  const loginUser = loginUserStore.loginUser;
  const toUrl = to.fullPath;
  if (toUrl.startsWith("/admin")) {
    if (!loginUser || loginUser.userRole !== 1) {
      message.error("没有权限");
      next(`/user/login?redirect=${to.fullPath}`);
      return;
    }
  }
  next();
});
