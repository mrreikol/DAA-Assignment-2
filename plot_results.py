import os
import matplotlib.pyplot as plt
import pandas as pd

os.makedirs('results/plots', exist_ok=True)

df_w1 = pd.read_csv('results/tables/workload1_random_access.csv')
df_w2 = pd.read_csv('results/tables/workload2_search.csv')
df_w4 = pd.read_csv('results/tables/workload4_priority_processing.csv')

plt.figure(figsize=(9, 5))
for ds in df_w1['DataStructure'].unique():
    sub = df_w1[df_w1['DataStructure'] == ds]
    plt.plot(sub['N'], sub['AvgTimeNs'] / 1e6, marker='o', label=f'{ds} get(i)')

plt.xscale('log')
plt.yscale('log')
plt.title('Plot 1: Workload 1 - Execution Time vs. Input Size (n)')
plt.xlabel('Initial Size n (log scale)')
plt.ylabel('Average Time (ms, log scale)')
plt.grid(True, which='both', ls='--', alpha=0.5)
plt.legend()
plt.tight_layout()
plt.savefig('results/plots/plot1_execution_time.png', dpi=300)
plt.close()

plt.figure(figsize=(9, 5))
for ds in df_w2['DataStructure'].unique():
    sub = df_w2[df_w2['DataStructure'] == ds]
    plt.plot(sub['N'], sub['AvgComparisons'], marker='s', label=f'{ds} Comparisons')

plt.xscale('log')
plt.yscale('log')
plt.title('Plot 2: Workload 2 - Comparisons vs. Input Size (n)')
plt.xlabel('Initial Size n (log scale)')
plt.ylabel('Average Comparisons (log scale)')
plt.grid(True, which='both', ls='--', alpha=0.5)
plt.legend()
plt.tight_layout()
plt.savefig('results/plots/plot2_metrics.png', dpi=300)
plt.close()

plt.figure(figsize=(9, 5))
plt.plot(df_w4['N'], df_w4['AvgInsertTimeNs'] / 1e6, marker='^', label='MinHeap Insert')
plt.plot(df_w4['N'], df_w4['AvgExtractTimeNs'] / 1e6, marker='v', label='MinHeap ExtractMin')
plt.xscale('log')
plt.yscale('log')
plt.title('Plot 3: Workload 4 - MinHeap Operations Time vs. n')
plt.xlabel('n (log scale)')
plt.ylabel('Time (ms, log scale)')
plt.grid(True, which='both', ls='--', alpha=0.5)
plt.legend()
plt.tight_layout()
plt.savefig('results/plots/plot3_heap_time.png', dpi=300)
plt.close()

print("Графики успешно сгенерированы в папку results/plots/")