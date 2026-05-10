# Running the server
Run docker in root folder:
`docker compose up --build`
Docker will start up the server, db, grafana and prometheus.

Swagger is running on: http://localhost:8080/swagger-ui/index.html

# Running the client
In [jutjubic-fe/jutjubic/src](https://github.com/K-atarinaVujovic/jutjubic/tree/f4be1c7912dee21e021a426a3dce18018987dded/jutjubic-fe/jutjubic/src) run:  
`npm install`  
`ng serve`

# Monitoring 
To be able to view CPU usage in Grafana dashboard, run windows exporter in a cmd:  
`windows_exporter --web.listen-address 127.0.0.1:8082`

Grafana (dashboard Jutjubic) is running on: http://localhost:3000
Prometheus queries are running on: http://localhost:9090/query

To test active and idle db connections, run the load test in [jutjubic/monitoring/testing](https://github.com/K-atarinaVujovic/jutjubic/blob/f4be1c7912dee21e021a426a3dce18018987dded/monitoring/testing/load_test.js):  
`k6 run load_test.js`  
Make sure to get a valid JWT and place in `const token` so the test can have access to the endpoint.
