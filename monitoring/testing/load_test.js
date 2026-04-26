import http from 'k6/http';

export const options = {
  vus: 200,        // 50 virtual users
  duration: '300s' // for 30 seconds
};

export default function () {
  http.get('http://localhost:8080/api/videos/1');
}