package com.company;

public class BarnesHutTree {

    private SpaceTime space;

    public BarnesHutTree(SpaceTime space) {

        this.space = space;
    }

    public TreeNode build() {

        TreeNode rootTreeNode = new TreeNode(this.space.getWidth(), 0, 0);
        for (Matter body : space.bodies) {

            rootTreeNode.add(body);
        }

        for (Matter body : space.bodies) {

            rootTreeNode.calculateForce(body);
        }


        return rootTreeNode;
    }
}
