package com.cn.offer;

/**
 * @autor: lvbiao
 * @version: 1.0
 * @descript:
 * @date: 2019-02-25 22:13
 * 输入一个链表，输出该链表中倒数第k个结点。
 */
public class FindKthToTail {

    public static void main(String[] args){

    }

    /**
     * 思路：用两个节点遍历依次即可完成， 先用一个节点遍历k次，然后另一个节点也开始遍历，
     * 当第一个节点遍历完成时，第二个节点刚好遍历到倒数第k个节点
     * @param head
     * @param k
     * @return
     */
    public static ListNode FindKthToTail(ListNode head, int k) {
        if (null == head || k <= 0){
            return null;
        }

        int nodeNum = 1;
        ListNode targetNode = head;
        ListNode tmpNode = head;
        while (tmpNode.next != null) {
            if (nodeNum >= k){
                targetNode = targetNode.next;
            }
            nodeNum++;
            tmpNode = tmpNode.next;
        }
        return nodeNum < k ? null : targetNode;
    }

    class ListNode {
        int val;
        ListNode next = null;

        ListNode(int val) {
            this.val = val;
        }
    }
}
