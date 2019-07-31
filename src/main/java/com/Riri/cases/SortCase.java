package com.Riri.cases;

import com.Riri.Case;
import com.Riri.annotations.Benchmark;
import com.Riri.annotations.Measurement;

import java.util.Arrays;
import java.util.Random;

/**
 *  1.测试快排排序和归并排序的差别
 *  2.自己实现的归并排序和Arrays.sort对比
 */

@Measurement(iterations = 10000,group = 5)
public class SortCase implements Case{
    //归并排序
    public void mergeSort(int[] arr){
        if(arr.length<=1) return;
        mergeSortInternal(arr,0,arr.length-1);
    }
    private void mergeSortInternal(int[] arr, int low, int high){
        if(low>=high) return;
        int mid=(low+high)>>1;
        mergeSortInternal(arr,low,mid);
        mergeSortInternal(arr,mid+1,high);
        merge(arr,low,mid,high);
    }
    private void merge(int[] arr, int low, int mid, int high){
        int i=low;
        int j=mid+1;
        int[] data=new int[high-low+1];
        int k=0;
        while (i<=mid && j<=high){
            if(arr[i]<=arr[j]){
                data[k++]=arr[i++];
            }
            else data[k++]=arr[j++];
        }
        int start=i;
        int end=mid;
        if(j<=high){
            start=j;
            end=high;
        }
        while (start<=end){
            data[k++]=arr[start++];
        }
        for(i=0;i<=high-low;i++){
            arr[low+i]=data[i];
        }
    }
    //快排
    public  void quickSort(int[] arr){
        if (arr.length<=1) return;
        quickSortInternal(arr,0,arr.length-1);
    }
    private void quickSortInternal(int[] arr,int low,int high){
        if(low>=high) return;
        int randomIndex= (int) (Math.random()*(high-low+1)+low);
        swap(arr,low,randomIndex);
        int value=arr[low];
        int lt=low;
        int rt=high+1;
        int i=low+1;
        while (i<rt){
            if(arr[i]<value){
                swap(arr,i,lt+1);
                lt++;
                i++;
            }
            else if(arr[i]>value) {
                swap(arr,rt-1,i);
                rt--;
            }
            else i++;
        }
        swap(arr,lt,low);
        quickSortInternal(arr,low,lt-1);
        quickSortInternal(arr,rt,high);
    }
    private void swap(int[] arr,int indexA,int indexB) {
        int temp = arr[indexA];
        arr[indexA] = arr[indexB];
        arr[indexB] = temp;
    }
//
//    @Benchmark
//    public void testQuickSort(){
//        int[] arr=new int[10000];
//        Random random=new Random(20190730);
//        for(int i=0;i<arr.length;i++){
//            arr[i]=random.nextInt(100000);
//        }
//        quickSort(arr);
//    }
//    @Benchmark
//    public void testMergeSort() {
//        int[] arr = new int[10000];
//        Random random = new Random(20190730);
//        for (int i = 0; i < arr.length; i++) {
//            arr[i] = random.nextInt(100000);
//        }
//        mergeSort(arr);
//    }
//    @Benchmark
//    public void testArraySort() {
//        int[] arr = new int[10000];
//        Random random = new Random(20190730);
//        for (int i = 0; i < arr.length; i++) {
//            arr[i] = random.nextInt(100000);
//        }
//        Arrays.sort(arr);
//    }
}

