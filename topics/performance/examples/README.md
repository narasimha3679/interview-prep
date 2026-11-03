# Performance Examples

`k6-load.js` is a starter k6 script. It targets the mini-service by default.

```bash
# Start the service first (optional)
cd ../../spring-boot/examples/mini-service
mvn spring-boot:run

# In another terminal
k6 run k6-load.js
```

Override the target:

```bash
k6 run -e BASE_URL=https://your-test-env k6-load.js
```

The thresholds are examples; set them from your SLO, not from this file.
