package com.mine.oa.util;

import com.google.common.collect.Lists;

import java.util.List;

/***
 *
 * 〈二叉排序树〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class OrderBinary<T extends Comparable> {

    private class Node {
        private T key;
        private Node left;
        private Node right;

        Node(T key) {
            this.key = key;
        }
    }

    /** 根节点 */
    private Node rootNode;

    public OrderBinary(List<T> dataList) {
        this.rootNode = new Node(dataList.get(0));
        for (int i = 1; i < dataList.size(); i++) {
            convertToBinary(this.rootNode, dataList.get(i));
        }
    }

    /***
     * 将数据存储在树的节点中，小于当前节点放入左子节点中，反之放在右子节点中
     * 
     * @param node 节点
     * @param data 数据
     * @return 节点
     */
    private Node convertToBinary(Node node, T data) {
        if (node == null) {
            node = new Node(data);
            return node;
        }
        if (data.compareTo(node.key) < 0) {
            node.left = convertToBinary(node.left, data);
        } else {
            node.right = convertToBinary(node.right, data);
        }
        return node;
    }

    /***
     * 对树进行升序排列
     * 
     * @return 升序结果
     */
    public List<T> ascOrder() {
        List<Node> ascOrder = ascOrder(this.rootNode);
        List<T> list = Lists.newArrayList();
        for (Node node : ascOrder) {
            list.add(node.key);
        }
        return list;
    }

    /***
     * 先取出左下的子节点，其次取出当前节点，最后取出右下的子节点
     * 
     * @param node 当前节点
     * @return 升序节点
     */
    private List<Node> ascOrder(Node node) {
        List<Node> nodeList = Lists.newArrayList();
        if (node.left != null) {
            nodeList.addAll(ascOrder(node.left));
        }
        nodeList.add(node);
        if (node.right != null) {
            nodeList.addAll(ascOrder(node.right));
        }
        return nodeList;
    }

    /***
     * 对树进行降序排列
     * 
     * @return 升序结果
     */
    public List<T> descOrder() {
        List<Node> descOrder = descOrder(this.rootNode);
        List<T> list = Lists.newArrayList();
        for (Node node : descOrder) {
            list.add(node.key);
        }
        return list;
    }

    /***
     * 先取出右下的子节点，其次取出当前节点，最后取出左下的子节点
     * 
     * @param node 前节点
     * @return 降序节点
     */
    private List<Node> descOrder(Node node) {
        List<Node> nodeList = Lists.newArrayList();
        if (node.right != null) {
            nodeList.addAll(descOrder(node.right));
        }
        nodeList.add(node);
        if (node.left != null) {
            nodeList.addAll(descOrder(node.left));
        }
        return nodeList;
    }

    public T min() {
        Node node = this.rootNode;
        while (node.left != null) {
            node = node.left;
        }
        return node.key;
    }

    public T max() {
        Node node = this.rootNode;
        while (node.right != null) {
            node = node.right;
        }
        return node.key;
    }

    public boolean contain(T data){
        return contain(this.rootNode, data);
    }

    private boolean contain(Node node, T data) {
        if (node == null) {
            return false;
        }
        if (node.key.equals(data)) {
            return true;
        }
        if (node.key.compareTo(data) > 0) {
            return contain(node.left, data);
        }
        return contain(node.right, data);
    }

}
