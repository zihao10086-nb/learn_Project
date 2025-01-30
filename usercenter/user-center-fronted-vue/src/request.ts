import axios from "axios";

const myAxios = axios.create({
  baseURL: "http://localhost:8080",
  timeout: 10000,
  withCredentials: true,
});

//添加一个请求接口
myAxios.interceptors.request.use(
  function (config) {
    //do something before request is sent
    return config;
  },

  function (error) {
    //do something with request error
    return Promise.reject(error);
  }
);

myAxios.interceptors.response.use(
  function (reponse) {
    console.log(reponse);

    const { data } = reponse;
    console.log(data);
    //未登录
    if (data.code === 40100) {
      //不是获取用户信息接口，或者不是登录页面，则跳转登录页面
      if (
        !reponse.request.responseURL.includes("user/current") &&
        !window.location.pathname.includes("user/login")
      ) {
        window.location.href = `/user/login?redirect=${window.location.href}`;
      }
    }
    return reponse;
  },
  function (error) {
    return Promise.reject(error);
  }
);

export default myAxios;
