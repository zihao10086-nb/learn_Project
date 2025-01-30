import { defineStore } from "pinia";
import { ref } from "vue";
import { getCurrentUser } from "@/api/user";

export const userLoginUserStore = defineStore("loginUser", () => {
  const loginUser = ref<any>({
    username: "未登录",
  });

  async function fetchLoginUser() {
    const res = await getCurrentUser();
    if (res.data.code === 0 && res.data.data) {
      loginUser.value = res.data.data;
    }
    // else {
    //   setTimeout(() => {
    //     loginUser.value = { username: "小黑子", id: 1 };
    //   }, 3000);
    // }
  }

  function setLoginUser(newLoginUser: any) {
    loginUser.value = newLoginUser;
  }

  return { loginUser, fetchLoginUser, setLoginUser };
});
