package com.javatechie.controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class Test {
    public static void main(String[] args) {
        try {
            List<Set<String>> transactions = new ArrayList<>();
            String fileName = "groceries.csv";
            BufferedReader reader = new BufferedReader(new FileReader("E:\\Ki_2_nam_4\\KDL_va_KPDL\\BTL\\data\\" + fileName));
            String line;
            while((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                Set<String> transaction = new HashSet<>();
                for(int i = 0; i < parts.length; i++) {
                    List<String> tmp = new ArrayList<>();
                    transaction.add(parts[i]);
                    tmp.add(parts[i]);
//                    supportCount.put(tmp, supportCount.getOrDefault(tmp, 0) + 1);
                }
                transactions.add(transaction);
            }

            double min = 0.01;
            double minSup = Math.ceil(min * transactions.size());

            System.out.println("MinSup: " + minSup);
            Long timeStart = System.currentTimeMillis();
            List<Set<String>> res = apriori(transactions, minSup);
            Long timeEnd = System.currentTimeMillis();
            System.out.println("Frequent itemSet: " + res.size());
            System.out.println("Thoi gian thuc hien: " + (timeEnd - timeStart));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static List<Set<String>> apriori(List<Set<String>> transactions, double minSupport) {
        List<Set<String>> frequentItemsets = new ArrayList<>();
        Map<String, Integer> itemsetCounts = countItemsets(transactions);
        Set<String> frequentOneItemset = new HashSet<>();
        for (Map.Entry<String, Integer> entry : itemsetCounts.entrySet()) {
            double support = (double) entry.getValue() / transactions.size();
            if (support >= minSupport) {
                frequentOneItemset.add(entry.getKey());
            }
        }
        frequentItemsets.add(frequentOneItemset);
        Set<Set<String>> globalPowerSet = generatePowerSet(frequentOneItemset);
        for (Set<String> transaction : transactions) {
//            transaction.removeIf(item -> {
//                boolean contains = frequentOneItemset.contains(item);
//                return !contains;
//            });
            Set<Set<String>> localPowerSet = generatePowerSet(transaction);
            for (Set<String> itemset : globalPowerSet) {
                if (localPowerSet.containsAll(itemset)) {
                    itemsetCounts.put(itemset.toString(), itemsetCounts.getOrDefault(itemset.toString(), 0) + 1);
                }
            }
        }

        // Step 4: Prune infrequent itemsets from global power set
        globalPowerSet.removeIf(itemset -> {
            double support = (double) itemsetCounts.getOrDefault(itemset.toString(), 0) / transactions.size();
            return support < minSupport;
        });

        // Step 5: Convert remaining itemsets to sets of strings
        for (Set<String> itemset : globalPowerSet) {
            Set<String> frequentItemset = new HashSet<>();
            for (String item : itemset.toString().replaceAll("\\[|\\]|\\s", "").split(",")) {
                frequentItemset.add(item.trim());
            }
            frequentItemsets.add(frequentItemset);
        }

        return frequentItemsets;
    }

    public static Map<String, Integer> countItemsets(List<Set<String>> transactions) {
        Map<String, Integer> itemsetCounts = new HashMap<>();
        for (Set<String> transaction : transactions) {
            for (String item : transaction) {
                String itemset = "[" + item + "]";
                itemsetCounts.put(itemset, itemsetCounts.getOrDefault(itemset, 0) + 1);
            }
        }
        return itemsetCounts;
    }

//    public static Set<Set<String>> generatePowerSet(Set<String> itemset) {
//        Set<Set<String>> powerSet = new HashSet<>();
//        powerSet.add(new HashSet<>());  // Add empty set
//        for (String item : itemset) {
//            Set<Set<String>> newSets = new HashSet<>();
//            for (Set<String> subset : powerSet) {
//                Set<String> newSet = new HashSet<>(subset);
//                newSet.add(item);
//                newSets.add(newSet);
//            }
//            powerSet.addAll(newSets);
//        }
//        return powerSet;
//    }

    public static Set<Set<String>> generatePowerSet(Set<String> itemset) {
        Set<Set<String>> powerSet = new HashSet<>();
        generatePowerSetHelper(itemset, new HashSet<>(), powerSet);
        return powerSet;
    }

    private static void generatePowerSetHelper(Set<String> remainingSet, Set<String> currentSet, Set<Set<String>> powerSet) {
        powerSet.add(currentSet);
        for (String item : remainingSet) {
            Set<String> newRemainingSet = new HashSet<>(remainingSet);
            newRemainingSet.remove(item);
            Set<String> newCurrentSet = new HashSet<>(currentSet);
            newCurrentSet.add(item);
            generatePowerSetHelper(newRemainingSet, newCurrentSet, powerSet);
        }
    }
}



/*
* A
* B
* C
* D
*
* AB
* AC
* AD
* BC
* BD
*
* */