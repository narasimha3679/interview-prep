function fn() {
  var env = karate.env;
  if (!env) {
    env = 'dev';
  }

  karate.log('karate.env selected:', env);

  var config = {
    env: env,
    connectTimeout: 10000,
    readTimeout: 15000
  };

  if (env === 'dev') {
    config.jsonPlaceholderUrl = 'https://jsonplaceholder.typicode.com';
    config.httpBinUrl = 'https://httpbin.org';
  } else if (env === 'qa') {
    // Same public APIs for this dummy — in real projects these differ per env
    config.jsonPlaceholderUrl = 'https://jsonplaceholder.typicode.com';
    config.httpBinUrl = 'https://httpbin.org';
  } else if (env === 'staging') {
    config.jsonPlaceholderUrl = 'https://jsonplaceholder.typicode.com';
    config.httpBinUrl = 'https://httpbin.org';
  } else {
    karate.fail('Unknown karate.env: ' + env + ' (use dev|qa|staging)');
  }

  // Defaults applied to every HTTP call unless a feature overrides them
  karate.configure('connectTimeout', config.connectTimeout);
  karate.configure('readTimeout', config.readTimeout);
  karate.configure('ssl', true);

  return config;
}
