package com.company;

public class BarnesHutTree {

    private SpaceTime space;

    BarnesHutTree(SpaceTime space) {

        this.space = space;
    }

    TreeNode build(double delta) {

        TreeNode rootTreeNode = new TreeNode(this.space.getWidth(), 0, 0);
        for (Matter body : space.bodies) {

            rootTreeNode.add(body);
        }

        for (Matter body : space.bodies) {

            rootTreeNode.calculateForce(body, delta);
        }

        return rootTreeNode;
    }
}
