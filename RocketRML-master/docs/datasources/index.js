const { performance } = require('perf_hooks');
const parser = require('rocketrml');

const doMapping = async () => {
  const options = {
    toRDF: true,
    verbose: true,
    xmlPerformanceMode: false,
    replace: false,
  };

  try {
    const result = await parser.parseFile('./mapping.ttl', './out.n3', options);
    //console.log(result);
  } catch (err) {
    console.log(err);
  }
};

const calculateQuartile = (data, q) => {
  const pos = ((data.length) - 1) * q;
  const base = Math.floor(pos);
  const rest = pos - base;

  if ((data[base + 1] !== undefined)) {
    return data[base] + rest * (data[base + 1] - data[base]);
  } else {
    return data[base];
  }
};

const calculateMetrics = (times) => {
  times.sort((a, b) => a - b);

  const n = times.length;
  const mean = times.reduce((acc, val) => acc + val, 0) / n;
  
  const q1 = calculateQuartile(times, 0.25);
  const median = calculateQuartile(times, 0.5);
  const q3 = calculateQuartile(times, 0.75);

  const fastest = times[0];
  const slowest = times[n - 1];

  return { fastest, slowest, mean, q1, median, q3 };
};

const measureExecutionTime = async () => {
  const executionTimes = [];
  const n = 1;

  for (let i = 0; i < n; i++) {
    const start = performance.now(); // Inizia la misurazione del tempo
    await doMapping();
    const end = performance.now(); // Termina la misurazione del tempo
    const executionTime = (end - start) / 1000; // Calcola il tempo di esecuzione in secondi
    executionTimes.push(executionTime); // Salva il tempo di esecuzione nella lista
    console.log(`Execution Time for iteration ${i + 1}: ${executionTime.toFixed(3)} seconds`);
  }

  const metrics = calculateMetrics(executionTimes);
  
  console.log(`Fastest Execution Time: ${metrics.fastest.toFixed(3)} seconds`);
  console.log(`Slowest Execution Time: ${metrics.slowest.toFixed(3)} seconds`);
  console.log(`Mean Execution Time: ${metrics.mean.toFixed(3)} seconds`);
  console.log(`Q1 (25th percentile) Execution Time: ${metrics.q1.toFixed(3)} seconds`);
  console.log(`Median (Q2) Execution Time: ${metrics.median.toFixed(3)} seconds`);
  console.log(`Q3 (75th percentile) Execution Time: ${metrics.q3.toFixed(3)} seconds`);
};

measureExecutionTime();
