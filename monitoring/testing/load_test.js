import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
  vus: 60,        // 50 virtual users
  duration: '300s' // for 30 seconds
};

export default function () {
  const token = 'eyJhbGciOiJIUzUxMiJ9.eyJ1c2VybmFtZSI6InBlcmEiLCJpZCI6MiwiaXNzIjoianV0anViaWMtYmUiLCJzdWIiOiJwZXJhQHlhaG9vLmNvbSIsImF1ZCI6IndlYiIsImlhdCI6MTc3ODQwOTE0OSwiZXhwIjoxNzc4NDEwOTQ5fQ.MDfdvfR1YAMK9kDtCXngztYUu_lGBT0Rxzls_Jata0yUWgfZ2IBmz80bSWdK-LHIjgdKZYRFNqUvDXxhRELmig'
  const headers = { Authorization: `Bearer ${token}` };
  // const res = http.get('http://localhost:8080/api/videos/1', { headers });
  // check(res, { 'status is 200': (r) => r.status === 200 });
  http.get('http://localhost:8080/api/videos/1', { headers });
  sleep(0.5);
}