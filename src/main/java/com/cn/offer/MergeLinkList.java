package com.cn.offer;

/**
 * @autor: lvbiao
 * @version: 1.0
 * @descript:
 * @date: 2019-02-26 23:31
 * 输入两个单调递增的链表，输出两个链表合成后的链表，当然我们需要合成后的链表满足单调不减规则。
 */
public class MergeLinkList {

    public static void main(String[] args){
        ListNode list1 = new ListNode(1);
        list1.next = new ListNode(3);
        list1.next.next = new ListNode(3);
        list1.next.next.next = new ListNode(5);
        list1.next.next.next.next = new ListNode(7);

        ListNode list2 = new ListNode(2);
        list2.next = new ListNode(4);
        list2.next.next = new ListNode(8);
        list2.next.next.next = new ListNode(10);

//        ListNode resultNode = mergeLinkList(list1, list2);
        ListNode resultNode = mergeLinkListRecursion(list1, list2);
        while (resultNode != null) {
            System.out.println(resultNode.val);
            resultNode = resultNode.next;
        }
    }

    /**
     * 非递归版本
     * @param list1
     * @param list2
     * @return
     */
    public static ListNode mergeLinkList(ListNode list1, ListNode list2) {
        if (list2 == null){
            return list1;
        }
        if (list1 == null){
            return list2;
        }

        ListNode resultHead = new ListNode(-1);
        ListNode resultNode = resultHead;

        while (list1 != null && list2 != null){
            if (list1.val < list2.val){
                resultNode.next = list1;
                resultNode = list1;
                list1 = list1.next;
            }else {
                resultNode.next = list2;
                resultNode = list2;
                list2 = list2.next;
            }
        }
        if (list1 != null){
            resultNode.next = list1;
        }
        if (list2 != null){
            resultNode.next = list2;
        }

        return resultHead.next;
    }

    /**
     * 递归版本
     * @param list1
     * @param list2
     * @return
     */
    public static ListNode mergeLinkListRecursion(ListNode list1, ListNode list2) {
        if (list2 == null){
            return list1;
        }
        if (list1 == null){
            return list2;
        }

        if(list1.val <= list2.val){
            list1.next = mergeLinkListRecursion(list1.next, list2);
            return list1;
        }else{
            list2.next = mergeLinkListRecursion(list1, list2.next);
            return list2;
        }
    }

    static class ListNode {
        int val;
        ListNode next = null;

        ListNode(int val) {
            this.val = val;
        }
    }
}
