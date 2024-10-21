import axios from 'axios'
const { VITE_API_BASE_URL } = import.meta.env

const localAxios = () => { 
    const instance = axios.create({
        baseURL: VITE_API_BASE_URL,
        headers: {
            'Content-Type': 'application/json;charset=utf-8'
        }
    })

    instance.defaults.headers.common["Authorization"] = "";
    instance.defaults.headers.post["Content-Type"] = "application/json";
    instance.defaults.headers.put["Content-Type"] = "application/json";

    // 요청 인터셉터 추가
    instance.interceptors.request.use(
        (config) => {
            const accessToken = sessionStorage.getItem('accessToken'); // 토큰 가져오기
            if (accessToken) {
                config.headers['Authorization'] = `Bearer ${accessToken}`; // 헤더에 토큰 추가
            }
            return config;
        },
        (error) => {
            return Promise.reject(error); 
        }
    );

    return instance;
}

export { localAxios }