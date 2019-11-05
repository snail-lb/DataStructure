package com.cn.offer;

import org.junit.Test;

import static org.junit.Assert.*;

public class SolutionLinkListTest {

    @Test
    public void addTwoNumbers() {
        SolutionLinkList linkList = new SolutionLinkList();
        SolutionLinkList.ListNode l1 = new SolutionLinkList.ListNode(2);
        l1.next = new SolutionLinkList.ListNode(4);
        l1.next.next = new SolutionLinkList.ListNode(3);

        SolutionLinkList.ListNode l2 = new SolutionLinkList.ListNode(5);
        l2.next = new SolutionLinkList.ListNode(6);
        l2.next.next = new SolutionLinkList.ListNode(4);

        SolutionLinkList.ListNode result = linkList.addTwoNumbers(l1, l2);
        SolutionLinkList.ListNode currentNode = result;
        while (currentNode != null) {
            System.out.print(currentNode.val);
            currentNode = currentNode.next;
        }
    }

    @Test
    public void addTwoNumbers1() {
        SolutionLinkList linkList = new SolutionLinkList();
        SolutionLinkList.ListNode l1 = new SolutionLinkList.ListNode(0);
        l1.next = new SolutionLinkList.ListNode(0);
        l1.next.next = new SolutionLinkList.ListNode(3);

        SolutionLinkList.ListNode l2 = new SolutionLinkList.ListNode(5);
        l2.next = new SolutionLinkList.ListNode(6);
        l2.next.next = new SolutionLinkList.ListNode(4);
        l2.next.next.next = new SolutionLinkList.ListNode(4);

        SolutionLinkList.ListNode result = linkList.addTwoNumbers(l1, l2);
        SolutionLinkList.ListNode currentNode = result;
        // 5674
        while (currentNode != null) {
            System.out.print(currentNode.val);
            currentNode = currentNode.next;
        }
    }

    @Test
    public void addTwoNumbers2() {
        SolutionLinkList linkList = new SolutionLinkList();
        SolutionLinkList.ListNode l1 = new SolutionLinkList.ListNode(0);
        l1.next = new SolutionLinkList.ListNode(2);
        l1.next.next = new SolutionLinkList.ListNode(0);

        SolutionLinkList.ListNode l2 = new SolutionLinkList.ListNode(5);
        l2.next = new SolutionLinkList.ListNode(6);
        l2.next.next = new SolutionLinkList.ListNode(4);
        l2.next.next.next = new SolutionLinkList.ListNode(4);

        SolutionLinkList.ListNode result = linkList.addTwoNumbers(l1, l2);
        SolutionLinkList.ListNode currentNode = result;
        // 5844
        while (currentNode != null) {
            System.out.print(currentNode.val);
            currentNode = currentNode.next;
        }
    }

    @Test
    public void addTwoNumbers3() {
        SolutionLinkList linkList = new SolutionLinkList();
        SolutionLinkList.ListNode l1 = new SolutionLinkList.ListNode(5);

        SolutionLinkList.ListNode l2 = new SolutionLinkList.ListNode(5);

        SolutionLinkList.ListNode result = linkList.addTwoNumbers(l1, l2);
        SolutionLinkList.ListNode currentNode = result;
        // 01
        while (currentNode != null) {
            System.out.print(currentNode.val);
            currentNode = currentNode.next;
        }
    }

    @Test
    public void addTwoNumbers4() {
        SolutionLinkList linkList = new SolutionLinkList();
        SolutionLinkList.ListNode l1 = new SolutionLinkList.ListNode(5);
        l1.next = new SolutionLinkList.ListNode(2);

        SolutionLinkList.ListNode l2 = new SolutionLinkList.ListNode(5);

        SolutionLinkList.ListNode result = linkList.addTwoNumbers(l1, l2);
        SolutionLinkList.ListNode currentNode = result;
        // 03
        while (currentNode != null) {
            System.out.print(currentNode.val);
            currentNode = currentNode.next;
        }
    }

    @Test
    public void addTwoNumbers5() {
        SolutionLinkList linkList = new SolutionLinkList();
        SolutionLinkList.ListNode l1 = new SolutionLinkList.ListNode(9);
        l1.next = new SolutionLinkList.ListNode(9);

        SolutionLinkList.ListNode l2 = new SolutionLinkList.ListNode(1);

        SolutionLinkList.ListNode result = linkList.addTwoNumbers(l1, l2);
        SolutionLinkList.ListNode currentNode = result;
        // 001
        while (currentNode != null) {
            System.out.print(currentNode.val);
            currentNode = currentNode.next;
        }
    }
}