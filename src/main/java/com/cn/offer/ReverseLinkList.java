package com.cn.offer;

/**
 * @autor: lvbiao
 * @version: 1.0
 * @descript:
 * @date: 2019-02-25 22:40
 * 输入一个链表，反转链表后，输出新链表的表头。
 */
public class ReverseLinkList {

    public static void main(String[] args){
        ListNode head = new ListNode(1);
        head.next = new ListNode(2);
        head.next.next = new ListNode(3);
        head.next.next.next = new ListNode(4);
        head.next.next.next.next = new ListNode(5);

        ListNode resultNode = reverseList(head);
        while (resultNode != null) {
            System.out.println(resultNode.val);
            resultNode = resultNode.next;
        }
    }

    /**
     * 反转链表
     * @param head
     * @return
     */
    public static ListNode reverseList(ListNode head) {
        if (null == head){
            return null;
        }

        ListNode currentLastNode = null;
        ListNode currentNode = head;
        while (currentNode != null){
            ListNode currentNextNode = currentNode.next;
            currentNode.next = currentLastNode;
            currentLastNode = currentNode;
            currentNode = currentNextNode;
        }
        return currentLastNode;
    }

    static class ListNode {
        int val;
        ListNode next = null;

        ListNode(int val) {
            this.val = val;
        }
    }
}
