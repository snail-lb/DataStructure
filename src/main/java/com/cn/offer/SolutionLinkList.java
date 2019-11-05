package com.cn.offer;

/**
 * @autor: lvbiao
 * @version: 1.0
 * 给出两个 非空 的链表用来表示两个非负的整数。其中，它们各自的位数是按照 逆序 的方式存储的，并且它们的每个节点只能存储 一位 数字。
 * <p>
 * 如果，我们将这两个数相加起来，则会返回一个新的链表来表示它们的和。
 * <p>
 * 您可以假设除了数字 0 之外，这两个数都不会以 0 开头。
 * <p>
 * https://leetcode-cn.com/problems/add-two-numbers/
 */
public class SolutionLinkList {

    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        if (l1 == null && l2 == null) {
            return null;
        }

        if (l1 == null || l2 == null) {
            return l1 == null ? l2 : l1;
        }

        ListNode firstNode = new ListNode(-1);
        ListNode currentNode = firstNode;
        int nextVal = 0;
        while (l1 != null || l2 != null || nextVal != 0) {
            int v1 = l1 == null ? 0 : l1.val;
            int v2 = l2 == null ? 0 : l2.val;

            int val = v1 + v2 + nextVal;
            ListNode tmpNode;
            if (val >= 10) {
                nextVal = 1;
                tmpNode = new ListNode(val - 10);
            } else {
                nextVal = 0;
                tmpNode = new ListNode(val);
            }
            l1 = l1 == null ? null : l1.next;
            l2 = l2 == null ? null : l2.next;
            currentNode.next = tmpNode;
            currentNode = tmpNode;
        }
        return firstNode.next;
    }

    static final class ListNode {
        int val;
        ListNode next;

        ListNode(int x) {
            val = x;
        }
    }
}
