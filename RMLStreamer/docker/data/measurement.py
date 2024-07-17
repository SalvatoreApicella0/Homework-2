import time
import statistics
import subprocess
import psutil


execution_times = []

def count_io_operations():
    # Ottieni i contatori di I/O del disco
    io_counters = psutil.disk_io_counters()
    # Restituisci il numero di operazioni di lettura e scrittura
    return io_counters.read_count, io_counters.write_count

container_image = "rmlio/rmlstreamer"  # Immagine del container Docker

PWD = 'c:/Users/marti/Desktop/TecnologieSemanticheHomework/Homework2_ST/RMLStreamer/docker/data' #CAMBIARE PATH


n = 10

for i in range(n):  # Esegui 10 round per esempio
    start_time = time.time()
    
    # Esegui il container Docker con il comando desiderato
    subprocess.run([
        "docker", "run", "-v", f"{PWD}:/data", "--rm", container_image, 
        "toFile", "-m", "/data/mapping.ttl", "-o", "/data/output"
    ])

    end_time = time.time()
    
    execution_time = end_time - start_time
    execution_times.append(execution_time)
    


# Calcolo delle metriche
fastest_time = min(execution_times)
slowest_time = max(execution_times)
mean_time = statistics.mean(execution_times)
median_time = statistics.median(execution_times)

sorted_execution_times = sorted(execution_times)
Q1 = sorted_execution_times[int(len(sorted_execution_times) * 0.25)]
#median = sorted_execution_times[int(len(sorted_execution_times) * 0.5)]
Q3 = sorted_execution_times[int(len(sorted_execution_times) * 0.75)]

read_count, write_count = count_io_operations()
print("Numero di operazioni di lettura:", read_count / n)
print("Numero di operazioni di scrittura:", write_count / n)
print("Fastest Round Execution Time:", fastest_time)
print("Slowest Round Execution Time:", slowest_time)
print("Mean Round Execution Time:", mean_time)
print("Q1 of Round Execution Times:", Q1)
print("Q2 (Median) of Round Execution Times:", median_time)
#print("Q2 NUOVOOO:", median)
print("Q3 of Round Execution Times:", Q3)

def run_bash_script(bash_command):
    result = subprocess.run(bash_command, shell=True, check=True)
    if result.returncode == 0:
        print("!!!!!!!!!!script successfully executed!!!!!!!!!!")
    else:
        print(f"script execution error: {result.returncode}")
