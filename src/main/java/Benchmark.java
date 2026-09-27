import structures.DynamicArray;
import structures.LinkedList;
import structures.MinHeap;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class Benchmark {
    private static final int[] N_VALUES = {100, 1000, 10000, 100000};
    private static final int REPETITIONS = 5;
    private static final long SEED = 42L;

    public static void main(String[] args) {
        File dir = new File("results/tables");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        runWorkload1();
        runWorkload2();
        runWorkload3();
        runWorkload4();
    }

    private static void runWorkload1() {
        String filename = "results/tables/workload1_random_access.csv";
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("DataStructure,N,Operations,AvgTimeNs,AvgAccesses,TheoreticalComplexity");
            final int M = 10000;

            for (int n : N_VALUES) {
                long totalTimeDA = 0;
                long totalAccessesDA = 0;

                for (int r = 0; r < REPETITIONS; r++) {
                    DynamicArray da = new DynamicArray(n);
                    Random rng = new Random(SEED + r);
                    for (int i = 0; i < n; i++) {
                        da.add(rng.nextInt());
                    }
                    int[] indices = new int[M];
                    for (int i = 0; i < M; i++) {
                        indices[i] = rng.nextInt(n);
                    }

                    da.resetMetrics();
                    long start = System.nanoTime();
                    for (int i = 0; i < M; i++) {
                        da.get(indices[i]);
                    }
                    long end = System.nanoTime();
                    totalTimeDA += (end - start);
                    totalAccessesDA += da.elementAccesses;
                }
                writer.printf("DynamicArray,%d,%d,%d,%d,Theta(1)%n",
                        n, M, totalTimeDA / REPETITIONS, totalAccessesDA / REPETITIONS);

                long totalTimeLL = 0;
                long totalAccessesLL = 0;

                for (int r = 0; r < REPETITIONS; r++) {
                    LinkedList ll = new LinkedList();
                    Random rng = new Random(SEED + r);
                    for (int i = 0; i < n; i++) {
                        ll.add(rng.nextInt());
                    }
                    int[] indices = new int[M];
                    for (int i = 0; i < M; i++) {
                        indices[i] = rng.nextInt(n);
                    }

                    ll.resetMetrics();
                    long start = System.nanoTime();
                    for (int i = 0; i < M; i++) {
                        ll.get(indices[i]);
                    }
                    long end = System.nanoTime();
                    totalTimeLL += (end - start);
                    totalAccessesLL += ll.elementAccesses;
                }
                writer.printf("LinkedList,%d,%d,%d,%d,Theta(n)%n",
                        n, M, totalTimeLL / REPETITIONS, totalAccessesLL / REPETITIONS);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void runWorkload2() {
        String filename = "results/tables/workload2_search.csv";
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("DataStructure,N,Operations,AvgTimeNs,AvgComparisons,TheoreticalComplexity");
            final int M = 1000;

            for (int n : N_VALUES) {
                long totalTimeDA = 0;
                long totalComparisonsDA = 0;

                for (int r = 0; r < REPETITIONS; r++) {
                    Random rng = new Random(SEED + r);
                    DynamicArray da = new DynamicArray(n);
                    for (int i = 0; i < n; i++) {
                        da.add(rng.nextInt(n * 2));
                    }
                    int[] searchVals = new int[M];
                    for (int i = 0; i < M; i++) {
                        searchVals[i] = rng.nextInt(n * 2);
                    }

                    da.resetMetrics();
                    long start = System.nanoTime();
                    for (int i = 0; i < M; i++) {
                        da.contains(searchVals[i]);
                    }
                    long end = System.nanoTime();
                    totalTimeDA += (end - start);
                    totalComparisonsDA += da.comparisons;
                }
                writer.printf("DynamicArray,%d,%d,%d,%d,Theta(n)%n",
                        n, M, totalTimeDA / REPETITIONS, totalComparisonsDA / REPETITIONS);

                long totalTimeLL = 0;
                long totalComparisonsLL = 0;

                for (int r = 0; r < REPETITIONS; r++) {
                    Random rng = new Random(SEED + r);
                    LinkedList ll = new LinkedList();
                    for (int i = 0; i < n; i++) {
                        ll.add(rng.nextInt(n * 2));
                    }
                    int[] searchVals = new int[M];
                    for (int i = 0; i < M; i++) {
                        searchVals[i] = rng.nextInt(n * 2);
                    }

                    ll.resetMetrics();
                    long start = System.nanoTime();
                    for (int i = 0; i < M; i++) {
                        ll.contains(searchVals[i]);
                    }
                    long end = System.nanoTime();
                    totalTimeLL += (end - start);
                    totalComparisonsLL += ll.comparisons;
                }
                writer.printf("LinkedList,%d,%d,%d,%d,Theta(n)%n",
                        n, M, totalTimeLL / REPETITIONS, totalComparisonsLL / REPETITIONS);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void runWorkload3() {
        String filename = "results/tables/workload3_insert_remove.csv";
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("DataStructure,N,Operation,Position,OperationsCount,AvgTimeNs,AvgMovements,AvgAccesses,TheoreticalComplexity");

            for (int n : N_VALUES) {
                int ops = Math.min(1000, n);

                long timeDAIns0 = 0, movesDAIns0 = 0, accDAIns0 = 0;
                for (int r = 0; r < REPETITIONS; r++) {
                    DynamicArray da = createBaseArray(n, r);
                    da.resetMetrics();
                    long start = System.nanoTime();
                    for (int i = 0; i < ops; i++) {
                        da.add(0, i);
                    }
                    long end = System.nanoTime();
                    timeDAIns0 += (end - start);
                    movesDAIns0 += da.elementMovements;
                    accDAIns0 += da.elementAccesses;
                }
                writer.printf("DynamicArray,%d,Insert,0,%d,%d,%d,%d,Theta(n)%n",
                        n, ops, timeDAIns0 / REPETITIONS, movesDAIns0 / REPETITIONS, accDAIns0 / REPETITIONS);

                long timeDARem0 = 0, movesDARem0 = 0, accDARem0 = 0;
                for (int r = 0; r < REPETITIONS; r++) {
                    DynamicArray da = createBaseArray(n, r);
                    da.resetMetrics();
                    long start = System.nanoTime();
                    for (int i = 0; i < ops; i++) {
                        da.remove(0);
                    }
                    long end = System.nanoTime();
                    timeDARem0 += (end - start);
                    movesDARem0 += da.elementMovements;
                    accDARem0 += da.elementAccesses;
                }
                writer.printf("DynamicArray,%d,Remove,0,%d,%d,%d,%d,Theta(n)%n",
                        n, ops, timeDARem0 / REPETITIONS, movesDARem0 / REPETITIONS, accDARem0 / REPETITIONS);

                long timeDAInsMid = 0, movesDAInsMid = 0, accDAInsMid = 0;
                for (int r = 0; r < REPETITIONS; r++) {
                    DynamicArray da = createBaseArray(n, r);
                    da.resetMetrics();
                    long start = System.nanoTime();
                    for (int i = 0; i < ops; i++) {
                        da.add(da.size() / 2, i);
                    }
                    long end = System.nanoTime();
                    timeDAInsMid += (end - start);
                    movesDAInsMid += da.elementMovements;
                    accDAInsMid += da.elementAccesses;
                }
                writer.printf("DynamicArray,%d,Insert,Middle,%d,%d,%d,%d,Theta(n)%n",
                        n, ops, timeDAInsMid / REPETITIONS, movesDAInsMid / REPETITIONS, accDAInsMid / REPETITIONS);

                long timeDARemMid = 0, movesDARemMid = 0, accDARemMid = 0;
                for (int r = 0; r < REPETITIONS; r++) {
                    DynamicArray da = createBaseArray(n, r);
                    da.resetMetrics();
                    long start = System.nanoTime();
                    for (int i = 0; i < ops; i++) {
                        da.remove(da.size() / 2);
                    }
                    long end = System.nanoTime();
                    timeDARemMid += (end - start);
                    movesDARemMid += da.elementMovements;
                    accDARemMid += da.elementAccesses;
                }
                writer.printf("DynamicArray,%d,Remove,Middle,%d,%d,%d,%d,Theta(n)%n",
                        n, ops, timeDARemMid / REPETITIONS, movesDARemMid / REPETITIONS, accDARemMid / REPETITIONS);

                long timeLLIns0 = 0, movesLLIns0 = 0, accLLIns0 = 0;
                for (int r = 0; r < REPETITIONS; r++) {
                    LinkedList ll = createBaseList(n, r);
                    ll.resetMetrics();
                    long start = System.nanoTime();
                    for (int i = 0; i < ops; i++) {
                        ll.add(0, i);
                    }
                    long end = System.nanoTime();
                    timeLLIns0 += (end - start);
                    movesLLIns0 += ll.elementMovements;
                    accLLIns0 += ll.elementAccesses;
                }
                writer.printf("LinkedList,%d,Insert,0,%d,%d,%d,%d,Theta(1)%n",
                        n, ops, timeLLIns0 / REPETITIONS, movesLLIns0 / REPETITIONS, accLLIns0 / REPETITIONS);

                long timeLLRem0 = 0, movesLLRem0 = 0, accLLRem0 = 0;
                for (int r = 0; r < REPETITIONS; r++) {
                    LinkedList ll = createBaseList(n, r);
                    ll.resetMetrics();
                    long start = System.nanoTime();
                    for (int i = 0; i < ops; i++) {
                        ll.remove(0);
                    }
                    long end = System.nanoTime();
                    timeLLRem0 += (end - start);
                    movesLLRem0 += ll.elementMovements;
                    accLLRem0 += ll.elementAccesses;
                }
                writer.printf("LinkedList,%d,Remove,0,%d,%d,%d,%d,Theta(1)%n",
                        n, ops, timeLLRem0 / REPETITIONS, movesLLRem0 / REPETITIONS, accLLRem0 / REPETITIONS);

                long timeLLInsMid = 0, movesLLInsMid = 0, accLLInsMid = 0;
                for (int r = 0; r < REPETITIONS; r++) {
                    LinkedList ll = createBaseList(n, r);
                    ll.resetMetrics();
                    long start = System.nanoTime();
                    for (int i = 0; i < ops; i++) {
                        ll.add(ll.size() / 2, i);
                    }
                    long end = System.nanoTime();
                    timeLLInsMid += (end - start);
                    movesLLInsMid += ll.elementMovements;
                    accLLInsMid += ll.elementAccesses;
                }
                writer.printf("LinkedList,%d,Insert,Middle,%d,%d,%d,%d,Theta(n)%n",
                        n, ops, timeLLInsMid / REPETITIONS, movesLLInsMid / REPETITIONS, accLLInsMid / REPETITIONS);

                long timeLLRemMid = 0, movesLLRemMid = 0, accLLRemMid = 0;
                for (int r = 0; r < REPETITIONS; r++) {
                    LinkedList ll = createBaseList(n, r);
                    ll.resetMetrics();
                    long start = System.nanoTime();
                    for (int i = 0; i < ops; i++) {
                        ll.remove(ll.size() / 2);
                    }
                    long end = System.nanoTime();
                    timeLLRemMid += (end - start);
                    movesLLRemMid += ll.elementMovements;
                    accLLRemMid += ll.elementAccesses;
                }
                writer.printf("LinkedList,%d,Remove,Middle,%d,%d,%d,%d,Theta(n)%n",
                        n, ops, timeLLRemMid / REPETITIONS, movesLLRemMid / REPETITIONS, accLLRemMid / REPETITIONS);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void runWorkload4() {
        String filename = "results/tables/workload4_priority_processing.csv";
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("DataStructure,N,AvgInsertTimeNs,AvgExtractTimeNs,AvgComparisons,AvgMovements,TheoreticalInsert,TheoreticalExtract");

            for (int n : N_VALUES) {
                long totalInsTime = 0;
                long totalExtTime = 0;
                long totalComparisons = 0;
                long totalMovements = 0;

                for (int r = 0; r < REPETITIONS; r++) {
                    Random rng = new Random(SEED + r);
                    int[] data = new int[n];
                    for (int i = 0; i < n; i++) {
                        data[i] = rng.nextInt();
                    }

                    MinHeap heap = new MinHeap(n);
                    heap.resetMetrics();

                    long startIns = System.nanoTime();
                    for (int i = 0; i < n; i++) {
                        heap.insert(data[i]);
                    }
                    long endIns = System.nanoTime();
                    totalInsTime += (endIns - startIns);

                    int prev = Integer.MIN_VALUE;
                    long startExt = System.nanoTime();
                    for (int i = 0; i < n; i++) {
                        int val = heap.extractMin();
                        if (val < prev) {
                            throw new IllegalStateException("MinHeap violated sorted order");
                        }
                        prev = val;
                    }
                    long endExt = System.nanoTime();
                    totalExtTime += (endExt - startExt);

                    totalComparisons += heap.comparisons;
                    totalMovements += heap.movements;
                }

                writer.printf("MinHeap,%d,%d,%d,%d,%d,O(log n),O(log n)%n",
                        n,
                        totalInsTime / REPETITIONS,
                        totalExtTime / REPETITIONS,
                        totalComparisons / REPETITIONS,
                        totalMovements / REPETITIONS);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static DynamicArray createBaseArray(int n, int rep) {
        Random rng = new Random(SEED + rep);
        DynamicArray da = new DynamicArray(n + 1500);
        for (int i = 0; i < n; i++) {
            da.add(rng.nextInt());
        }
        return da;
    }

    private static LinkedList createBaseList(int n, int rep) {
        Random rng = new Random(SEED + rep);
        LinkedList ll = new LinkedList();
        for (int i = 0; i < n; i++) {
            ll.add(rng.nextInt());
        }
        return ll;
    }
}