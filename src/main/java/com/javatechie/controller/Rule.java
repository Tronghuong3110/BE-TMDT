package com.javatechie.controller;

import lombok.Data;

import java.util.LinkedList;
import java.util.Queue;



public class Rule {

    public static class Pair<T, S> {
        public T first;
        public S second;
        public Pair(T first, S second) {
            this.first = first;
            this.second = second;
        }
    }
    public static void main(String[] args) {
        run();
    }

    public static void run() {
        StringBuilder s = new StringBuilder("000000");
        Queue<Pair<StringBuilder, Integer>> q = new LinkedList<>();
        Pair<StringBuilder, Integer> p = new Pair<>(s, s.length());
        q.add(p);
        int len = 0;
        while(!q.isEmpty()) {
            int n = q.size();
//            System.out.println("Size: " + n);
            for(int i = 0; i < n; i++) {
                Pair<StringBuilder, Integer> top = q.poll();
                System.out.print(top.first + ":" + top.second + " ");
                for(int k = top.second - 1; k >= 0; k--) {
                    StringBuilder first = top.first;
                    if(first.charAt(k) != '1') {
                        first.setCharAt(k, '1');
                        StringBuilder tmpStr = new StringBuilder(first);
                        Pair<StringBuilder, Integer> tmp = new Pair<>(tmpStr, k);
                        q.offer(tmp);
                        first.setCharAt(k, '0');
                    }
                }
                len++;
            }
            System.out.println();
        }
        System.out.println("2^" + s.length() + "=" + len);
    }
}
